package com.ddouberley.metrics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReadOptimizedMetric extends Metric {

    @JsonIgnore
    private double runningSum;

    public ReadOptimizedMetric(String metricName) {
        super(metricName);
    }

    public void addMetricEntry(MetricEntry newEntry) {
        this.runningSum += newEntry.getMetricValue();
        if (this.getMetricEntries().isEmpty()) {
            this.getMetricEntries().add(newEntry);
            return;
        }
        int index = this.findSortedPosition(0, this.getMetricEntries().size() - 1, newEntry);
        this.getMetricEntries().add(index, newEntry);

    }

    /**
     * Binary search to find correct sorted position for the metric entry
     * @param low
     * @param high
     * @param newEntry
     * @return
     */
    private int findSortedPosition(int low, int high, MetricEntry newEntry) {
        if (low >= high) {
            // we found the closest value now correct for this value
            if (this.getMetricEntries().get(low).getMetricValue() < newEntry.getMetricValue()) {
                return low + 1;
            } else {
                return low;
            }
        }
        int middle = low + ((high - low) / 2);
        float middleValue = this.getMetricEntries().get(middle).getMetricValue();
        if (newEntry.getMetricValue() < middleValue) {
            return findSortedPosition(low, middle - 1, newEntry);
        } else if (newEntry.getMetricValue() > middleValue) {
            return findSortedPosition(middle + 1, high, newEntry);
        }
        return middle;
    }

    public float getKthGreatestEntryValue(int k) {
        if (k >= this.getMetricEntries().size()) {
            throw new IllegalArgumentException("The value of k must be less than the number of entries");
        }
        return this.getMetricEntries().get(this.getMetricEntries().size() - 1 - k).getMetricValue();
    }

    public float getKthSmallestEntryValue(int k) {
        if (k >= this.getMetricEntries().size()) {
            throw new IllegalArgumentException("The value of k must be less than the number of entries");
        }
        return this.getMetricEntries().get(k).getMetricValue();
    }

    @Override
    @JsonIgnore
    public double getEntrySum() {
        return this.runningSum;
    }

}
