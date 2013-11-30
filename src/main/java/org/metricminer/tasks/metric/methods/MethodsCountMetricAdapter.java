package org.metricminer.tasks.metric.methods;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.metricminer.codemetrics.common.Method;
import org.metricminer.codemetrics.methods.MethodsCountMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class MethodsCountMetricAdapter implements Metric {

	private SourceCode source;
	private MethodsCountMetric methodsCountMetric;

	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		this.methodsCountMetric = new MethodsCountMetric();
		this.methodsCountMetric.calculate(is);
	}

	public String getName() {
		return this.methodsCountMetric.getName();
	}

	public List<Method> getMethods() {
		return this.methodsCountMetric.getMethods();
	}

	public List<String> getAttributes() {
		return this.methodsCountMetric.getAttributes();
	}

	public List<Method> getPublicMethods() {
		return this.methodsCountMetric.getPublicMethods();
	}

	public List<Method> getPrivateMethods() {
		return this.methodsCountMetric.getPrivateMethods();
	}

	public List<Method> getProtectedMethods() {
		return this.methodsCountMetric.getProtectedMethods();
	}

	public List<Method> getDefaultMethods() {
		return this.methodsCountMetric.getDefaultMethods();
	}

	public List<Method> getConstructorMethods() {
		return this.methodsCountMetric.getConstructorMethods();
	}

	public List<String> getPublicAttributes() {
		return this.methodsCountMetric.getPublicAttributes();
	}

	public List<String> getPrivateAttributes() {
		return this.methodsCountMetric.getPrivateAttributes();
	}

	public List<String> getProtectedAttributes() {
		return this.methodsCountMetric.getProtectedAttributes();
	}

	public List<String> getDefaultAttributes() {
		return this.methodsCountMetric.getDefaultAttributes();
	}

	@Override
	public Collection<MetricResult> results() {
		return Arrays.asList((MetricResult) new MethodsCountResult(source,
				getPrivateAttributes().size(), getPublicMethods().size(),
				getProtectedMethods().size(), getDefaultMethods().size(),
				getConstructorMethods().size(), getPrivateAttributes().size(),
				getPublicAttributes().size(), getProtectedAttributes().size(),
				getDefaultAttributes().size()));
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith(".java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return MethodsCountMetricFactory.class;
	}

}
