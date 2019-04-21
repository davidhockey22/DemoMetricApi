package com.ddouberley.metrics.stores;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import com.ddouberley.metrics.exceptions.UniqueNameException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetricStore implements MetricStore {
    private Map<String, Metric> metricsMap;

    public InMemoryMetricStore() {
        this.metricsMap = new HashMap<>();
    }

    @Override
    public Metric addMetric(Metric metric) {
        if (metric == null) {
            throw new IllegalArgumentException("Metric must not be null");
        } else if (metricsMap.containsKey(metric.getMetricName())){
            throw new UniqueNameException();
        }
        metricsMap.put(metric.getMetricName(), metric);
        return metric;
    }

    @Override
    public Metric updateMetric(Metric metric) {
        if (metric == null) {
            throw new IllegalArgumentException("Metric must not be null");
        }
        if (metric.getMetricName() != null && metricsMap.containsKey(metric.getMetricName())) {
            metricsMap.put(metric.getMetricName(), metric);
            return metric;
        }
        throw new ResourceNotFoundException();
    }

    @Override
    public Metric getMetric(String metricName) {
        return metricsMap.get(metricName);
    }

    @Override
    public Collection<Metric> getMetrics() {
        return metricsMap.values();
    }

    @Override
    public void deleteMetric(Metric metric) {
        if (metric == null) {
            throw new IllegalArgumentException("Metric must not be null");
        }
        if (!metricsMap.containsKey(metric.getMetricName())) {
            throw new ResourceNotFoundException();
        }
        metricsMap.remove(metric.getMetricName());
    }
}
