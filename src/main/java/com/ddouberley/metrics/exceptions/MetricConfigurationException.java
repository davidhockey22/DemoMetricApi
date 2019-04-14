package com.ddouberley.metrics.exceptions;

public class MetricConfigurationException extends RuntimeException {
    public MetricConfigurationException(String reason) {
        super(reason);
    }
}
