package org.metricminer.tasks.metric.testedmethods;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.codemetrics.testedmethods.TestedMethodFinderMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class TestedMethodFinderMetricAdapter implements Metric{

	private SourceCode source;
	private TestedMethodFinderMetric testedMethodFinderMetric;

	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		testedMethodFinderMetric = new TestedMethodFinderMetric();
		testedMethodFinderMetric.calculate(is);
	}

	public Map<String, Set<String>> getMethods() {
		return testedMethodFinderMetric.getMethods();
	}

    @Override
    public Collection<MetricResult> results() {
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for(Entry<String, Set<String>> testMethod : getMethods().entrySet()) {
            for(String productionMethod : testMethod.getValue()) {
                results.add(new TestedMethodFinderResult(source, testMethod.getKey(),
                        productionMethod));
            }
        }
        return results;
    }

    @Override
    public boolean matches(String name) {
        return name.endsWith("Test.java") || name.endsWith("Tests.java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return TestedMethodsFinderMetricFactory.class;
	}

}
