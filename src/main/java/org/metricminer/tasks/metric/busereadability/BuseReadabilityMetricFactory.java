package org.metricminer.tasks.metric.busereadability;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Buse readability", result=BuseReadabilityResult.class)
public class BuseReadabilityMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new BuseReadabilityMetricAdapter();
    }

}
