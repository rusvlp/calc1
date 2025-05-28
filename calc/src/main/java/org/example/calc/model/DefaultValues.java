package org.example.calc.model;

import java.util.List;

public class DefaultValues {
    private Metric[] metrics;

    public DefaultValues(Metric[] metrics) {
        this.metrics = metrics;
    }

    public Metric[] getMetrics() {
        return metrics;
    }

    public void setMetrics(Metric[] metrics) {
        this.metrics = metrics;
    }
}
