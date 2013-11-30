package org.metricminer.tasks.metric.staticcounter;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Quantity of Static Attributes and Methods", result=StaticCounterResult.class)
public class StaticCounterMetricFactory implements MetricFactory {

	@Override
	public Metric build() {
		return new StaticCounterMetricAdapter();
	}

}
