package org.metricminer.tasks.metric.entropy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.metricminer.codemetrics.entropy.EntropyPerMethodMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class EntropyPerMethodMetricAdapter implements Metric {
	private SourceCode source;
	private EntropyPerMethodMetric metric = new EntropyPerMethodMetric();
	@Override
	public Collection<MetricResult> results() {
		Collection<MetricResult> results = new ArrayList<MetricResult>();
		Map<String, Double> methods = metric.getMethods();
		for (Entry<String, Double> entry : methods.entrySet()) {
			String method = entry.getKey();
			Double entropy = entry.getValue();
			results.add(new EntropyPerMethodResult(source, method, entropy));
		}
		return results;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		metric = new EntropyPerMethodMetric();
		metric.calculate(is);
	}

	@Override
	public boolean matches(String name) {
		return metric.matches(name);
	}

	@Override
	public Class<?> getFactoryClass() {
		return EntropyPerMethodMetricFactory.class;
	}

}
