package org.metricminer.tasks.metric.unitorsystemtest;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

//@MetricComponent(name="Unit or System Test Identifier")
public class UnitOrSystemTestFactory implements MetricFactory {

	@Override
	public Metric build() {
		return new UnitOrSystemTestFinderAdapter();
	}

}
