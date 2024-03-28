package eu.prechtel.bootcamp;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//
@Repository
public interface InvoiceRepository
        extends ReactiveCrudRepository<Invoice, Long> {

    Flux<Invoice> findAllByType(String type);

	Mono<Invoice> findFirstByType(String type);

    Mono<Invoice> findBy(Integer id);
}
