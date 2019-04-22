package com.ddouberley.metrics.services;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.MetricSummary;

public interface MetricService {
    public abstract Metric createMetric(String metricName);
    public abstract MetricEntry addMetricEntry(String name, float value);
    public abstract MetricSummary getMetricSummary(String name);
    public abstract Metric getMetric(String metricName);
}


