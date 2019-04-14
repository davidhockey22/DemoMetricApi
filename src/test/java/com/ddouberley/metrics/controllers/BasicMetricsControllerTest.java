package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.exceptions.MetricConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicMetricsControllerTest {

    // If the persistence layer were more complex we'd probably want to set a test profile to inject a test store
    @Autowired
    BasicMetricsController metricsController;

    @Test
    public void createMetric() {
        Metric m = metricsController.createMetric("TestMetric");
        Assert.assertNotNull(m);
    }

    @Test(expected = MetricConfigurationException.class)
    @DirtiesContext() // Want to reset the optimize configuration after this test
    public void createMetricNoOptimizeConfig() {
        metricsController.optimizeFor = "";
        Metric m = metricsController.createMetric("TestMetric");
        Assert.assertNotNull(m);
    }

    @Test
    public void addMetricEntry() {
        Metric m = metricsController.createMetric("TestMetric");
        m = metricsController.addMetricEntry(m.getMetricId(), 4);
        Assert.assertEquals(1, m.getMetricEntries().size());
        Assert.assertEquals(4, m.getMetricEntries().get(0).getMetricValue(), .01);

    }

    @Test
    public void getMetricSummary() {
        Metric m = metricsController.createMetric("TestMetric");
        m = metricsController.addMetricEntry(m.getMetricId(), 4);
        MetricSummary ms = metricsController.getMetricSummary(m.getMetricId());
        assertNotNull(ms);
    }
}