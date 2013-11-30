package org.metricminer.tasks.metric.changedtests;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.metricminer.codemetrics.changedtests.NewOrChangedTestUnit;
import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

// TODO: Understand removed lines as a changed test
public class NewOrChangedTestUnitAdapter implements Metric {

	private SourceCode source;
	private NewOrChangedTestUnit newOrChangedTestUnit;

	@Override
	public Collection<MetricResult> results() {
		List<MetricResult> result = new ArrayList<MetricResult>();

		for(String test : this.newOrChangedTestUnit.newTests()) {
			result.add(new NewOrChangedTestUnitResult(source, test, UnitTestChangeStatus.NEW));
		}
		for(String test : this.newOrChangedTestUnit.modifiedTests()) {
			result.add(new NewOrChangedTestUnitResult(source, test, UnitTestChangeStatus.CHANGED));
		}
			
		return result;
	}

	@Override
	public void calculate(SourceCode source, InputStream is) {
		this.newOrChangedTestUnit = new NewOrChangedTestUnit();
		this.source = source;
		this.newOrChangedTestUnit.calculate(is, source.getDiff());
	}

	@Override
	public boolean matches(String name) {
		return name.endsWith("Test.java") || name.endsWith("Tests.java");
	}

	@Override
	public Class<?> getFactoryClass() {
		return NewOrChangedTestUnitFactory.class;
	}

	public Set<String> newTests() {
		return newOrChangedTestUnit.newTests();
	}

	public Set<String> modifiedTests() {
		return newOrChangedTestUnit.modifiedTests();
	}

}
