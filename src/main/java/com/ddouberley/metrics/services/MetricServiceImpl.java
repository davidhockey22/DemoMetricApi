package com.ddouberley.metrics.services;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.entities.ReadOptimizedMetric;
import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import com.ddouberley.metrics.stores.MetricStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricServiceImpl implements MetricService {
    private MetricStore metricStore;

    /**
     * Constructs a BasicMetricsController with a metric store for persistence
     */
    @Autowired
    public MetricServiceImpl(MetricStore metricStore) {
        this.metricStore = metricStore;
    }

    /**
     * Create and store a new metric
     *
     * @return the created metric
     */
    public Metric createMetric(String metricName) {
        Metric newMetric = newMetric(metricName);
        newMetric = this.metricStore.addMetric(newMetric);
        return newMetric;
    }

    /**
     * Builds a new metrics based on the configured optimization type
     *
     * @return the newly constructed metric
     */
    private Metric newMetric(String metricName) {
        // This could be a factory but seems too simple for that right now
        return new ReadOptimizedMetric(metricName);
    }

    /**
     * Adds a metric entry to the metric associated with the metricName provided and stored the updated metric
     *
     * @param metricName the name associated with a metric in the store
     * @param value      the value to provide the new metric entry for construction
     * @return the updated metric
     */
    public MetricEntry addMetricEntry(String metricName, float value) {
        MetricEntry metricEntry = new MetricEntry(value);
        return metricStore.addMetricEntryToMetric(metricName, metricEntry);
    }

    /**
     * Gets a new metric summary based on the metric from the store associated with the provided name
     *
     * @param metricName the name of a metric in the store
     * @return a new mertric summary object
     */
    public MetricSummary getMetricSummary(String metricName) {
        Metric metric = this.metricStore.getMetric(metricName);
        if (metric == null) {
            throw new ResourceNotFoundException();
        }
        return createMetricSummary(metric);
    }

    /**
     * Creates a metric summary object with the metric provided
     *
     * @param metric metric to base the summary on
     * @return the constructed metric summary
     */
    private MetricSummary createMetricSummary(Metric metric) {
        return new MetricSummary(metric);
    }

    public Metric getMetric(String metricName) {
        return metricStore.getMetric(metricName);
    }
}


