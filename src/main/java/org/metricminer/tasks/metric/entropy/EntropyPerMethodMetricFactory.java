package org.metricminer.tasks.metric.entropy;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Entropy per method", result=EntropyPerMethodResult.class)
public class EntropyPerMethodMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new EntropyPerMethodMetricAdapter();
    }

}
