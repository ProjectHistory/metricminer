package org.metricminer.tasks.metric.halstead;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class HalsteadMetricsFactory implements MetricFactory {

	@Override
	public Metric build() {
		return new HalsteadMetricsAdapter();
	}

}
