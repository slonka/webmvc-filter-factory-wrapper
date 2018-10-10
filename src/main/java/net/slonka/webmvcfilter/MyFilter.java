package net.slonka.webmvcfilter;

import org.springframework.core.Ordered;

import java.util.concurrent.atomic.AtomicLong;

public class MyFilter implements Ordered {
    private AtomicLong executedAt = new AtomicLong();

    void execute() {
        this.executedAt.set(System.nanoTime());
    }

    public long getExecutedAt() {
        return executedAt.get();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
