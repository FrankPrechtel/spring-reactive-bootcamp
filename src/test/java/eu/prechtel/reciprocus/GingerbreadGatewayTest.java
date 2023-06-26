package eu.prechtel.reciprocus;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GingerbreadGatewayTest {

    final WebTestClient webTestClient;

    public GingerbreadGatewayTest(@Autowired WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void unauthenticatedTest() {
        webTestClient.get()
                .uri("/gingerbread/1")
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
