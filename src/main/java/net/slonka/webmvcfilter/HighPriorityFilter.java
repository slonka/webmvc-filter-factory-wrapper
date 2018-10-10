package net.slonka.webmvcfilter;

public class HighPriorityFilter extends MyFilter {
    @Override
    public int getOrder() {
        return -1;
    }
}
