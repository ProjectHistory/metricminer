package org.metricminer.tasks.metric.invocation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.codemetrics.invocation.MethodInvocation;
import org.metricminer.codemetrics.invocation.NoInlineMethodInvocationMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class MethodsInvocationMetricAdapter implements Metric {

	private SourceCode source;
	private NoInlineMethodInvocationMetric methodsInvocationMetric;
	
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		this.methodsInvocationMetric = new NoInlineMethodInvocationMetric();
		this.methodsInvocationMetric.calculate(is);
	}
	
	public Map<String, Set<MethodInvocation>> getMethods() {
		return methodsInvocationMetric.getInvocations();
	}

    @Override
    public Collection<MetricResult> results() {
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for (Entry<String, Set<MethodInvocation>> e : getMethods().entrySet()) {
            results.add(new MethodsInvocationResult(source, e.getValue().size(), e.getKey()));
        }
        return results;
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return MethodsInvocationMetricFactory.class;
	}

	public Map<String, Set<String>> getMethodsNames() {
		Map<String, Set<String>> invocationsNames = new HashMap<String, Set<String>>();
		Map<String, Set<MethodInvocation>> invocations = methodsInvocationMetric.getInvocations();
		Set<Entry<String, Set<MethodInvocation>>> entries = invocations.entrySet();
		for (Entry<String, Set<MethodInvocation>> entry : entries) {
			Set<MethodInvocation> value = entry.getValue();
			Set<String> calls = new HashSet<String>();
			for (MethodInvocation methodInvocation : value) {
				calls.add(methodInvocation.getInvokedMethod());
			}
			invocationsNames.put(entry.getKey(), calls);
		}
		return invocationsNames;
	}

}
