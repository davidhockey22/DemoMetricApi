package com.ddouberley.metrics.endpoints;

import com.ddouberley.metrics.controllers.MetricsController;
import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MetricsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MetricsController metricsController; // Use to prepare tests - Could mock this out in future

    @Test
    public void createMetric() throws Exception {
        metricsController.createMetric("TestBeforeMetric");
        String metricName = "TestMetric";
        this.mockMvc.perform(put("/CreateMetric")
                .param("name", metricName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricId").exists())
                .andExpect(jsonPath("metricName").value(metricName))
                .andReturn();

    }

    @Test
    public void addMetricEntry() throws Exception {
        Metric metric = metricsController.createMetric("TestMetric");
        this.mockMvc.perform(post("/AddMetricEntry")
                .param("metricId", metric.getMetricId().toString())
                .param("value", ".021")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricEntries").isArray())
                .andExpect(jsonPath("metricEntries").value(
                        Matchers.hasItem(
                            Matchers.<MetricEntry>hasProperty("metricValue", Matchers.is(.021))
                        )
                ))
                .andExpect(jsonPath("metricId").value(metric.getMetricId()));
    }

    @Test
    public void getMetricSummary() throws Exception {
        Metric metric = metricsController.createMetric("TestMetric");
        metricsController.addMetricEntry(metric.getMetricId(), 1f);
        metricsController.addMetricEntry(metric.getMetricId(), 2f);
        metricsController.addMetricEntry(metric.getMetricId(), 3f);
        metricsController.addMetricEntry(metric.getMetricId(), 4f);
        this.mockMvc.perform(get("/MetricSummary")
                .param("metricId", metric.getMetricId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricId").value(metric.getMetricId()))
                .andExpect(jsonPath("metricName").value("TestMetric"))
                .andExpect(jsonPath("mean").value(2.5))
                .andExpect(jsonPath("median").value(2.5))
                .andExpect(jsonPath("min").value(1))
                .andExpect(jsonPath("max").value(4));
    }

    @Test
    public void addMetricEntryNotFound() throws Exception {
        this.mockMvc.perform(post("/AddMetricEntry")
                .param("metricId", "1000000")
                .param("value", ".021")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMetricSummaryNotFound() throws Exception {
        this.mockMvc.perform(get("/MetricSummary")
                .param("metricId", "1000000")
        )
                .andExpect(status().isNotFound());
    }


}