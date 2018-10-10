package net.slonka.webmvcfilter;

import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class MyFilterWebFluxAdapter implements WebFilter, Ordered {
    private final MyFilter filter;

    public MyFilterWebFluxAdapter(MyFilter filter) {
        this.filter = filter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        filter.execute();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return filter.getOrder();
    }
}
