package eu.prechtel.reciprocus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class GingerbreadRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GingerbreadHandler handler) {
        return RouterFunctions
                .route(GET("/reactive/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::get);
    }
}
