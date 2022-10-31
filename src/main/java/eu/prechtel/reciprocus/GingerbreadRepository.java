package eu.prechtel.reciprocus;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//
@Repository
public interface GingerbreadRepository
        extends ReactiveCrudRepository<Gingerbread, Long> {

    Flux<Gingerbread> findAllByFlavor(String flavor);

	Mono<Gingerbread> findFirstByFlavor(String flavor);

    Mono<Gingerbread> findBy(Integer id);
}
