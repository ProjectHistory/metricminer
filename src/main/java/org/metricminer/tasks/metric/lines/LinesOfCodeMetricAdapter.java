package org.metricminer.tasks.metric.lines;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.metricminer.codemetrics.lines.LinesOfCodeMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class LinesOfCodeMetricAdapter implements Metric {


	private LinesOfCodeMetric linesOfCodeMetric;
	private SourceCode source;

	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		linesOfCodeMetric = new LinesOfCodeMetric();
		linesOfCodeMetric.calculate(is);
	}
	
	public Map<String, Integer> getMethodLines() {
		return linesOfCodeMetric.getMethodLines();
	}

    @Override
    public Collection<MetricResult> results() {
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for (Entry<String, Integer> entry : linesOfCodeMetric.getMethodLines().entrySet()) {
            results.add(new LinesOfCodeResult(source, entry.getKey(), entry.getValue()));
        }
        return results;
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return LinesOfCodeMetricFactory.class;
	}

}
