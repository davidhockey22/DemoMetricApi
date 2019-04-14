package com.ddouberley.metrics.entities;

import java.util.Objects;

public class MetricEntry {
    private float metricValue;
    private int order;

    public MetricEntry(float metricValue, int order) {
        this.metricValue = metricValue;
        this.order = order;
    }

    public float getMetricValue() {
        return metricValue;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricEntry that = (MetricEntry) o;
        return Float.compare(that.getMetricValue(), getMetricValue()) == 0 &&
                getOrder() == that.getOrder();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMetricValue(), getOrder());
    }
}
