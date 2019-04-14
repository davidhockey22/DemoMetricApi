package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.entities.ReadOptimizedMetric;
import com.ddouberley.metrics.exceptions.MetricConfigurationException;
import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import com.ddouberley.metrics.stores.MetricStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BasicMetricsController extends MetricsController {
    @Value("${server.optimize}")
    String optimizeFor;

    /**
     * Constructs a BasicMetricsController a metric store for persistence
     */
    @Autowired
    public BasicMetricsController(MetricStore metricStore) {
        super(metricStore);
    }

    /**
     * Create and store a new metric
     * @return the created metric
     */
    public Metric createMetric(String metricName) {
        Metric newMetric = newMetric(metricName);
        newMetric = this.metricStore.addMetric(newMetric);
        return newMetric;
    }

    /**
     * Builds a new metrics based on the configured optimization type
     * @return the newly constructed metric
     */
    private Metric newMetric(String metricName) {
        // This could be a factory but seems too simple for that right now
        if("READ".equals(optimizeFor)){
            return new ReadOptimizedMetric(metricName);
        } else{
            throw new MetricConfigurationException("Optimization property was not set correctly");
        }
    }

    /**
     * Adds a metric entry to the metric associated with the metricId provided and stored the updated metric
     * @param metricId the id associated with a metric in the store
     * @param value the value to provide the new metric entry for construction
     * @return the updated metric
     */
    public Metric addMetricEntry(long metricId, float value) {
        synchronized (this) {
            Metric metric = this.metricStore.getMetric(metricId);
            if (metric != null) {
                MetricEntry metricEntry = new MetricEntry(value, metric.getMetricEntries().size());
                metric.addMetricEntry(metricEntry);
                return this.metricStore.updateMetric(metric);
            }
        }
        throw new ResourceNotFoundException();
    }

    /**
     * Gets a new metric summary based on the metric from the store associated with the provided id
     * @param id the id of a metric in the store
     * @return a new mertric summary object
     */
    public MetricSummary getMetricSummary(long id) {
        Metric metric = this.metricStore.getMetric(id);
        if(metric == null){
            throw new ResourceNotFoundException();
        }
        return createMetricSummary(metric);
    }

    /**
     * Creates a metric summary object with the metric provided
     * @param metric metric to base the summary on
     * @return the constructed metric summary
     */
    private MetricSummary createMetricSummary(Metric metric) {
        return new MetricSummary(metric);
    }
}


