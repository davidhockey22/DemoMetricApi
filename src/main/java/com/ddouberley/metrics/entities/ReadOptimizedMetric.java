package com.ddouberley.metrics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReadOptimizedMetric extends Metric {

    @JsonIgnore
    // For mean calculation optimization we can keep the running sum
    private double runningSum;

    public ReadOptimizedMetric(String metricName) {
        super(metricName);
    }

    public void addMetricEntry(MetricEntry newEntry) {
        this.runningSum += newEntry.getMetricValue();
        // Add the entry into it's order ascending value location
        for (int i = 0; i < this.getMetricEntries().size(); i++) {
            MetricEntry entry = this.getMetricEntries().get(i);
            if (entry.getMetricValue() > newEntry.getMetricValue()) {
                this.getMetricEntries().add(i, newEntry);
                return;
            }
        }
        this.getMetricEntries().add(newEntry);
    }

    public float getKthGreatestEntryValue(int k){
        if(k >= this.getMetricEntries().size()){
            throw new IllegalArgumentException("The value of k must be less than the number of entries");
        }
        return this.getMetricEntries().get(this.getMetricEntries().size() - 1 - k).getMetricValue();
    }

    public float getKthSmallestEntryValue(int k){
        if(k >= this.getMetricEntries().size()){
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
