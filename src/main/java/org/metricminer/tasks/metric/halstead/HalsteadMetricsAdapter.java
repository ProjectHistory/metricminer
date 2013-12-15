package org.metricminer.tasks.metric.halstead;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.metricminer.codemetrics.halstead.HalstaedMetrics;
import org.metricminer.codemetrics.halstead.HalsteadMethod;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class HalsteadMetricsAdapter implements Metric {

	private HalstaedMetrics halstaedMetrics;
	private SourceCode source;

	@Override
	public Collection<MetricResult> results() {
		List<HalsteadMethod> methods = halstaedMetrics.getMethods();
		List<MetricResult> results = new ArrayList<MetricResult>();
		for (HalsteadMethod m : methods) {
			String methodName = m.getMethodName();
			int operands = m.getOperands();
			int operators = m.getOperators();
			int uniqueOperands = m.getUniqueOperands();
			int uniqueOperators = m.getUniqueOperators();
			results.add(new HalsteadMetricsResult(source, methodName, operands, operators, uniqueOperands, uniqueOperators));
		}
		return results;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		halstaedMetrics = new HalstaedMetrics();
		halstaedMetrics.calculate(is);
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith(".java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return HalsteadMetricsFactory.class;
	}

}
