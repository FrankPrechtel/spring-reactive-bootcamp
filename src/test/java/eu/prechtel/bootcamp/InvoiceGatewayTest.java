package eu.prechtel.bootcamp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvoiceGatewayTest {

    final WebTestClient webTestClient;

    public InvoiceGatewayTest(@Autowired WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void unauthenticatedTest() {
        webTestClient.get()
                .uri("/Invoice/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

	@Disabled("need to fix Cloud Gateway configuration first")
	@Test
    void redirectOnFailure() {
        final String token = "Basic " + Base64.getEncoder().encodeToString("myuser:Password1234".getBytes());
        webTestClient.get()
                .uri("/failure")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }
}
