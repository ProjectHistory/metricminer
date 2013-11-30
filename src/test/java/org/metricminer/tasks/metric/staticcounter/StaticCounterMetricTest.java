package org.metricminer.tasks.metric.staticcounter;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.SourceCode;
import org.mockito.Mockito;

public class StaticCounterMetricTest {

	private StaticCounterMetricAdapter metric;
	private SourceCode source;
	
	@Before
	public void setUp() {
		this.source = Mockito.mock(SourceCode.class);
		metric = new StaticCounterMetricAdapter();
	}

	@Test
	public void shouldCountStaticAttributes() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private static int attr1;\r\n" +
								"private int attr2;\r\n" +
								"public static int attr3;\r\n" +
								"public int attr4;\r\n" +
								"public static void method() {}\r\n"
								)));
		
		assertEquals(2, metric.getNumberOfStaticAttributes());
		assertTrue(metric.getStaticAttributesName().containsAll(Arrays.asList("attr1", "attr3")));
	}
	
	@Test
	public void shouldReturnZeroWhenHasNotStaticAttributes() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private int x;" +
								"public static void method() {}\r\n"
								)));
		
		assertEquals(0, metric.getNumberOfStaticAttributes());
		assertTrue(metric.getStaticAttributesName().isEmpty());
	}
	
	@Test
	public void shouldCountStaticMethods() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private int x;" +
								"public static void method1() {}\r\n" +
								"private static void method2() {}\r\n" +
								"public void method3(){}\r\n"
								)));
		
		assertEquals(2, metric.getNumberOfStaticMethods());
		assertTrue(metric.getStaticMethodsName().containsAll(Arrays.asList("method1", "method2")));
	}
	
	@Test
	public void shouldReturnZeroWhenHasNotStaticMethods() {
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private int x;" +
								"public void method1() {}\r\n" +
								"privatevoid method2() {}\r\n"
								)));
		
		assertEquals(0, metric.getNumberOfStaticMethods());
		assertTrue(metric.getStaticMethodsName().isEmpty());
	}
	
	@Test
	public void shouldReturnOnlyAttributeNameEvenIfAttributeIsInitialized(){
		metric.calculate(source,
				toInputStream(
						classDeclaration(
								"private static int attr1 = 0;\r\n" +
								"private int attr2;\r\n" +
								"public static int attr3;\r\n" +
								"public int attr4;\r\n" +
								"public static void method() {}\r\n"
								)));
		assertEquals(2, metric.getNumberOfStaticAttributes());
		System.out.println(metric.getStaticAttributesName());
		assertTrue(metric.getStaticAttributesName().contains("attr1"));
		assertTrue(metric.getStaticAttributesName().contains("attr3"));
	}
}