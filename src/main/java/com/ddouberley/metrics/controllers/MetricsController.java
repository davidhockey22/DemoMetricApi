package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricEntry;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.services.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MetricsController {

    private final MetricService metricsService;

    @Autowired()
    public MetricsController(MetricService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * Creates a metric
     *
     * @return the metric
     */
    @RequestMapping(value = "/Metric", method = RequestMethod.POST)
    public Metric createMetric(@RequestParam(value = "metricName") String metricName) {
        return metricsService.createMetric(metricName);
    }

    /**
     * Adds an entry with the value provided to the metric associated with the name
     * Note the metric order is not guaranteed
     *
     * @param name  the name of the metric to add the metric entry to
     * @param value the value for the metric entry
     * @return the metrics with the updated metric entries
     */
    @RequestMapping(value = "/MetricEntry", method = RequestMethod.POST)
    public MetricEntry addMetricEntry(@RequestParam(value = "metricName") String name,
                                      @RequestParam(value = "value") float value) {
        return metricsService.addMetricEntry(name, value);
    }

    /**
     * Provided a metric summary for the metric associated with the name argument
     *
     * @param name the name of the metric
     * @return the metric summary
     */
    @RequestMapping(value = "/MetricSummary", method = RequestMethod.GET)
    public MetricSummary getMetricSummary(@RequestParam(value = "metricName") String name) {
        return metricsService.getMetricSummary(name);
    }
}


