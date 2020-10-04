package eu.prechtel.reciprocus;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class GingerbreadController {

    final Logger log = LoggerFactory.getLogger(GingerbreadController.class);

    final GingerbreadRepository repository;
    final MeterRegistry registry;

    GingerbreadController(
            @Autowired GingerbreadRepository repository,
            @Autowired MeterRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @GetMapping(value = "/gingerbread/{id}")
    public Mono<Gingerbread> singleGingerbread(@PathVariable Integer id) {
        return repository.findBy(id);
    }

    @PostMapping(value = "/gingerbread")
    public Mono<Gingerbread> createGingerbread(@RequestBody Gingerbread gingerbread) {
        return repository.save(gingerbread);
    }

    @GetMapping(value = "/gingerbreads")
    public Flux<Gingerbread> multipleGingerbread() {
        return repository.findAll();
    }

    @DeleteMapping(value = "/gingerbreads")
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
