package org.metricminer.tasks.metric.busereadability;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.codemetrics.busereadability.BuseReadabilityMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class BuseReadabilityMetricAdapter implements Metric {
	
	private BuseReadabilityMetric metric;
	private SourceCode source;

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		metric = new BuseReadabilityMetric();
		metric.calculate(is);
	}

	@Override
	public Collection<MetricResult> results() {
		Set<Entry<String, Double>> entries = metric.getResults().entrySet();
		ArrayList<MetricResult> results = new ArrayList<MetricResult>();
		for (Entry<String, Double> entry : entries) {
			results.add(new BuseReadabilityResult(entry.getKey(), entry.getValue(), source));
		}
		return results;
	}


	@Override
	public boolean matches(String name) {
		return name.endsWith(".java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return BuseReadabilityMetricFactory.class;
	}

}
