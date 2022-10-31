package eu.prechtel.reciprocus;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.stream.Stream;

@DisplayName("Mono/Flux examples")
public class MonoFluxTest {

    final Logger log = LoggerFactory.getLogger(MonoFluxTest.class);

    // https://github.com/spring-cloud/spring-cloud-sleuth/issues/1712
    // Spring Cloud Sleuth doesn't cleanup its context on shutdown
    @BeforeAll
    public static void beforeAllTests() {
        Hooks.resetOnEachOperator();
        Hooks.resetOnLastOperator();
        Schedulers.resetOnScheduleHooks();
    }

    @Nested
    class ExampleMono {
        @Test
        void justMono() {
            Mono<String> order =
                    Mono.just("food")
                            .map(String::toUpperCase)
                            .map(entry -> "I'd like to have " + entry + "!");
            StepVerifier.create(order)
                    .expectNext("I'd like to have FOOD!")
                    .verifyComplete();
        }

        @Test
        void deferMono() {
            StepVerifier.create(Mono.defer(() -> Mono.just("food")))
                    .expectNext("food")
                    .verifyComplete();

            StepVerifier.create(Mono.defer(() -> Mono.error(IllegalStateException::new)))
                    .expectError(IllegalStateException.class)
                    .verify();
        }

        // just -> defer -> create: https://stackoverflow.com/a/56116772
    }

    @Nested
    class ExampleFlux {
        @Test
        void justFlux() {
            StepVerifier.create(Flux.just("Hello", "World"))
                    .expectNext("Hello")
                    .expectNext("World")
                    .verifyComplete();
        }

        @Test
        void createFlux() {
            StepVerifier.create(
                    Flux.create(
                            sink -> {
                                sink.next("fine");
                                sink.next("food");
                                sink.error(new IllegalStateException("rotten"));
                                sink.complete();
                            }))
                    .expectNext("fine", "food")
                    .expectError(IllegalStateException.class)
                    .verify();
        }

        @Test
        void matchesFlux() {
            StepVerifier.create(Flux.fromStream(Stream.of("fine", "food", "is", "here", "to", "stay")))
                    .expectNextMatches(entry -> entry.startsWith("f"))
                    .expectNextMatches(entry -> entry.endsWith("ood"))
                    .expectNextCount(2)
                    .thenCancel()
                    .verify();
        }

        @Test
        void subscription() {
            Flux.range(1, 20)
                    .map(i -> {
                        if (i % 3 == 0) {
                            throw new IllegalArgumentException("fizz: " + i);
                        } else {
                            return i;
                        }
                    })
                    .onErrorContinue((t, c) -> log.error(t.getMessage()))
                    .map(Object::toString)
                    .subscribe(log::info, err -> log.error(err.getMessage(), err), () -> log.info("Completed"));
        }

        @Test
        void blockDetection() {

            Scheduler canNotBlock = Schedulers.newParallel("eventLoop", 4);
            Flux<String> stringFlux =
                    Flux.just("a", "b", "c", "d", "e", "f")
                            .subscribeOn(canNotBlock)
                            .log()
                            .map(input -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    // ignore
                                }
                                return input.toUpperCase();
                            });

            StepVerifier.create(stringFlux).expectError(BlockingOperationError.class).verify();
        }

        @Test
        void merge() {
            Flux<Integer> flux1 = Flux.range(2, 4).delayElements(Duration.ofMillis(100));
            Flux<Integer> flux2 = Flux.range(6, 4);
            StepVerifier.create(flux1.mergeWith(flux2).log())
                    .expectNext(6, 7, 8, 9, 2, 3, 4, 5)
                    .verifyComplete();
        }

        // TODO: give it a try
		@Disabled("fix as an exercise for the workshop")
		@Test
		void whatMethod() {
			Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
			Flux<Integer> flux2 = Flux.range(6, 5);
			StepVerifier.create(flux1.mergeWith(flux2).log())
					.expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
					.verifyComplete();
		}
    }

    @Nested
    class ErrorsHappen {

        @Test
        void errorHandling() {
            StepVerifier.create(Flux.range(1, 10).log()
                    .map(i -> {
                        if (i > 2) throw new IllegalArgumentException();
                        else return i;
                    }).onErrorReturn(6))
                    .expectNext(1, 2, 6)
                    .expectComplete()
                    .verify();
        }
    }
}
