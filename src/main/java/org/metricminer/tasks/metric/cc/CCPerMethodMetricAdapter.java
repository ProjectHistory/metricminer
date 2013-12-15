package org.metricminer.tasks.metric.cc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.codemetrics.cc.CCMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class CCPerMethodMetricAdapter implements Metric {

	private SourceCode sourceCode;
	private CCMetric ccMetric;

    public String header() {
        return "path;project;class;method;cc";
    }

    public void calculate(SourceCode sourceCode, InputStream is) {
        this.sourceCode = sourceCode;
        ccMetric = new CCMetric();
		ccMetric.calculate(is);
    }

    @Override
    public Collection<MetricResult> results() {
    	Set<Entry<String, Integer>> ccPerMethods = ccMetric.ccPerMethod();
    	Collection<MetricResult> list = new ArrayList<MetricResult>();
    	for (Entry<String, Integer> method : ccPerMethods) {
    		int cc = method.getValue();
    		String methodName = method.getKey();
    		list.add(new CCPerMethodResult(sourceCode, methodName, cc));
		}
        return list;
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return CCPerMethodMetricFactory.class;
	}


}
