package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.stores.MetricStore;
import org.springframework.stereotype.Service;

@Service
public abstract class MetricsController {

    final MetricStore metricStore;

    public MetricsController(MetricStore metricStore) {
        this.metricStore = metricStore;
    }

    public abstract Metric createMetric(String metricName);


    public abstract Metric addMetricEntry(long id, float value);

    public abstract MetricSummary getMetricSummary(long id);
}


