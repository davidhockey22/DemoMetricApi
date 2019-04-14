package com.ddouberley.metrics.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Metric {
    private String metricName;
    private Long metricId;
    private List<MetricEntry> metricEntries;

    public Metric() {
        this.metricEntries = new ArrayList<>();
    }

    public Metric(String metricName) {
        this.metricName = metricName;
        this.metricEntries = new ArrayList<>();
    }

    public abstract void addMetricEntry(MetricEntry newEntry);

    public Long getMetricId() {
        return metricId;
    }

    public void setMetricId(long metricId) {
        this.metricId = metricId;
    }

    public List<MetricEntry> getMetricEntries() {
        return metricEntries;
    }

    // TODO note reasoning for separating the kth greatest and smallest

    /**
     * Gets the kth greatest entry by value's value property where k is 0 based index
     * IE k = 0 would return the greatest entry
     * @param k - 0 based distance from greatest entry
     * @return - The kth greatest entry value
     */
    public abstract float getKthGreatestEntryValue(int k);

    /**
     * Gets the kth smallest entry by value's value property where k is 0 based index
     * IE k = 0 would return the minimum value
     * @param k - 0 based distance from the minimum entry
     * @return - The kth smallest value
     */
    public abstract float getKthSmallestEntryValue(int k);

    public abstract double getEntrySum();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metric metric = (Metric) o;
        return Objects.equals(getMetricName(), metric.getMetricName()) &&
                Objects.equals(getMetricId(), metric.getMetricId()) &&
                Objects.equals(getMetricEntries(), metric.getMetricEntries());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMetricName(), getMetricId(), getMetricEntries());
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
}
