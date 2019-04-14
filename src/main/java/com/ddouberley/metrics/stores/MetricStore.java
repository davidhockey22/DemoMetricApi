package com.ddouberley.metrics.stores;

import com.ddouberley.metrics.entities.Metric;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public abstract class MetricStore{
    public abstract Metric addMetric(Metric metric);
    public abstract Metric updateMetric(Metric metric);
    public abstract Metric getMetric(long id);
    public abstract Collection<Metric> getMetrics();
    public abstract void deleteMetric(Metric metric);
}
