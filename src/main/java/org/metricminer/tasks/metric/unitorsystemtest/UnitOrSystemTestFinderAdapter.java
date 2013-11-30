package org.metricminer.tasks.metric.unitorsystemtest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.metricminer.codemetrics.unitorsystemtest.TestInfo;
import org.metricminer.codemetrics.unitorsystemtest.UnitOrSystemTestFinder;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class UnitOrSystemTestFinderAdapter implements Metric {

	private SourceCode source;
	private UnitOrSystemTestFinder unitOrSystemTestFinder;

	@Override
	public Collection<MetricResult> results() {
		List<MetricResult> result = new ArrayList<MetricResult>();
		for(Entry<String, TestInfo> test : getTests().entrySet()) {
			result.add(new UnitOrSystemTestResult(source, test.getValue().getName(), !test.getValue().isIntegration()));
		}
		return result;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.source = source;
		unitOrSystemTestFinder = new UnitOrSystemTestFinder();
		unitOrSystemTestFinder.calculate(is);
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith("Test.java") || name.endsWith("Tests.java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return UnitOrSystemTestFactory.class;
	}

	public Map<String, TestInfo> getTests() {
		return unitOrSystemTestFinder.getTests();
	}

}
