package eu.prechtel.reciprocus;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/index.html
@Configuration
public class GingerbreadGateway {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("rewrite URL", it -> it
                        .path("/gateway/**")
                        // GET http://localhost:8080/gateway/1
                        // GET http://localhost:8080/reactive/1
                        .filters(f -> f.rewritePath("/gateway/(?<segment>.*)", "/reactive/${segment}"))
                        .uri("http://localhost:8080"))
                // redirect URL
                .route("redirect", it -> it
                        .path("/another/**")
                        .and()
                        .host("localhost")
                        .uri("http://localhost:8080/gingerbread/1"))
                // Hystrix is deprecated -> Resilience4j
                .route("hystrix", it -> it
                        .path("/failure/**")
                        .filters(f -> f
                                .hystrix(config -> config
                                        .setName("fallback")
                                        .setFallbackUri("forward:/fallback")))
                        .uri("http://localhost:8080"))
                // weighted routing
                .route("first", it -> it
                        .weight("my-group", 4).and()
                        .path("/distribute/**")
                        .filters(f -> f.rewritePath("/distribute", "/first"))
                        .uri("http://localhost:8080/first")
                )
                .route("second", it -> it
                        .weight("my-group", 4).and()
                        .path("/distribute/**")
                        .filters(f -> f.rewritePath("/distribute", "/second"))
                        .uri("http://localhost:8080/second")
                )
                .route("third", it -> it
                        .weight("my-group", 4).and()
                        .path("/distribute/**")
                        .filters(f -> f.rewritePath("/distribute", "/third"))
                        .uri("http://localhost:8080/third")
                )
                .build();
    }
}
