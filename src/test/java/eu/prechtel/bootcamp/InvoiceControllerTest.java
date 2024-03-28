package eu.prechtel.bootcamp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

import static org.hamcrest.Matchers.*;

//@WebFluxTest(
//        controllers = InvoiceController.class,
//        excludeAutoConfiguration = { ReactiveSecurityAutoConfiguration.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvoiceControllerTest {

    final WebTestClient webTestClient;

    public InvoiceControllerTest(@Autowired WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

	@Disabled("need to fix Cloud Gateway configuration first")
	@Test
    void exchangeInvoice() {
        final String token = "Basic " + Base64.getEncoder().encodeToString("myuser:Password1234".getBytes());
		webTestClient.get()
			.uri("/Invoice/1")
			.header("Authorization", token)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Invoice.class);
    }

    // FIXME: remove @Disabled to activate the test
	@Disabled("fix as an exercise for the workshop")
    @Test
    void feast() {
        // FIXME: delete all Invoice and create and get a list of 2 of them
        ////////////////////////////////////////////////////////////////
        // webTestClient...
        // webTestClient...
        // webTestClient...
        ////////////////////////////////////////////////////////////////

        webTestClient.get()
                .uri("/Invoices")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Invoice.class)
        // FIXME: enable
        ////////////////////////////////////////////////////////////////
        //        .hasSize(2)
        ////////////////////////////////////////////////////////////////
        ;
    }
}
