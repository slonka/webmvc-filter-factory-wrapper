package net.slonka.webmvcfilter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilterWebMvcAdapter extends OncePerRequestFilter implements Ordered {
    private final MyFilter filter;

    public MyFilterWebMvcAdapter(MyFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filter.execute();
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return filter.getOrder();
    }
}
