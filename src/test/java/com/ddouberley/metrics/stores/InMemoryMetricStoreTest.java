package com.ddouberley.metrics.stores;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.ReadOptimizedMetric;
import com.ddouberley.metrics.stores.InMemoryMetricStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Want to reset the store state after each
public class InMemoryMetricStoreTest {

    @Autowired()
    InMemoryMetricStore metricStore;


    @Test
    public void addMetric() {
        Metric metric = new ReadOptimizedMetric("MetricTest");
        metric = metricStore.addMetric(metric);
        Metric metric2 = new ReadOptimizedMetric("MetricTest2");
        metric2 = metricStore.addMetric(metric2);
        Assert.assertTrue(metricStore.getMetrics().contains(metric));
        Assert.assertNotNull(metric.getMetricId());
        Assert.assertTrue(metricStore.getMetrics().contains(metric2));
        Assert.assertNotNull(metric2.getMetricId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullMetric() {
        metricStore.addMetric(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMetricWithInvalidId() {
        Metric metric = new ReadOptimizedMetric("MetricTest");
        metric.setMetricId(0L);
        metric = metricStore.addMetric(metric);
    }

    @Test
    public void updateMetric() {
        Metric metric = new ReadOptimizedMetric("MetricTest");
        metric = metricStore.addMetric(metric);
        Metric metric2 = new ReadOptimizedMetric("MetricTest2");
        metric2 = metricStore.addMetric(metric2);

        // Create new reference for updating so we know it's not just changing the current reference
        // but persisting to store
        Metric metric2Update = new ReadOptimizedMetric("MetricTest");
        metric2Update.setMetricId(metric2.getMetricId());
        metric2Update.addMetricEntry(new MetricEntry(4f, 0));
        metric2 = metricStore.updateMetric(metric2Update);

        Assert.assertEquals(metricStore.getMetric(metric2.getMetricId()).getMetricEntries().get(0).getMetricValue(), 4f, .01);
        Assert.assertTrue(metricStore.getMetric(metric.getMetricId()).getMetricEntries().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullMetric() {
        metricStore.updateMetric(null);
    }

    @Test
    public void getMetrics() {
        Assert.assertTrue(metricStore.getMetrics().isEmpty());
        Metric metric = new ReadOptimizedMetric("MetricTest");
        metric = metricStore.addMetric(metric);
        Metric metric2 = new ReadOptimizedMetric("MetricTest2");
        metric2 = metricStore.addMetric(metric2);
        Assert.assertEquals(this.metricStore.getMetrics().size(), 2);
        Assert.assertTrue(this.metricStore.getMetrics().contains(metric));
        Assert.assertTrue(this.metricStore.getMetrics().contains(metric2));
    }

    @Test
    public void deleteMetric() {
        Metric metric = new ReadOptimizedMetric("MetricTest");
        metric = metricStore.addMetric(metric);
        Metric metric2 = new ReadOptimizedMetric("MetricTest2");
        metric2 = metricStore.addMetric(metric2);
        metricStore.deleteMetric(metric2);
        Assert.assertNull(metricStore.getMetric(metric2.getMetricId()));
        Assert.assertNotNull(metricStore.getMetric(metric.getMetricId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullMetric() {
        metricStore.deleteMetric(null);
    }

}