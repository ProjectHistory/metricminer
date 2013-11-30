package org.metricminer.tasks.metric.staticcounter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.metricminer.codemetrics.staticcounter.StaticCounterMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class StaticCounterMetricAdapter implements Metric {

	private SourceCode source;
	private StaticCounterMetric staticCounterMetric;

	@Override
	public Collection<MetricResult> results() {
		Collection<MetricResult> results = new ArrayList<MetricResult>();

		int numberOfStaticAttributes = staticCounterMetric.getNumberOfStaticAttributes();
		int numberOfStaticMethods = staticCounterMetric.getNumberOfStaticMethods();
		Set<String> staticAttributesName = staticCounterMetric.getStaticAttributesName();
		Set<String> staticMethodsName = staticCounterMetric.getStaticMethodsName();
		
		if (numberOfStaticAttributes > 0 || numberOfStaticMethods > 0) {
			String attributesName = StringUtils.join(staticAttributesName.toArray(), ",");
			String methodsName = StringUtils.join(staticMethodsName.toArray(), ",");
			
			MetricResult staticAttributesResult = new StaticCounterResult(
					source,
					attributesName,
					numberOfStaticAttributes,
					methodsName,
					numberOfStaticMethods);
			
			results.add(staticAttributesResult);
		}

		return results;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		staticCounterMetric = new StaticCounterMetric();
		staticCounterMetric.calculate(is);
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith(".java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return StaticCounterMetricFactory.class;
	}

	public int getNumberOfStaticAttributes() {
		return staticCounterMetric.getNumberOfStaticAttributes();
	}

	public Set<String> getStaticAttributesName() {
		return staticCounterMetric.getStaticAttributesName();
	}

	public int getNumberOfStaticMethods() {
		return staticCounterMetric.getNumberOfStaticMethods();
	}

	public Set<String> getStaticMethodsName() {
		return staticCounterMetric.getStaticMethodsName();
	}

}