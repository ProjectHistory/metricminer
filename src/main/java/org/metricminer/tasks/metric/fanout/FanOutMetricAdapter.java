package org.metricminer.tasks.metric.fanout;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.metricminer.codemetrics.fanout.FanOutMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class FanOutMetricAdapter implements Metric {

	private SourceCode source;
	private FanOutMetric fanOutMetric;

	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		this.fanOutMetric = new FanOutMetric();
		this.fanOutMetric.calculate(is);
	}

	public int fanOut() {
		return this.fanOutMetric.fanOut();
	}

    @Override
    public Collection<MetricResult> results() {
        return Arrays.asList((MetricResult) new FanOutResult(source, fanOut()));
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return FanOutMetricFactory.class;
	}

    
}
