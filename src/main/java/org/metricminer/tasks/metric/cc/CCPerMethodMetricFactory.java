package org.metricminer.tasks.metric.cc;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Cyclomatic Complexity per method", result=CCPerMethodResult.class)
public class CCPerMethodMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new CCPerMethodMetricAdapter();
    }

}
