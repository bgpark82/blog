# OCP
Open Close Principle

## Bertrand Meyer의 Object Oriented Software Construction
위키를 찾아보면 OCP라는 말이 처음 나온 것은 프랑스의 출신의 교수인 Bertrand Meyer의 책인 Object Oriented Software Construction이라는 책에서 처음 나왔습니다.
1988년에 처음 나오고 2판이 1997년에 나왔는데 대략 1200 페이지가 넘는 엄청난 분량의 책입니다.

개방 폐쇄 원칙을 지켜야하는 이유는 **모든 것은 변한다** 에서 시작됩니다.

개발을 할 때 요구사항의 변화는 필연적으로 찾아오고 이러한 변화를 겪으면서 안정적인 오래남는 설계를 하기위해 필요한 원칙입니다

개방 폐쇄를 따르는 모듈은 두가지 중요한 속성을 가집니다
1. 확장에 열려있다
2. 수정에 닫혀있다

여기서 중요한 점은 수정에 닫혀있다 라고 생각합니다. 어떤 변경이 일어났을 때, **소스코드가 변하지 않았다면** 수정에 닫혀있다고 정의합니다. 
만약 내가 코드를 수정해서 확장을 했다고 생각했는데 소스코드가 변경된 부분이 생긴다면 그것은 수정이 일어난 것이므로 개방 폐쇄 원칙을 지켰는지 다시한번 생각해 봐야 합니다.
확장에 열려 있다는 것은 변경에 맞게 행위를 추가해서 기존의 모듈을 확장하는 것입니다. 여기서 행위라는 것은 추상화를 의미를 하는데 해당 추상화의 구현 클래스를 만들어서 확장을 할 수 있습니다. 
예를들어 영화를 보고 커피를 마신다라는 행위가 있을 때, 어떤 영화를 보고 어떤 커피를 마시는지 정하는 것은 확장이 되어야 합니다.
여기서 영화를 보고 커피를 마신다는 행위 사이에 식사를 한다라는 행위가 추가되었을 때, 그 변경에 맞게 식사를 하는 행위에 맞게 밥을 먹을지 라면을 먹을지 확장 할 수 있어야 합니다.

### 추상화
모듈들은 추상화로 조작할 수 있어야 합니다. 이러한 모듈들을 추상화에 의존하고 추가적인 구현 모듈들을 만들어서 확장할 수 있습니다.

## 출처
- [OCP Wikipedia](https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle#:~:text=In%20object%2Doriented%20programming%2C%20the,without%20modifying%20its%20source%20code.)
- [Bertrand Meyer's Object Oriented Software Construction](https://web.uettaxila.edu.pk/CMS/AUT2011/seSCbs/tutorial/Object%20Oriented%20Software%20Construction.pdf)
- [Robert C. Martin's The Open Closed Principle](https://drive.google.com/file/d/0BwhCYaYDn8EgN2M5MTkwM2EtNWFkZC00ZTI3LWFjZTUtNTFhZGZiYmUzODc1/view)
- [UncleBob's The Principals of OOD](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)

