package com.ddouberley.metrics.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

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
        return metric.getMin();
    }

    /**
     * Gets the maximum value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return float maximum value of the metric entries
     */
    static float calculateMax(Metric metric) {
        return metric.getMax();
    }

    /**
     * Calculates the median value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return double the median value of the metric entries
     */
    static double calculateMedian(Metric metric) {
       return metric.getMedian();
    }

    /**
     * Calculates the mean value of the metric entries
     *
     * @param metric the metric holding the metric entries
     * @return double the mean value of the metric entries
     */
    static Double calculateMean(Metric metric) {
        if(metric.getMetricEntries().size() > 0) {
            return metric.getEntrySum() / metric.getMetricEntries().size();
        }
        return null;
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
