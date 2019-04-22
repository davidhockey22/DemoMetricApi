package com.ddouberley.metrics.services;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.MetricSummary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetricServiceImplTest {

    // If the persistence layer were more complex we'd probably want to set a test profile to inject a test store
    @Autowired
    MetricServiceImpl metricService;

    @Test
    public void createMetric() {
        Metric m = metricService.createMetric("TestMetric5");
        Assert.assertNotNull(m);
    }

    @Test
    public void addMetricEntry() {
        Metric m = metricService.createMetric("TestMetric232");
        MetricEntry metricEntry = metricService.addMetricEntry(m.getMetricName(), 4);
        m = metricService.getMetric("TestMetric232");
        Assert.assertEquals(1, m.getMetricEntries().size());
        Assert.assertEquals(4, m.getMetricEntries().get(0).getMetricValue(), .01);

    }

    @Test
    public void getMetricSummary() {
        Metric m = metricService.createMetric("TestMetric330");
        metricService.addMetricEntry(m.getMetricName(), 4);
        MetricSummary ms = metricService.getMetricSummary(m.getMetricName());
        assertNotNull(ms);
    }
}