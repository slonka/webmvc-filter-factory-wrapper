package net.slonka.webmvcfilter;

import java.util.concurrent.atomic.AtomicLong;

public class MyFilter {
    private AtomicLong executedAt = new AtomicLong();

    void execute() {
        this.executedAt.set(System.nanoTime());
    }

    public long getExecutedAt() {
        return executedAt.get();
    }
}
