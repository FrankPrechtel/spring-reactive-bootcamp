package eu.prechtel.bootcamp;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class InvoiceController {

    final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    final InvoiceRepository repository;
    final MeterRegistry registry;

    InvoiceController(
            @Autowired InvoiceRepository repository,
            @Autowired MeterRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @GetMapping(value = "/Invoice/{id}")
    public Mono<Invoice> singleInvoice(@PathVariable Integer id) {
        return repository.findBy(id);
    }

    @PostMapping(value = "/Invoice")
    public Mono<Invoice> createInvoice(@RequestBody Invoice Invoice) {
        return repository.save(Invoice);
    }

    @GetMapping(value = "/Invoices")
    public Flux<Invoice> multipleInvoice() {
        return repository.findAll();
    }

    @DeleteMapping(value = "/Invoices")
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        // http://localhost:8080/actuator/metrics/fallback
        log.info("increase counter");
        registry.counter("fallback").increment();
        return Mono.just("fallback");
    }

    @RequestMapping("/delay")
    public Mono<String> delay() throws Exception {
        Thread.sleep(5_000L);
        return Mono.just("delay");
    }

    @RequestMapping("/alwaysfail")
    public Mono<String> alwaysfail() throws Exception {
        Thread.sleep(5_000L);
        return Mono.error(new IllegalArgumentException("this service will always fail"));
    }

    @RequestMapping("/first")
    public Mono<String> first() {
        return Mono.just("first path");
    }

    @RequestMapping("/second")
    public Mono<String> second() {
        return Mono.just("second path");
    }

    @RequestMapping("/third")
    public Mono<String> third() {
        return Mono.just("third path");
    }
}
