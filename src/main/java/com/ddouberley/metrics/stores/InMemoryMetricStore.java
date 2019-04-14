package com.ddouberley.metrics.stores;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryMetricStore extends MetricStore {
    private Map<Long, Metric> metricsMap;
    private AtomicLong nextId = new AtomicLong(0);

    private long getNextId() {
        return this.nextId.getAndAdd(1);
    }

    public InMemoryMetricStore() {
        this.metricsMap = new HashMap<>();
    }

    @Override
    public Metric addMetric(Metric metric) {
        if(metric == null){
            throw new IllegalArgumentException("Metric must not be null");
        }
        if(metric.getMetricId() != null){
            throw new IllegalArgumentException("Metric id is generated value");
        }
        metric.setMetricId(getNextId());
        metricsMap.put(metric.getMetricId(), metric);
        return metric;
    }

    @Override
    public Metric updateMetric(Metric metric) {
        if(metric == null){
            throw new IllegalArgumentException("Metric must not be null");
        }
        if (metric.getMetricId() != null && metricsMap.containsKey(metric.getMetricId())) {
            metricsMap.put(metric.getMetricId(), metric);
            return metric;
        }
        throw new ResourceNotFoundException();
    }

    @Override
    public Metric getMetric(long id) {
        return metricsMap.get(id);
    }

    @Override
    public Collection<Metric> getMetrics() {
        return metricsMap.values();
    }

    @Override
    public void deleteMetric(Metric metric) {
        if(metric == null){
            throw new IllegalArgumentException("Metric must not be null");
        }
        if (!metricsMap.containsKey(metric.getMetricId())) {
            throw new ResourceNotFoundException();
        }
        metricsMap.remove(metric.getMetricId());
    }
}
