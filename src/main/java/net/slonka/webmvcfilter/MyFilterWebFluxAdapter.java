package net.slonka.webmvcfilter;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class MyFilterWebFluxAdapter implements WebFilter {
    private final MyFilter filter;

    public MyFilterWebFluxAdapter(MyFilter filter) {
        this.filter = filter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        filter.execute();
        return chain.filter(exchange);
    }
}
