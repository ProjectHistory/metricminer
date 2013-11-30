package org.metricminer.tasks.metric.invocation;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.SourceCode;
import org.mockito.Mockito;

public class MethodsInvocationMetricTests {

	private MethodsInvocationMetricAdapter metric;
	private SourceCode source;

	@Before
	public void setUp() {
		this.metric = new MethodsInvocationMetricAdapter();
		this.source = Mockito.mock(SourceCode.class);
	}

	@Test
	public void shouldCountMethodsInvocationPerMethod() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"public void method() {"+
								"a();"+
								"b();"+
								"}"
								)));
	
		assertEquals(2, metric.getMethods().get("method/0").size());
	}
	
	@Test
	public void shouldCountMethodsInvocationPerMethodRegardlessOfVisibility() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"public void method1() {"+
								"a();"+
								"b();"+
								"}"+
								"private void method2() {"+
								"a();"+
								"b();"+
								"}"+
								"protected void method3() {"+
								"a();"+
								"b();"+
								"}"+
								"void method4() {"+
								"a();"+
								"b();"+
								"}"
								)));
	
		assertEquals(2, metric.getMethods().get("method1/0").size());
		assertEquals(2, metric.getMethods().get("method2/0").size());
		assertEquals(2, metric.getMethods().get("method3/0").size());
		assertEquals(2, metric.getMethods().get("method4/0").size());
	}
	
	@Test
	public void shouldNotCountRepeatedInvocations() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"public void method() {"+
								"x++;"+
								"a();"+
								"a();"+
								"}"
								)));
	
		assertEquals(1, metric.getMethods().get("method/0").size());
	}
	
	@Test
	public void shouldIgnoreMethodInvokedOutsideOfAMethod() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private int x = someMethod();"+
								"public void method() {"+
								"x++;"+
								"a();"+
								"}"
								)));
		
		assertEquals(1, metric.getMethods().get("method/0").size());
	}
	
	@Test
	public void shouldNotGetSequencedInvocations() {
		metric.calculate(source,
				toInputStream(
				classDeclaration(
				"@Test"+
				"public void shouldInvokeR() throws Exception {"+
				"	StatisticalTest test = new StatisticalTest(\"wilcoxon\", wilcoxon(), new User());"+
				"	QueryResult q1 = new QueryResult(path + \"/q1\", null);"+
				"	QueryResult q2 = new QueryResult(path + \"/q2\", null);"+
				"	"+
				"	StatisticalTestResult result = r.execute(test, q1, q2, null, \"test\");"+
				"	"+
				"	assertTrue(result.getOutput().contains(\"Wilcoxon signed rank test\"));"+
				"	assertTrue(result.getOutput().contains(\"p-value = 0.25\"));"+
				"}"
				)));
		assertTrue(metric.getMethodsNames().get("shouldInvokeR/0").contains("getOutput/0"));
		assertFalse(metric.getMethodsNames().get("shouldInvokeR/0").contains("contains"));
	}
}
