package com.ddouberley.metrics.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetricSummary {
    private final String metricName;
    private Double mean;
    private Double median;
    private Float min;
    private Float max;

    public MetricSummary(Metric metric) {
        metricName = metric.getMetricName();
        if (!metric.getMetricEntries().isEmpty()) {
            mean = MetricSummary.calculateMean(metric);
            median = MetricSummary.calculateMedian(metric);
            min = MetricSummary.calculateMin(metric);
            max = MetricSummary.calculateMax(metric);
        }
    }

    /**
     * Gets the minimum value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return float minimum value of the metric entries
     */
    static float calculateMin(Metric metric) {
        return metric.getKthSmallestEntryValue(0);
    }

    /**
     * Gets the maximum value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return float maximum value of the metric entries
     */
    static float calculateMax(Metric metric) {
        return metric.getKthGreatestEntryValue(0);
    }

    /**
     * Calculates the median value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return double the median value of the metric entries
     */
    static double calculateMedian(Metric metric) {
        Collection<MetricEntry> metricEntries = metric.getMetricEntries();
        int size = metricEntries.size();
        if (size % 2 == 0) {
            // Average 2 mean values
            return (metric.getKthSmallestEntryValue(size / 2) +
                    metric.getKthSmallestEntryValue((size / 2) - 1)) / 2;
        }
        return metric.getKthSmallestEntryValue(size / 2);
    }

    /**
     * Calculates the mean value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return double the mean value of the metric entries
     */
    static double calculateMean(Metric metric) {
        return metric.getEntrySum() / metric.getMetricEntries().size();
    }

    public Double getMean() {
        return mean;
    }

    public Double getMedian() {
        return median;
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() {
        return max;
    }

    public String getMetricName() {
        return metricName;
    }
}
