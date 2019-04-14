package com.ddouberley.metrics.entities;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReadOptimizedMetricTest {

    @Test
    public void addMetricEntry() {
        Metric metric = buildTestMetric();
        Assert.assertNotNull(metric.getMetricEntries());
        Assert.assertEquals(5, metric.getMetricEntries().size());
        Assert.assertEquals(1f, metric.getMetricEntries().get(0).getMetricValue(), .00001);
        Assert.assertEquals(4f, metric.getMetricEntries().get(1).getMetricValue(), .00001);
        Assert.assertEquals(7f, metric.getMetricEntries().get(2).getMetricValue(), .00001);
        Assert.assertEquals(9f, metric.getMetricEntries().get(3).getMetricValue(), .00001);
        Assert.assertEquals(12f, metric.getMetricEntries().get(4).getMetricValue(), .00001);
    }

    @Test
    public void getKthSmallestEntryValue() {
        Metric metric = buildTestMetric();
        Assert.assertEquals(1f, metric.getKthSmallestEntryValue(0), .00001);
        Assert.assertEquals(4f, metric.getKthSmallestEntryValue(1), .00001);
        Assert.assertEquals(7f, metric.getKthSmallestEntryValue(2), .00001);
        Assert.assertEquals(9f, metric.getKthSmallestEntryValue(3), .00001);
        Assert.assertEquals(12f, metric.getKthSmallestEntryValue(4), .00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getKthSmallestMoreThanEntries() {
        Metric metric = buildTestMetric();
        metric.getKthSmallestEntryValue(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getKthGreatestMoreThanEntries() {
        Metric metric = buildTestMetric();
        metric.getKthGreatestEntryValue(5);
    }

    @Test
    public void getKthGreatestEntryValue() {
        Metric metric = buildTestMetric();
        Assert.assertEquals(1f, metric.getKthGreatestEntryValue(4), .00001);
        Assert.assertEquals(4f, metric.getKthGreatestEntryValue(3), .00001);
        Assert.assertEquals(7f, metric.getKthGreatestEntryValue(2), .00001);
        Assert.assertEquals(9f, metric.getKthGreatestEntryValue(1), .00001);
        Assert.assertEquals(12f, metric.getKthGreatestEntryValue(0), .00001);

    }

    @Test
    public void getEntrySum() {
        Metric metric = buildTestMetric();
        Assert.assertEquals(33d, metric.getEntrySum(), .00001);
    }


    private Metric buildTestMetric() {
        Metric metric = new ReadOptimizedMetric("TestMetric");
        metric.addMetricEntry(new MetricEntry(7, 0));
        metric.addMetricEntry(new MetricEntry(9, 1));
        metric.addMetricEntry(new MetricEntry(4, 2));
        metric.addMetricEntry(new MetricEntry(12, 3));
        metric.addMetricEntry(new MetricEntry(1, 4));
        return metric;
    }
}