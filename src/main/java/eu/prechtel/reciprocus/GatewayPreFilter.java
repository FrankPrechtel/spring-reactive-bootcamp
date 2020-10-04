package eu.prechtel.reciprocus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayPreFilter implements GlobalFilter {

    final Logger log = LoggerFactory.getLogger(GatewayPreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("prefilter: " + exchange.getAttributes().toString());
        return chain.filter(exchange);
    }
}
