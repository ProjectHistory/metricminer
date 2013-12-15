package org.metricminer.tasks.metric.halstead;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Halstead metrics", result=HalsteadMetricsResult.class)
public class HalsteadMetricsFactory implements MetricFactory {

	@Override
	public Metric build() {
		return new HalsteadMetricsAdapter();
	}

}
