package org.example.calc.requests;

import java.util.List;
import java.util.Map;


public class RadarChartRequest {
    private String action;
    private List<Double> metrics;
    private List<String> metricsName;
    private List<Double> minMetrics;
    private List<Integer> maxFuncs;
    private Map<String, Map<String, Double>> disturbances;
    private Map<String, Map<String, Double>> additionalRows;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Double> metrics) {
        this.metrics = metrics;
    }

    public List<String> getMetricsName() {
        return metricsName;
    }

    public void setMetricsName(List<String> metricsName) {
        this.metricsName = metricsName;
    }

    public List<Double> getMinMetrics() {
        return minMetrics;
    }

    public void setMinMetrics(List<Double> minMetrics) {
        this.minMetrics = minMetrics;
    }

    public List<Integer> getMaxFuncs() {
        return maxFuncs;
    }

    public void setMaxFuncs(List<Integer> maxFuncs) {
        this.maxFuncs = maxFuncs;
    }

    public Map<String, Map<String, Double>> getDisturbances() {
        return disturbances;
    }

    public void setDisturbances(Map<String, Map<String, Double>> disturbances) {
        this.disturbances = disturbances;
    }

    public Map<String, Map<String, Double>> getAdditionalRows() {
        return additionalRows;
    }

    public void setAdditionalRows(Map<String, Map<String, Double>> additionalRows) {
        this.additionalRows = additionalRows;
    }
}
