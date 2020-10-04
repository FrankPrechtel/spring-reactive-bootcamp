package eu.prechtel.reciprocus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class GingerbreadHandler {

    final GingerbreadRepository repository;

    GingerbreadHandler(@Autowired GingerbreadRepository repository) {
        this.repository = repository;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> get(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return repository.findBy(id)
                .flatMap(it -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
