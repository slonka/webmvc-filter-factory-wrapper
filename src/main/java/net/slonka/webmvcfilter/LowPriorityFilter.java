package net.slonka.webmvcfilter;

public class LowPriorityFilter extends MyFilter {
    @Override
    public int getOrder() {
        return 1;
    }
}
