package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import groovy.util.logging.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class PlaceReactiveServiceTest {

    private static final String CACHE = "place";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static Flux<PlaceDocument> places;

    @Autowired
    private CacheManager cacheManager;


    @BeforeEach
    void setUp() {
        List<PlaceDocument> list = IntStream.range(1, 10)
                .mapToObj(num -> new PlaceDocument(num + " ", num + ""))
                .peek((i) -> System.out.println("cache missed"))
                .collect(Collectors.toList());

        places = Flux.fromIterable(list);
        getPlaceDocumentFlux(places);
    }

    @Test
    void putCache() {
        cacheManager.getCache(CACHE).put(2, places);

        Flux<PlaceDocument> actual = (Flux<PlaceDocument>) cacheManager.getCache(CACHE).get(2).get();

        actual.doOnNext(System.out::println)
                .subscribe();
        assertThat(actual).isEqualTo(places);
    }

    @Test
    void handleCache() throws InterruptedException {
        System.out.println(cacheManager.getCache(CACHE).get(1).get());;
    }

    @Test
    void justOrEmpty() {
        // if mono has null value, then it will return Mono.empty()
        Mono<Cache.ValueWrapper> nullValue = Mono.justOrEmpty(null);
        Mono<Cache.ValueWrapper> value = Mono.justOrEmpty(cacheManager.getCache(CACHE).get(KEY));

        assertThat(nullValue).isEqualTo(Mono.empty());
        assertThat(value).isEqualTo(Mono.empty());
    }

    @Test
    void fromRunnable() {
        System.out.println("start");
        Mono<String> mono = Mono.fromRunnable(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("hello");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("end");
        mono.subscribe();
    }

    @Test
    void logging() {
        Flux<Integer> flux = Flux.range(1, 10)
                .doOnNext(item -> System.out.println(item))
                .take(3);

        flux.subscribe();
    }

    @Test
    void materialize() {
        Mono<List<Signal<Integer>>> listMono = Mono.justOrEmpty(Lists.newArrayList(1, 2, 3))
                .flatMap(item -> Flux.fromIterable(item)
                            .materialize()
                            .collectList());

        listMono.subscribe(System.out::println);
    }

    @Test
    void dematerialize() {
        List<Object> list = new ArrayList<>();

        Flux<Signal<Integer>> materialize = Flux.fromIterable(Lists.newArrayList(1, 2, 3))
                .materialize();

        materialize
                .doOnNext(System.out::println)
                .dematerialize()
                .doOnNext(System.out::println)
                .collectList()
                .doOnNext(i -> list.add(i))
                .then().subscribe();


        list.forEach(System.out::println);

    }

    private Flux<PlaceDocument> getPlaceDocumentFlux(Flux<PlaceDocument> places) {
        return CacheFlux.lookup(getCache(), 1)
                .onCacheMissResume(places)
                .andWriteWith(saveCache());
    }

    private BiFunction<Integer, List<Signal<PlaceDocument>>, Mono<Void>> saveCache() {
        return (key, signals) ->
                Flux.fromIterable(signals)
                        .dematerialize()
                        .collectList()
                        .doOnNext(System.out::println)
                        .doOnNext(list -> cacheManager.getCache(CACHE).put(key, list))
                        .then();
    }

    private Function<Integer, Mono<List<Signal<PlaceDocument>>>> getCache() {
        return key -> Mono.justOrEmpty(getPlacesFromCache(key))
                .flatMap(p -> Flux.fromIterable(p).materialize().collectList());
    }

    private <T> List<PlaceDocument> getPlacesFromCache(T key) {
        return (List<PlaceDocument>) cacheManager.getCache(CACHE).get(key).get();
    }
}