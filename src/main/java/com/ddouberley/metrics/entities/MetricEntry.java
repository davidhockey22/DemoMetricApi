package com.ddouberley.metrics.entities;

import java.util.Objects;

public class MetricEntry {
    private float metricValue;

    public MetricEntry(float metricValue) {
        this.metricValue = metricValue;
    }

    public float getMetricValue() {
        return metricValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricEntry that = (MetricEntry) o;
        return Float.compare(that.getMetricValue(), getMetricValue()) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMetricValue());
    }
}
