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
        Assert.assertEquals(1f, metric.getMetricEntries().get(0).getMetricValue(), .00001);
        Assert.assertEquals(4f, metric.getMetricEntries().get(1).getMetricValue(), .00001);
        Assert.assertEquals(7f, metric.getMetricEntries().get(2).getMetricValue(), .00001);
        Assert.assertEquals(9f, metric.getMetricEntries().get(3).getMetricValue(), .00001);
        Assert.assertEquals(12f, metric.getMetricEntries().get(4).getMetricValue(), .00001);
    }

    @Test
    public void getKthSmallestEntryValue() {
        Assert.assertEquals(1f, metric.getKthSmallestEntryValue(0), .00001);
        Assert.assertEquals(4f, metric.getKthSmallestEntryValue(1), .00001);
        Assert.assertEquals(7f, metric.getKthSmallestEntryValue(2), .00001);
        Assert.assertEquals(9f, metric.getKthSmallestEntryValue(3), .00001);
        Assert.assertEquals(12f, metric.getKthSmallestEntryValue(4), .00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getKthSmallestMoreThanEntries() {
        metric.getKthSmallestEntryValue(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getKthGreatestMoreThanEntries() {
        metric.getKthGreatestEntryValue(5);
    }

    @Test
    public void getKthGreatestEntryValue() {
        Assert.assertEquals(1f, metric.getKthGreatestEntryValue(4), .00001);
        Assert.assertEquals(4f, metric.getKthGreatestEntryValue(3), .00001);
        Assert.assertEquals(7f, metric.getKthGreatestEntryValue(2), .00001);
        Assert.assertEquals(9f, metric.getKthGreatestEntryValue(1), .00001);
        Assert.assertEquals(12f, metric.getKthGreatestEntryValue(0), .00001);

    }

    @Test
    public void getEntrySum() {
        Assert.assertEquals(33d, metric.getEntrySum(), .00001);
    }

    @Test
    public void addMetricSortingDuplicates() {
        Metric m = new ReadOptimizedMetric("Test");
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(7f));
        m.addMetricEntry(new MetricEntry(4f));
        m.addMetricEntry(new MetricEntry(9f));
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(5f));
        m.addMetricEntry(new MetricEntry(7f));
        m.addMetricEntry(new MetricEntry(10f));
        m.addMetricEntry(new MetricEntry(2f));
        Assert.assertEquals(2f, m.getMetricEntries().get(0).getMetricValue(), .0001);
        Assert.assertEquals(4f, m.getMetricEntries().get(1).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(2).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(3).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(4).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(5).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(6).getMetricValue(), .0001);
        Assert.assertEquals(5f, m.getMetricEntries().get(7).getMetricValue(), .0001);
        Assert.assertEquals(7f, m.getMetricEntries().get(8).getMetricValue(), .0001);
        Assert.assertEquals(7f, m.getMetricEntries().get(9).getMetricValue(), .0001);
        Assert.assertEquals(9f, m.getMetricEntries().get(10).getMetricValue(), .0001);
        Assert.assertEquals(10f, m.getMetricEntries().get(11).getMetricValue(), .0001);

    }


}