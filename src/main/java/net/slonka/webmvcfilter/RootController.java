package net.slonka.webmvcfilter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RootController {
    public RootController() {

    }

    @GetMapping("/")
    public Mono<HttpStatus> returnOk() {
        return Mono.just(HttpStatus.OK);
    }
}
