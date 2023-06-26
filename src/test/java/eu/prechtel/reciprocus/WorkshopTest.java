package eu.prechtel.reciprocus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;

@DisplayName("Tests for custom workshop of 2023-06-26")
public class WorkshopTest {
	@Test
	void cleanupResources() {
		byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
		var inputStream = new ByteArrayInputStream(bytes);
		Flux<Integer> flux = Flux.range(0, 10);
		flux
			.filter(i -> i < 5)
			/*.handle((a, b) -> {
				System.out.println(a);
				System.out.println(b);
			})*/
			.doOnNext(i -> System.out.println("Test in:  " + i))
			.doOnDiscard(Integer.class, i -> System.out.println("Test out: " + i))
			.doOnError(error -> System.out.println(error.getMessage()))
			.doFinally(i -> System.out.println("Finally: " + i))
			.subscribe();
	}
}
