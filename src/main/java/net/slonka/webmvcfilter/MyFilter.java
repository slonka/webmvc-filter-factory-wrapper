package net.slonka.webmvcfilter;

public class MyFilter {
    private long executedAt;

    void execute() {
        this.executedAt = System.nanoTime();
    }

    public long getExecutedAt() {
        return executedAt;
    }
}
