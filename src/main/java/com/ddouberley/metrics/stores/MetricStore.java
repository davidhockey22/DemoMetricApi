package com.ddouberley.metrics.stores;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;

import java.util.Collection;

public interface MetricStore {
    public abstract Metric addMetric(Metric metric);

    public abstract Metric updateMetric(Metric metric);

    public abstract Metric getMetric(String metricName);

    public abstract Collection<Metric> getMetrics();

    public abstract void deleteMetric(Metric metric);

    public abstract MetricEntry addMetricEntryToMetric(String metricName, MetricEntry metricEntry);
}
