package org.metricminer.tasks.metric.cc;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.metricminer.codemetrics.cc.CCMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CCMetricAdapter implements Metric {

	private SourceCode sourceCode;
	private CCMetric ccMetric;

    public String header() {
        return "path;project;class;cc;average cc";
    }

    public Collection<MetricResult> results() {
        return Arrays.asList((MetricResult) new CCResult(sourceCode, cc(), avgCc()));
    }

    public void calculate(SourceCode sourceCode, InputStream is) {
		this.sourceCode = sourceCode;
		ccMetric = new CCMetric();
		ccMetric.calculate(is);
    }

    public double avgCc() {
        double avgCc = ccMetric.avgCc();
        if (Double.isNaN(avgCc))
            avgCc = -1.0;
        return avgCc;
    }

    public int cc() {
        return ccMetric.cc();
    }

    public int cc(String method) {
        return ccMetric.cc(method);
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return CCMetricFactory.class;
	}
}
