package com.ddouberley.metrics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadOptimizedMetric extends Metric {

    @JsonIgnore
    private double runningSum;
    @JsonIgnore
    private Float min;
    @JsonIgnore
    private Float max;
    @JsonIgnore
    private PriorityQueue<Float> lowerHalfMaxHeap;
    @JsonIgnore
    private PriorityQueue<Float> upperHalfMinHeap;

    public ReadOptimizedMetric(String metricName) {
        super(metricName);
        this.min = null;
        this.max = null;
        this.lowerHalfMaxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        this.upperHalfMinHeap = new PriorityQueue<>();
    }

    public void addMetricEntry(MetricEntry newEntry) {
        this.runningSum += newEntry.getMetricValue();
        if (this.max == null || newEntry.getMetricValue() > this.max) {
            this.max = newEntry.getMetricValue();
        }
        if (this.min == null || newEntry.getMetricValue() < this.min) {
            this.min = newEntry.getMetricValue();
        }
        this.addToRollingMedian(newEntry);
        this.getMetricEntries().add(newEntry);
    }

    @Override
    public List<MetricEntry> getMetricEntries() {
        return Stream.concat(this.lowerHalfMaxHeap.stream(), this.upperHalfMinHeap.stream())
                        .map(MetricEntry::new)
                        .collect(Collectors.toList());
    }

    private void addToRollingMedian(MetricEntry newEntry) {
        this.upperHalfMinHeap.offer(newEntry.getMetricValue());
        this.lowerHalfMaxHeap.offer(upperHalfMinHeap.poll());
        // Rebalance
        if (upperHalfMinHeap.size() < lowerHalfMaxHeap.size()) {
            upperHalfMinHeap.offer(lowerHalfMaxHeap.poll());
        }
    }

    @JsonIgnore
    public Double getMedian() {
        if (upperHalfMinHeap.isEmpty()) {
            return null;
        }
        if (upperHalfMinHeap.size() > lowerHalfMaxHeap.size()) {
            return new Double(upperHalfMinHeap.peek());
        } else {
            return (upperHalfMinHeap.peek() + lowerHalfMaxHeap.peek()) / 2.0;
        }
    }

    @Override
    @JsonIgnore
    public double getEntrySum() {
        return this.runningSum;
    }


    @Override
    @JsonIgnore
    public Float getMin() {
        return min;
    }

    @Override
    @JsonIgnore
    public Float getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadOptimizedMetric that = (ReadOptimizedMetric) o;
        return Double.compare(that.runningSum, runningSum) == 0 &&
                Objects.equals(getMin(), that.getMin()) &&
                Objects.equals(getMax(), that.getMax()) &&
                Objects.equals(lowerHalfMaxHeap, that.lowerHalfMaxHeap) &&
                Objects.equals(upperHalfMinHeap, that.upperHalfMinHeap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(runningSum, getMin(), getMax(), lowerHalfMaxHeap, upperHalfMinHeap);
    }
}
