---
title: ApplicationEventPublisher
catalog: true
date: 2022-11-15 23:58:33
subtitle:
header-img:
tags:
---



# ApplicationEventPublisher 사용

```java
@Getter
public class PersonEvent {
		private String message;
}
```

```java
@Component
public class PersonService {
  
		@EventListener
  	public void register(PersonEvent event) {
				System.out.println(event.getMessage());
    }
}
```

```java
@Component
@RequiredArgConstructor
public class Application {
  
  	private final ApplicationEventPublisher publisher;
  
  	public void publish() {
      	publisher.publish(new PersonEvent("박병길"));
    }
}
```





# 내부 동작 과정

ApplicationEventPublisher는 사용자가 생성한 Event를 @EventListener의 메소드 파라미터로 전달합니다.

내부적으로 아래와 같이 동작합니다

![image-20221116210819265](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221116210819265.png)

1. ApplicationEventPublisher가 publishEvent로 사용자가 만든 Event (POJO) 를 전송합니다.
2. ApplicationEventPublisher는 내부에 ApplicationEventMultiCaster를 가집니다. ApplicationEventMultiCaster는 ApplicationListener을 List 형태로 가지고 있습니다. ApplicationListener는 @EventListener로 등록된 Proxy 빈입니다.
3. ApplicationEventMultiCaster는 List를 순회하면서 ApplicationListener의 onApplicationEvent 메소드를 실행합니다.
4. Proxy 객체의 메소드의 파라미터 Event를 전달하게 됩니다.



![image-20221116202731558](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221116202731558.png)







## EventListener 등록

@EventListener를 메소드에 등록하면 스프링 컨텍스트에 등록이 됩니다.

![image-20221116201031217](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221116201031217.png)

EventListener는 EventListenerMethodProcessor에 의해 Listener로 등록됩니다.

EventListenerMethodProcessor는 말그대로 스프링 컨텍스트에 빈이 등록되기 전에 후처리를 합니다. EventListenerMethodProcessor는 SmartInitializingSingleton을 상속받아 afterSingletonsInstantiated를 실행합니다. 해당 메소드는 스프링 컨텍스트에서 @EventListener를 가지는 인스턴스를 Listener로 등록하게 됩니다.

```java
@Override
public void afterSingletonsInstantiated() {
    	...
      try {
        processBean(beanName, type);
      }
		  catch (Throwable ex) { }
  		...
		
	}
```

```java
private void processBean(final String beanName, final Class<?> targetType) {
    ...	
      ApplicationListener<?> applicationListener =
      factory.createApplicationListener(beanName, targetType, methodToUse);
  		// applicationListener가 ApplicationListenerMethodAdapter라면 
  		if (applicationListener instanceof ApplicationListenerMethodAdapter) {
        	((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
      }
  		context.addApplicationListener(applicationListener);
  	...
}
```



ApplicationEventPubliser를 구현한 AbstractApplicationContext에서 registerListeners로 ApplicationListener들을 등록합니다.

```java
	protected void registerListeners() {
		// Register statically specified listeners first.
		for (ApplicationListener<?> listener : getApplicationListeners()) {
			getApplicationEventMulticaster().addApplicationListener(listener);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let post-processors apply to them!
		String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
		for (String listenerBeanName : listenerBeanNames) {
			getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
		}

		// Publish early application events now that we finally have a multicaster...
		Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
		this.earlyApplicationEvents = null;
		if (!CollectionUtils.isEmpty(earlyEventsToProcess)) {
			for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
				getApplicationEventMulticaster().multicastEvent(earlyEvent);
			}
		}
	}
```



ApplicationListenerMethodAdapter





## ApplicationPublisher 실행

![image-20221116193932486](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221116193932486.png)

ApplicationEventMulticaster

```java
@Override
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
		ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
		Executor executor = getTaskExecutor();
		for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
			if (executor != null) {
				executor.execute(() -> invokeListener(listener, event));
			}
			else {
				invokeListener(listener, event);
			}
		}
	}
```



# @TrasnactionalEventListener

@EventListener의 동작과정과 조금 다릅니다. TransactionalApplicationListenerAdapter

![image-20221116003143960](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221116003143960.png)

# Reference

https://velog.io/@jeongyunsung/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%ED%95%B4%EB%B6%80%ED%95%99-Event1-EventListener-EventPublisher

https://reflectoring.io/spring-boot-application-events-explained/

https://www.baeldung.com/spring-events



