package org.metricminer.tasks.metric.entropy;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.metricminer.codemetrics.entropy.EntropyMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class EntropyMetricAdapter implements Metric {
	
	private EntropyMetric metric;
	private SourceCode source;

	@Override
	public Collection<MetricResult> results() {
		List<MetricResult> results = Arrays.<MetricResult>asList(new EntropyResult(source, metric.getEntropy()));
		return results;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		metric = new EntropyMetric();
		metric.calculate(is);
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith(".java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return EntropyMetricFactory.class;
	}

}
