package eu.prechtel.bootcamp;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class InvoiceRepositoryTest {

    final Logger log = LoggerFactory.getLogger(InvoiceRepositoryTest.class);

    @Autowired
	DatabaseClient databaseClient;

    @Autowired
	ConnectionFactory connectionFactory;

    @Autowired
    InvoiceRepository repository;

    @BeforeEach
    void clear() {
        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS Invoice;",
                "CREATE TABLE Invoice (id SERIAL PRIMARY KEY, type VARCHAR(255));"
        );
        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());

        repository.deleteAll().block(Duration.ofSeconds(10));
    }

    @Test
    void findAllByType() {
        repository.save(new Invoice("REGULAR")).block();
        repository.save(new Invoice("REGULAR")).block();
        repository.save(new Invoice("PROFORMA")).block();
        repository.save(new Invoice("OVERDUE")).block();

        Flux<Invoice> InvoiceFlux = repository.findAll();//ByType(Type.REGULAR);\
        StepVerifier.create(InvoiceFlux.log())
                .expectNextCount(1)
                .assertNext(entry -> assertEquals("REGULAR", entry.getType()))
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void findFirstByType() {
        repository.save(new Invoice("REGULAR")).block();
		repository.save(new Invoice("PROFORMA")).block();

		repository.findFirstByType("PROFORMA")
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
    }

    @Test
    void findNoMatch() {
        repository.save(new Invoice("REGULAR")).block();
        repository.save(new Invoice("PROFORMA")).block();

        final Mono<Invoice> overdue = repository.findFirstByType("OVERDUE");
        StepVerifier.create(overdue).expectNextCount(0).expectComplete().verify();
    }

    @Test
    // FIXME: remove @Disabled to activate the test
    @Disabled("fix as an exercise for the workshop")
    void combineFlux() {
        final Invoice regular = repository.save(new Invoice("REGULAR")).block();
        final Invoice proforma = repository.save(new Invoice("PROFORMA")).block();
        final Invoice overdue = repository.save(new Invoice("OVERDUE")).block();

        // FIXME: read all values from database and use only <b>Flux</b> to output the expected result
        ////////////////////////////////////////////////////////////////
        final Flux first = Flux.empty();
		final Flux second = Flux.empty();
		final Flux third = Flux.empty();
		final Flux combination = Flux.empty();
        ////////////////////////////////////////////////////////////////

        StepVerifier.create(combination.log())
                .expectNext(Tuples.of(1, "REGULAR"))
                .expectNext(Tuples.of(2, "PROFORMA"))
                .expectNext(Tuples.of(3, "OVERDUE"))
                .expectComplete()
                .verify();
    }
}
