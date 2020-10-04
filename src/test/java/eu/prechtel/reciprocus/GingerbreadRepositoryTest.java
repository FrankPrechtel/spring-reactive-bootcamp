package eu.prechtel.reciprocus;

import io.r2dbc.h2.H2ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class GingerbreadRepositoryTest {

    final Logger log = LoggerFactory.getLogger(GingerbreadRepositoryTest.class);

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    H2ConnectionFactory connectionFactory;

    @Autowired
    GingerbreadRepository repository;

    @BeforeEach
    void clear() {
        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS gingerbread;",
                "CREATE TABLE gingerbread (id SERIAL PRIMARY KEY, flavor VARCHAR(255));"
        );
        statements.forEach(it -> databaseClient.execute(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());

        repository.deleteAll().block(Duration.ofSeconds(10));
    }

    @Test
    void findAllByFlavor() {
        repository.save(new Gingerbread("chocolate")).block();
        repository.save(new Gingerbread("chocolate")).block();
        repository.save(new Gingerbread("cinnamon")).block();
        repository.save(new Gingerbread("honey")).block();

        Flux<Gingerbread> gingerbreadFlux = repository.findAll();//ByFlavor("chocolate");\
        StepVerifier.create(gingerbreadFlux.log())
                .expectNextCount(1)
                .assertNext(entry -> assertEquals("chocolate", entry.getFlavor()))
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void findFirstByFlavor() {
        final Gingerbread first = repository.save(new Gingerbread("chocolate")).block();
        log.trace("{}", first);
        final Optional<Gingerbread> second = repository.save(new Gingerbread("cinnamon")).blockOptional();
        log.trace("{}", second);

        final Mono<Gingerbread> cinnamon = repository.findFirstByFlavor(Mono.just("cinnamon"));
        final Mono<Gingerbread> chocolate = repository.findFirstByFlavor(Mono.just("chocolate"));

        StepVerifier.create(cinnamon)
                .assertNext(gingerbread ->
                        assertEquals("cinnamon", gingerbread.getFlavor()))
                .expectComplete()
                .verify();

        StepVerifier.create(chocolate)
                .assertNext(gingerbread ->
                        assertEquals("chocolate", gingerbread.getFlavor()))
                .expectComplete()
                .verify();
    }

    @Test
    void findNoMatch() {
        repository.save(new Gingerbread("chocolate")).block();
        repository.save(new Gingerbread("cinnamon")).block();

        final Mono<Gingerbread> honey = repository.findFirstByFlavor(Mono.just("honey"));
        StepVerifier.create(honey).expectNextCount(0).expectComplete().verify();
    }

    @Test
    // FIXME: remove @Disabled to activate the test
    @Disabled
    void combineFlux() {
        final Gingerbread chocolate = repository.save(new Gingerbread("chocolate")).block();
        final Gingerbread cinnamon = repository.save(new Gingerbread("cinnamon")).block();
        final Gingerbread honey = repository.save(new Gingerbread("honey")).block();

        // FIXME: read all values from database and use only <b>Flux</b> to output the expected result
        ////////////////////////////////////////////////////////////////
        final Flux first = Flux.empty();
        final Flux second = Flux.empty();
        final Flux combination = Flux.empty();
        ////////////////////////////////////////////////////////////////

        StepVerifier.create(combination.log())
                .expectNext(Tuples.of(4, chocolate))
                .expectNext(Tuples.of(5, cinnamon))
                .expectNext(Tuples.of(6, honey))
                .expectComplete()
                .verify();
    }
}