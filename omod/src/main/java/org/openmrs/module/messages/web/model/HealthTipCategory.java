package org.openmrs.module.messages.web.model;

/**
 * The HealthTipCategory Class is a class of an immutable data object which stored basic information about Health Tip
 * Category.
 * It's used to return these information to the FE client.
 */
public final class HealthTipCategory {
    private final String label;
    private final String name;

    public HealthTipCategory(String label, String name) {
        this.label = label;
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }
}
