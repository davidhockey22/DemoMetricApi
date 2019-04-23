package com.ddouberley.metrics.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MetricSummaryTest {
    private Metric metric;

    @Before
    public void setUp() throws Exception {
        metric = new ReadOptimizedMetric("MetricTest");
        metric.addMetricEntry(new MetricEntry(1.01f));
        metric.addMetricEntry(new MetricEntry(9.056f));
        metric.addMetricEntry(new MetricEntry(.01f));
        metric.addMetricEntry(new MetricEntry(.02f));
        metric.addMetricEntry(new MetricEntry(.02f));
        metric.addMetricEntry(new MetricEntry(.03f));
        metric.addMetricEntry(new MetricEntry(.03f));
    }

    @Test
    public void calculateMin() {
        Assert.assertEquals(.01f, MetricSummary.calculateMin(metric), .0001);
        metric.addMetricEntry(new MetricEntry(.002f));
        Assert.assertEquals(.002f, MetricSummary.calculateMin(metric), .0001);
    }


    @Test
    public void calculateMax() {
        Assert.assertEquals(9.056f, MetricSummary.calculateMax(metric), .0001);
        metric.addMetricEntry(new MetricEntry(9.057f));
        Assert.assertEquals(9.057f, MetricSummary.calculateMax(metric), .0001);


    }

    @Test
    public void calculateMedian() {
        Assert.assertEquals(.02999d, MetricSummary.calculateMedian(metric), .001);
        metric.addMetricEntry(new MetricEntry(.04f));
        Assert.assertEquals(.03d, MetricSummary.calculateMedian(metric), .001);
    }

    @Test
    public void calculateMean() {
        Assert.assertEquals(1.45371d, MetricSummary.calculateMean(metric), .0001);
        metric.addMetricEntry(new MetricEntry(9.057f));
        Assert.assertEquals(2.40412d, MetricSummary.calculateMean(metric), .0001);
    }


}