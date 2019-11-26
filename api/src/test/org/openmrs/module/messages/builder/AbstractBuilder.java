package org.openmrs.module.messages.builder;

public abstract class AbstractBuilder<T extends Object> {

    protected static int instanceNumber = 0;

    public abstract T build();

    protected AbstractBuilder() {
        instanceNumber++;
    }

    protected int getInstanceNumber() {
        return instanceNumber;
    }
}
