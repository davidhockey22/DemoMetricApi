package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.entities.Metric;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MetricsController metricsController; // Use to prepare tests - Could mock this out in future

    @Test
    public void createMetric() throws Exception {
        metricsController.createMetric("TestBeforeMetric");
        String metricName = "TestMetric1000";
        this.mockMvc.perform(post("/Metric")
                .param("metricName", metricName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricName").value(metricName))
                .andReturn();

    }

    @Test
    public void addMetricEntry() throws Exception {
        Metric metric = metricsController.createMetric("TestMetric500");
        this.mockMvc.perform(post("/MetricEntry")
                .param("metricName", metric.getMetricName())
                .param("value", ".021")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricValue", is(.021)));
    }

    @Test
    public void getMetricSummary() throws Exception {
        Metric metric = metricsController.createMetric("TestMetric400");
        metricsController.addMetricEntry(metric.getMetricName(), 1f);
        metricsController.addMetricEntry(metric.getMetricName(), 2f);
        metricsController.addMetricEntry(metric.getMetricName(), 3f);
        metricsController.addMetricEntry(metric.getMetricName(), 4f);
        this.mockMvc.perform(get("/MetricSummary")
                .param("metricName", metric.getMetricName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("metricName").value("TestMetric400"))
                .andExpect(jsonPath("mean").value(2.5))
                .andExpect(jsonPath("median").value(2.5))
                .andExpect(jsonPath("min").value(1))
                .andExpect(jsonPath("max").value(4));
    }

    @Test
    public void addMetricEntryNotFound() throws Exception {
        this.mockMvc.perform(post("/MetricEntry")
                .param("metricName", "1000000")
                .param("value", ".021")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMetricDuplicateName() throws Exception {
        Metric metric = metricsController.createMetric("TestMetric123");
        this.mockMvc.perform(post("/Metric")
                .param("metricName", "TestMetric123")
                .param("value", ".021")
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getMetricSummaryNotFound() throws Exception {
        this.mockMvc.perform(get("/MetricSummary")
                .param("metricName", "1000000")
        )
                .andExpect(status().isNotFound());
    }


}