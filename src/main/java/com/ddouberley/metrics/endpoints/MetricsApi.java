package com.ddouberley.metrics.endpoints;

import com.ddouberley.metrics.controllers.MetricsController;
import com.ddouberley.metrics.entities.Metric;
import com.ddouberley.metrics.entities.MetricSummary;
import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class MetricsApi {

    private final MetricsController metricsController;

    @Autowired()
    public MetricsApi(MetricsController metricsController) {
        this.metricsController = metricsController;
    }

    /**
     * Creates a metric
     * @return the metric
     */
    @RequestMapping(value = "/CreateMetric", method = RequestMethod.PUT)
    public Metric createMetric(@RequestParam(value = "name") String metricName) {
        return metricsController.createMetric(metricName);
    }

    /**
     * Adds an entry with the value provided to the metric associated with the id
     * Note the metric order is not guaranteed
     * @param id the id of the metric to add to
     * @param value the value for the metric entry
     * @return the metrics with the updated metric entries
     */
    @RequestMapping(value = "/AddMetricEntry", method = RequestMethod.POST)
    public Metric addMetricEntry(@RequestParam(value = "metricId") long id,
                                 @RequestParam(value = "value") float value) {
        try {
            return metricsController.addMetricEntry(id, value);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The metric id was not associated with a stored metric");
        }
    }

    /**
     * Provided a metric summary for the metric associated with the id argument
     * @param id the id of the metric
     * @return the metric summary
     */
    @RequestMapping(value = "/MetricSummary", method = RequestMethod.GET)
    public MetricSummary getMetricSummary(@RequestParam(value = "metricId") long id) {
        try {
            return metricsController.getMetricSummary(id);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The metric id was not associated with a stored metric");
        }
    }
}


