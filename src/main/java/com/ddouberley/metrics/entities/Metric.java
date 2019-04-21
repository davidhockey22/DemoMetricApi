package com.ddouberley.metrics.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Metric {
    private String metricName;
    private List<MetricEntry> metricEntries;

    public Metric(String metricName) {
        this.metricName = metricName;
        this.metricEntries = new ArrayList<>();
    }

    public abstract void addMetricEntry(MetricEntry newEntry);

    public List<MetricEntry> getMetricEntries() {
        return metricEntries;
    }

    // TODO note reasoning for separating the kth greatest and smallest

    /**
     * Gets the kth greatest entry by value's value property where k is 0 based index
     * IE k = 0 would return the greatest entry
     *
     * @param k - 0 based distance from greatest entry
     * @return - The kth greatest entry value
     */
    public abstract float getKthGreatestEntryValue(int k);

    /**
     * Gets the kth smallest entry by value's value property where k is 0 based index
     * IE k = 0 would return the minimum value
     *
     * @param k - 0 based distance from the minimum entry
     * @return - The kth smallest value
     */
    public abstract float getKthSmallestEntryValue(int k);

    public abstract double getEntrySum();


    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    // TODO regen the hash and equals
}
