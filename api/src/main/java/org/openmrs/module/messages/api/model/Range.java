package org.openmrs.module.messages.api.model;

public class Range<T> {

    private final T start;
    private final T end;

    public Range(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }
}
