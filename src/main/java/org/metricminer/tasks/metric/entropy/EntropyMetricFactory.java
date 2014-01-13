package org.metricminer.tasks.metric.entropy;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Entropy", result=EntropyResult.class)
public class EntropyMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new EntropyMetricAdapter();
    }

}
