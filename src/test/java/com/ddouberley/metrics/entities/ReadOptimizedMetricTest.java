package com.ddouberley.metrics.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReadOptimizedMetricTest {

    private ReadOptimizedMetric metric;

    @Before
    public void setUp() throws Exception {
        metric = new ReadOptimizedMetric("TestMetric");
        metric.addMetricEntry(new MetricEntry(7));
        metric.addMetricEntry(new MetricEntry(9));
        metric.addMetricEntry(new MetricEntry(4));
        metric.addMetricEntry(new MetricEntry(12));
        metric.addMetricEntry(new MetricEntry(1));
    }

    @Test
    public void addMetricEntry() {
        Assert.assertNotNull(metric.getMetricEntries());
        Assert.assertEquals(5, metric.getMetricEntries().size());
        Assert.assertTrue(metric.getMetricEntries().contains(new MetricEntry(1f)));
        Assert.assertTrue(metric.getMetricEntries().contains(new MetricEntry(4f)));
        Assert.assertTrue(metric.getMetricEntries().contains(new MetricEntry(7f)));
        Assert.assertTrue(metric.getMetricEntries().contains(new MetricEntry(9f)));
        Assert.assertTrue(metric.getMetricEntries().contains(new MetricEntry(12f)));
    }

    @Test
    public void getEntrySum() {
        Assert.assertEquals(33d, metric.getEntrySum(), .00001);
    }

}