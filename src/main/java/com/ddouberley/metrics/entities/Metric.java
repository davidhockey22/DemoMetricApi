package com.ddouberley.metrics.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Metric {
    private String metricName;

    public Metric(String metricName) {
        this.metricName = metricName;
    }

    public abstract void addMetricEntry(MetricEntry newEntry);

    public abstract List<MetricEntry> getMetricEntries();


    public abstract double getEntrySum();


    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public abstract Float getMin();

    public abstract Float getMax();

    public abstract Double getMedian();
}
