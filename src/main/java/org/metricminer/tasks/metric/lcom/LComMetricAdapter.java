package org.metricminer.tasks.metric.lcom;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.metricminer.codemetrics.lcom.LComMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


/**
 * Calculates LCOM
 * Formula: Formula in http://codenforcer.com/lcom.aspx
 * 
 * @author mauricioaniche
 *
 */
public class LComMetricAdapter implements Metric {

	private String name;
	private SourceCode source;
	private LComMetric lComMetric;

	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		lComMetric = new LComMetric();
	}
	
	public double lcomHS() {
		return lComMetric.lcomHS();
	}

	public double lcom() {
		return lComMetric.lcom();
	}
	
	public String getName() {
		return name;
	}

    @Override
    public Collection<MetricResult> results() {
        return Arrays.asList((MetricResult) new LComResult(source, lcom()));
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return LComMetricFactory.class;
	}
	
}
