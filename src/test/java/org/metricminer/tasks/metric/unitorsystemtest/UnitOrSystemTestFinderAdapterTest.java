package org.metricminer.tasks.metric.unitorsystemtest;

import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.SourceCode;
import org.mockito.Mockito;

public class UnitOrSystemTestFinderAdapterTest {
	private UnitOrSystemTestFinderAdapter metric;
	private SourceCode source;

	@Before
	public void setUp() {
		this.metric = new UnitOrSystemTestFinderAdapter();
		this.source = Mockito.mock(SourceCode.class);
	}
	
	@Test
	public void pequeninho() {
		metric.calculate(source,
				toInputStream(
					"import a.b.Dep1;" +
					"import a.b.Dep2;" +
					"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
							"Dep1 x = new Dep1();" +
							"Dep2 y = new Dep2();" +
							"Bla obj = new Bla(x, y);" +
							"obj.method1();" +
							"assertEquals(obj.isAbc());" +
						"}" +
					"}"
								));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	@Test
	public void shouldTellThatIsAnIntegrationTestIfConstructorReceivesConcreteStuffWithCtor() {
		metric.calculate(source,
				toInputStream(
					"import a.b.Dep1;" +
					"import a.b.Dep2;" +
					"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
							"Dep1 x = new Dep1(new TT());" +
							"Dep2 y = new Dep2(new KK());" +
							"Bla obj = new Bla(x, y);" +
							"obj.method1();" +
							"assertEquals(obj.isAbc());" +
						"}" +
					"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAUnitTestIfConstructorReceivesConcreteStuffFromSamePackage() {
		metric.calculate(source,
				toInputStream(
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1();" +
								"Dep2 y = new Dep2();" +
								"Bla obj = new Bla(x, y);" +
								"obj.method1();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAUnitTestIfConstructorReceivesConcreteStuffWithCtorFromSamePackage() {
		metric.calculate(source,
				toInputStream(
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1(new TT());" +
								"Dep2 y = new Dep2(new ZZ());" +
								"Bla obj = new Bla(x, y);" +
								"obj.method1();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAUnitTestIfConstructorReceivesConcreteAndPrimitiveStuffFromSamePackage() {
		metric.calculate(source,
				toInputStream(
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1();" +
								"Bla obj = new Bla(x, 1, 3.5, \"bla\");" +
								"obj.method1();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	@Test
	public void shouldTellThatIsAnIntegrationUnitTestIfConstructorReceivesConcreteAndPrimitiveStuff() {
		metric.calculate(source,
				toInputStream(
						"import a.b.Dep1;" +
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1();" +
								"Bla obj = new Bla(x, 1, 3.5, \"bla\");" +
								"obj.method1();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldUnderstandTwoTestsInARow() {
		metric.calculate(source,
				toInputStream(
					"import a.b.Dep1;" +
					"class BlaTest {" +
					
						"@Test " +
						"public void aSimpleTest1() {" +
							"Dep1 x = new Dep1();" +
							"Bla obj = new Bla(x, 1, 3.5, \"bla\");" +
							"obj.method1();" +
							"assertEquals(obj.isAbc());" +
						"}" +
					
						"@Test " +
						"public void aSimpleTest2() {" +
							"Dep2 x = new Dep2();" +
							"Bla obj = new Bla(x, 1, 3.5, \"bla\");" +
							"obj.method1();" +
							"assertEquals(obj.isAbc());" +
						"}" +
						
					"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest1").isIntegration());
		assertFalse(metric.getTests().get("aSimpleTest2").isIntegration());
		
	}
	
	
	@Test
	public void shouldTellThatIsAnUnitTestIfMethodReceivesConcreteStuffFromSamePackage() {
		metric.calculate(source,
				toInputStream(
					"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
							"Dep1 x = new Dep1();" +
							"Dep2 y = new Dep2();" +
							"Bla obj = new Bla(x);" +
							"obj.method1(y);" +
							"assertEquals(obj.isAbc());" +
						"}" +
					"}"
								));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAnIntegrationTestIfMethodReceivesConcreteStuff() {
		metric.calculate(source,
				toInputStream(
						"import a.b.Dep1;" +
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1();" +
								"Dep2 y = new Dep2();" +
								"Bla obj = new Bla(y);" +
								"obj.method1(x);" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAnIntegrationTestIfConcreteMethodIsInvoked() {
		metric.calculate(source,
				toInputStream(
						"import a.b.Dep1;" +
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Dep1 x = new Dep1();" +
								"Dep2 y = new Dep2();" +
								"Bla obj = new Bla(y);" +
								"x.doMagic();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAnIntegrationTestIfConcreteMethodIsInvokedWithoutInstance() {
		metric.calculate(source,
				toInputStream(
						"import a.b.Dep1;" +
						"class BlaTest {" +
							"@Test " +
							"public void aSimpleTest() {" +
								"Bla obj = new Bla();" +
								"int result = new Dep1().doMagic();" +
								"new Dep1().doMagic2();" +
								"assertEquals(obj.isAbc());" +
							"}" +
						"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsAUnitTestIfConcreteMethodFromSamePackageIsInvoked() {
		metric.calculate(source,
				toInputStream(
					"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
							"Dep1 newX = new Dep1();" +
							"Bla obj = new Bla();" +
							"newX.doMagic();" +
							"obj.doOtherMagic();" +
							"assertEquals(obj.isAbc());" +
						"}" +
					"}"
						));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsUnitTestIfMethodFromSamePackageIsInvokedCompletlelyInline() {
		metric.calculate(source,
				toInputStream(
					"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
							"assertEquals(new Dep1().doMagic());" +
						"}" +
					"}"
						));
		
		assertFalse(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void shouldTellThatIsIntegrationTestIfMethodFromDifferentPackageIsInvokedCompletlelyInline() {
		metric.calculate(source,
				toInputStream(
						"import a.b.Dep1;" +
						"class BlaTest {" +
								"@Test " +
								"public void aSimpleTest() {" +
								"assertEquals(new Dep1().doMagic());" +
								"}" +
								"}"
						));
		
		assertTrue(metric.getTests().get("aSimpleTest").isIntegration());
		
	}
	
	@Test
	public void someRealExampleFromJUnit() {

		metric.calculate(source, toInputStream(
				"package org.junit.tests.junit3compatibility;"+
				""+
				"import static org.junit.Assert.assertNull;"+
				""+
				"import org.junit.Test;"+
				"import org.junit.internal.builders.SuiteMethodBuilder;"+
				""+
				"public class ClassRequestTest {"+
				"    public static class PrivateSuiteMethod {"+
				"        static junit.framework.Test suite() {"+
				"            return null;"+
				"        }"+
				"    }"+
				""+
				"    @Test"+
				"    public void noSuiteMethodIfMethodPrivate() throws Throwable {"+
				"        assertNull(new SuiteMethodBuilder()"+
				"                .runnerForClass(PrivateSuiteMethod.class));"+
				"    }"+
				"}"));
		
		assertTrue(metric.getTests().get("noSuiteMethodIfMethodPrivate").isIntegration());
		
	}
	
	
	// levar pro exemplo menor 
	
	@Test
	public void shouldIgnoreAMethodThatIsNotATest() {
		// JUnit: TimeoutTest.java
		
		String c = 
				"package org.junit.tests.running.methods;\r\n"+
				"\r\n"+
				"import static org.hamcrest.CoreMatchers.containsString;\r\n"+
				"import static org.hamcrest.CoreMatchers.is;\r\n"+
				"import static org.junit.Assert.assertEquals;\r\n"+
				"import static org.junit.Assert.assertThat;\r\n"+
				"import static org.junit.Assert.assertTrue;\r\n"+
				"import static org.junit.Assert.fail;\r\n"+
				"\r\n"+
				"import java.io.PrintWriter;\r\n"+
				"import java.io.StringWriter;\r\n"+
				"import java.io.Writer;\r\n"+
				"\r\n"+
				"import junit.framework.JUnit4TestAdapter;\r\n"+
				"import junit.framework.TestResult;\r\n"+
				"import org.junit.After;\r\n"+
				"import org.junit.Ignore;\r\n"+
				"import org.junit.Test;\r\n"+
				"import org.junit.runner.JUnitCore;\r\n"+
				"import org.junit.runner.Result;\r\n"+
				"\r\n"+
				"public class TimeoutTest {\r\n"+
				"\r\n"+
				"    static public class FailureWithTimeoutTest {\r\n"+
				"        @Test(timeout = 1000)\r\n"+
				"        public void failure() {\r\n"+
				"            fail();\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void failureWithTimeout() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(FailureWithTimeoutTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(1, result.getFailureCount());\r\n"+
				"        assertEquals(AssertionError.class, result.getFailures().get(0).getException().getClass());\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    static public class FailureWithTimeoutRunTimeExceptionTest {\r\n"+
				"        @Test(timeout = 1000)\r\n"+
				"        public void failure() {\r\n"+
				"            throw new NullPointerException();\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void failureWithTimeoutRunTimeException() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(FailureWithTimeoutRunTimeExceptionTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(1, result.getFailureCount());\r\n"+
				"        assertEquals(NullPointerException.class, result.getFailures().get(0).getException().getClass());\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    static public class SuccessWithTimeoutTest {\r\n"+
				"        @Test(timeout = 1000)\r\n"+
				"        public void success() {\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void successWithTimeout() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(SuccessWithTimeoutTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(0, result.getFailureCount());\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    static public class TimeoutFailureTest {\r\n"+
				"        @Test(timeout = 100)\r\n"+
				"        public void success() throws InterruptedException {\r\n"+
				"            Thread.sleep(40000);\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Ignore(\"was breaking gump\")\r\n"+
				"    @Test\r\n"+
				"    public void timeoutFailure() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(TimeoutFailureTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(1, result.getFailureCount());\r\n"+
				"        assertEquals(InterruptedException.class, result.getFailures().get(0).getException().getClass());\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    static public class InfiniteLoopTest {\r\n"+
				"        @Test(timeout = 100)\r\n"+
				"        public void failure() {\r\n"+
				"            infiniteLoop();\r\n"+
				"        }\r\n"+
				"\r\n"+
				"        private void infiniteLoop() {\r\n"+
				"            for (; ; ) {\r\n"+
				"                try {\r\n"+
				"                    Thread.sleep(10);\r\n"+
				"                } catch (InterruptedException e) {\r\n"+
				"                }\r\n"+
				"            }\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void infiniteLoop() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(InfiniteLoopTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(1, result.getFailureCount());\r\n"+
				"        Throwable exception = result.getFailures().get(0).getException();\r\n"+
				"        assertTrue(exception.getMessage().contains(\"test timed out after 100 milliseconds\"));\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    static public class ImpatientLoopTest {\r\n"+
				"        @Test(timeout = 1)\r\n"+
				"        public void failure() {\r\n"+
				"            infiniteLoop();\r\n"+
				"        }\r\n"+
				"\r\n"+
				"        private void infiniteLoop() {\r\n"+
				"            for (; ; ) ;\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Ignore(\"This breaks sporadically with time differences just slightly more than 200ms\")\r\n"+
				"    @Test\r\n"+
				"    public void infiniteLoopRunsForApproximatelyLengthOfTimeout() throws Exception {\r\n"+
				"        // \"prime the pump\": running these beforehand makes the runtimes more predictable\r\n"+
				"        //                   (because of class loading?)\r\n"+
				"        JUnitCore.runClasses(InfiniteLoopTest.class, ImpatientLoopTest.class);\r\n"+
				"        long longTime = runAndTime(InfiniteLoopTest.class);\r\n"+
				"        long shortTime = runAndTime(ImpatientLoopTest.class);\r\n"+
				"        long difference = longTime - shortTime;\r\n"+
				"        assertTrue(String.format(\"Difference was %sms\", difference), difference < 200);\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    private long runAndTime(Class<?> clazz) {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        long startTime = System.currentTimeMillis();\r\n"+
				"        core.run(clazz);\r\n"+
				"        long totalTime = System.currentTimeMillis() - startTime;\r\n"+
				"        return totalTime;\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void stalledThreadAppearsInStackTrace() throws Exception {\r\n"+
				"        JUnitCore core = new JUnitCore();\r\n"+
				"        Result result = core.run(InfiniteLoopTest.class);\r\n"+
				"        assertEquals(1, result.getRunCount());\r\n"+
				"        assertEquals(1, result.getFailureCount());\r\n"+
				"        Throwable exception = result.getFailures().get(0).getException();\r\n"+
				"        Writer buffer = new StringWriter();\r\n"+
				"        PrintWriter writer = new PrintWriter(buffer);\r\n"+
				"        exception.printStackTrace(writer);\r\n"+
				"        assertThat(buffer.toString(), containsString(\"infiniteLoop\")); // Make sure we have the stalled frame on the stack somewhere\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void compatibility() {\r\n"+
				"        TestResult result = new TestResult();\r\n"+
				"        new JUnit4TestAdapter(InfiniteLoopTest.class).run(result);\r\n"+
				"        assertEquals(1, result.errorCount());\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    public static class WillTimeOut {\r\n"+
				"        static boolean afterWasCalled = false;\r\n"+
				"\r\n"+
				"        @Test(timeout = 1)\r\n"+
				"        public void test() {\r\n"+
				"            for (; ; ) {\r\n"+
				"                try {\r\n"+
				"                    Thread.sleep(10000);\r\n"+
				"                } catch (InterruptedException e) {\r\n"+
				"                    // ok, tests are over\r\n"+
				"                }\r\n"+
				"            }\r\n"+
				"        }\r\n"+
				"\r\n"+
				"        @After\r\n"+
				"        public void after() {\r\n"+
				"            afterWasCalled = true;\r\n"+
				"        }\r\n"+
				"    }\r\n"+
				"\r\n"+
				"    @Test\r\n"+
				"    public void makeSureAfterIsCalledAfterATimeout() {\r\n"+
				"        JUnitCore.runClasses(WillTimeOut.class);\r\n"+
				"        assertThat(WillTimeOut.afterWasCalled, is(true));\r\n"+
				"    }\r\n"+
				"}";
		
		metric.calculate(source, toInputStream(c));
	}

	@Test
	public void shouldUnderstandAttributesInInnerClassesWithNoTests() {
		String c = 
				"package org.junit.tests.running.classes;\n"+
				"\n"+
				"import static org.junit.Assert.assertEquals;\n"+
				"import static org.junit.Assert.assertThat;\n"+
				"import static org.junit.Assert.fail;\n"+
				"import static org.junit.experimental.results.PrintableResult.testResult;\n"+
				"import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;\n"+
				"import static org.junit.runner.Description.createSuiteDescription;\n"+
				"import static org.junit.runner.Description.createTestDescription;\n"+
				"\n"+
				"import java.util.Collections;\n"+
				"import java.util.HashMap;\n"+
				"import java.util.List;\n"+
				"import java.util.Map;\n"+
				"\n"+
				"import org.junit.Test;\n"+
				"import org.junit.runner.Description;\n"+
				"import org.junit.runner.JUnitCore;\n"+
				"import org.junit.runner.Request;\n"+
				"import org.junit.runner.Result;\n"+
				"import org.junit.runner.RunWith;\n"+
				"import org.junit.runner.Runner;\n"+
				"import org.junit.runner.manipulation.Filter;\n"+
				"import org.junit.runner.manipulation.NoTestsRemainException;\n"+
				"import org.junit.runners.Suite;\n"+
				"import org.junit.runners.Suite.SuiteClasses;\n"+
				"import org.junit.runners.model.InitializationError;\n"+
				"import org.junit.runners.model.RunnerBuilder;\n"+
				"\n"+
				"public class ParentRunnerFilteringTest {\n"+
				"    private static Filter notThisMethodName(final String methodName) {\n"+
				"        return new Filter() {\n"+
				"            @Override\n"+
				"            public boolean shouldRun(Description description) {\n"+
				"                return description.getMethodName() == null\n"+
				"                        || !description.getMethodName().equals(methodName);\n"+
				"            }\n"+
				"\n"+
				"            @Override\n"+
				"            public String describe() {\n"+
				"                return \"don't run method name: \" + methodName;\n"+
				"            }\n"+
				"        };\n"+
				"    }\n"+
				"\n"+
				"    private static class CountingFilter extends Filter {\n"+
				"        private final Map<Description, Integer> countMap = new HashMap<Description, Integer>();\n"+
				"\n"+
				"        @Override\n"+
				"        public boolean shouldRun(Description description) {\n"+
				"            Integer count = countMap.get(description);\n"+
				"            if (count == null) {\n"+
				"                countMap.put(description, 1);\n"+
				"            } else {\n"+
				"                countMap.put(description, count + 1);\n"+
				"            }\n"+
				"            return true;\n"+
				"        }\n"+
				"\n"+
				"        @Override\n"+
				"        public String describe() {\n"+
				"            return \"filter counter\";\n"+
				"        }\n"+
				"\n"+
				"        public int getCount(final Description desc) {\n"+
				"            if (!countMap.containsKey(desc)) {\n"+
				"                throw new IllegalArgumentException(\"Looking for \" + desc\n"+
				"                        + \", but only contains: \" + countMap.keySet());\n"+
				"            }\n"+
				"            return countMap.get(desc);\n"+
				"        }\n"+
				"    }\n"+
				"\n"+
				"    public static class ExampleTest {\n"+
				"        @Test\n"+
				"        public void test1() throws Exception {\n"+
				"            // passes\n"+
				"        }\n"+
				"    }\n"+
				"\n"+
				"    @RunWith(Suite.class)\n"+
				"    @SuiteClasses({ExampleTest.class})\n"+
				"    public static class ExampleSuite {\n"+
				"    }\n"+
				"\n"+
				"    @Test\n"+
				"    public void testSuiteFiltering() throws Exception {\n"+
				"        Runner runner = Request.aClass(ExampleSuite.class).getRunner();\n"+
				"        Filter filter = notThisMethodName(\"test1\");\n"+
				"        try {\n"+
				"            filter.apply(runner);\n"+
				"        } catch (NoTestsRemainException e) {\n"+
				"            return;\n"+
				"        }\n"+
				"        fail(\"Expected 'NoTestsRemainException' due to complete filtering\");\n"+
				"    }\n"+
				"\n"+
				"    public static class SuiteWithUnmodifyableChildList extends Suite {\n"+
				"\n"+
				"        public SuiteWithUnmodifyableChildList(\n"+
				"                Class<?> klass, RunnerBuilder builder)\n"+
				"                throws InitializationError {\n"+
				"            super(klass, builder);\n"+
				"        }\n"+
				"\n"+
				"        @Override\n"+
				"        protected List<Runner> getChildren() {\n"+
				"            return Collections.unmodifiableList(super.getChildren());\n"+
				"        }\n"+
				"    }\n"+
				"\n"+
				"    @RunWith(SuiteWithUnmodifyableChildList.class)\n"+
				"    @SuiteClasses({ExampleTest.class})\n"+
				"    public static class ExampleSuiteWithUnmodifyableChildList {\n"+
				"    }\n"+
				"\n"+
				"    @Test\n"+
				"    public void testSuiteFilteringWithUnmodifyableChildList() throws Exception {\n"+
				"        Runner runner = Request.aClass(ExampleSuiteWithUnmodifyableChildList.class)\n"+
				"                .getRunner();\n"+
				"        Filter filter = notThisMethodName(\"test1\");\n"+
				"        try {\n"+
				"            filter.apply(runner);\n"+
				"        } catch (NoTestsRemainException e) {\n"+
				"            return;\n"+
				"        }\n"+
				"        fail(\"Expected 'NoTestsRemainException' due to complete filtering\");\n"+
				"    }\n"+
				"\n"+
				"    @Test\n"+
				"    public void testRunSuiteFiltering() throws Exception {\n"+
				"        Request request = Request.aClass(ExampleSuite.class);\n"+
				"        Request requestFiltered = request.filterWith(notThisMethodName(\"test1\"));\n"+
				"        assertThat(testResult(requestFiltered),\n"+
				"                hasSingleFailureContaining(\"don't run method name: test1\"));\n"+
				"    }\n"+
				"\n"+
				"    @Test\n"+
				"    public void testCountClassFiltering() throws Exception {\n"+
				"        JUnitCore junitCore = new JUnitCore();\n"+
				"        Request request = Request.aClass(ExampleTest.class);\n"+
				"        CountingFilter countingFilter = new CountingFilter();\n"+
				"        Request requestFiltered = request.filterWith(countingFilter);\n"+
				"        Result result = junitCore.run(requestFiltered);\n"+
				"        assertEquals(1, result.getRunCount());\n"+
				"        assertEquals(0, result.getFailureCount());\n"+
				"\n"+
				"        Description desc = createTestDescription(ExampleTest.class, \"test1\");\n"+
				"        assertEquals(1, countingFilter.getCount(desc));\n"+
				"    }\n"+
				"\n"+
				"    @Test\n"+
				"    public void testCountSuiteFiltering() throws Exception {\n"+
				"        Class<ExampleSuite> suiteClazz = ExampleSuite.class;\n"+
				"        Class<ExampleTest> clazz = ExampleTest.class;\n"+
				"\n"+
				"        JUnitCore junitCore = new JUnitCore();\n"+
				"        Request request = Request.aClass(suiteClazz);\n"+
				"        CountingFilter countingFilter = new CountingFilter();\n"+
				"        Request requestFiltered = request.filterWith(countingFilter);\n"+
				"        Result result = junitCore.run(requestFiltered);\n"+
				"        assertEquals(1, result.getRunCount());\n"+
				"        assertEquals(0, result.getFailureCount());\n"+
				"\n"+
				"        Description suiteDesc = createSuiteDescription(clazz);\n"+
				"        assertEquals(1, countingFilter.getCount(suiteDesc));\n"+
				"\n"+
				"        Description desc = createTestDescription(ExampleTest.class, \"test1\");\n"+
				"        assertEquals(1, countingFilter.getCount(desc));\n"+
				"    }\n"+
				"}";
		
		metric.calculate(source, toInputStream(c));
	}
	
	@Test
	public void dd() {
		String c = 
				"package org.junit.tests.internal.runners.statements;\n"+
						"\n"+
						"import static java.lang.Long.MAX_VALUE;\n"+
						"import static java.lang.Math.atan;\n"+
						"import static java.lang.System.currentTimeMillis;\n"+
						"import static java.lang.Thread.sleep;\n"+
						"import static org.hamcrest.core.Is.is;\n"+
						"import static org.junit.Assert.assertFalse;\n"+
						"import static org.junit.Assert.assertTrue;\n"+
						"import static org.junit.Assert.fail;\n"+
						"\n"+
						"import org.junit.Rule;\n"+
						"import org.junit.Test;\n"+
						"import org.junit.internal.runners.statements.FailOnTimeout;\n"+
						"import org.junit.rules.ExpectedException;\n"+
						"import org.junit.runners.model.Statement;\n"+
						"\n"+
						"/**\n"+
						" * @author Asaf Ary, Stefan Birkner\n"+
						" */\n"+
						"public class FailOnTimeoutTest {\n"+
						"    private static final int TIMEOUT = 100;\n"+
						"\n"+
						"    @Rule\n"+
						"    public final ExpectedException thrown = ExpectedException.none();\n"+
						"\n"+
						"    private final TestStatement statement = new TestStatement();\n"+
						"\n"+
						"    private final FailOnTimeout failOnTimeout = new FailOnTimeout(statement,\n"+
						"            TIMEOUT);\n"+
						"\n"+
						"    @Test\n"+
						"    public void throwExceptionWithNiceMessageOnTimeout() throws Throwable {\n"+
						"        thrown.expectMessage(\"test timed out after 100 milliseconds\");\n"+
						"        evaluateWithWaitDuration(TIMEOUT + 50);\n"+
						"    }\n"+
						"\n"+
						"    @Test\n"+
						"    public void sendUpExceptionThrownByStatement() throws Throwable {\n"+
						"        RuntimeException exception = new RuntimeException();\n"+
						"        thrown.expect(is(exception));\n"+
						"        evaluateWithException(exception);\n"+
						"    }\n"+
						"\n"+
						"    @Test\n"+
						"    public void throwExceptionIfTheSecondCallToEvaluateNeedsTooMuchTime()\n"+
						"            throws Throwable {\n"+
						"        thrown.expect(Exception.class);\n"+
						"        evaluateWithWaitDuration(0);\n"+
						"        evaluateWithWaitDuration(TIMEOUT + 50);\n"+
						"    }\n"+
						"\n"+
						"    @Test\n"+
						"    public void throwTimeoutExceptionOnSecondCallAlthoughFirstCallThrowsException()\n"+
						"            throws Throwable {\n"+
						"        thrown.expectMessage(\"test timed out after 100 milliseconds\");\n"+
						"        try {\n"+
						"            evaluateWithException(new RuntimeException());\n"+
						"        } catch (Throwable expected) {\n"+
						"        }\n"+
						"        evaluateWithWaitDuration(TIMEOUT + 50);\n"+
						"    }\n"+
						"\n"+
						"    private void evaluateWithException(Exception exception) throws Throwable {\n"+
						"        statement.nextException = exception;\n"+
						"        statement.waitDuration = 0;\n"+
						"        failOnTimeout.evaluate();\n"+
						"    }\n"+
						"\n"+
						"    private void evaluateWithWaitDuration(int waitDuration) throws Throwable {\n"+
						"        statement.nextException = null;\n"+
						"        statement.waitDuration = waitDuration;\n"+
						"        failOnTimeout.evaluate();\n"+
						"    }\n"+
						"\n"+
						"    private static final class TestStatement extends Statement {\n"+
						"        int waitDuration;\n"+
						"\n"+
						"        Exception nextException;\n"+
						"\n"+
						"        @Override\n"+
						"        public void evaluate() throws Throwable {\n"+
						"            sleep(waitDuration);\n"+
						"            if (nextException != null) {\n"+
						"                throw nextException;\n"+
						"            }\n"+
						"        }\n"+
						"    }\n"+
						"\n"+
						"    @Test\n"+
						"    public void stopEndlessStatement() throws Throwable {\n"+
						"        InfiniteLoopStatement infiniteLoop = new InfiniteLoopStatement();\n"+
						"        FailOnTimeout infiniteLoopTimeout = new FailOnTimeout(infiniteLoop,\n"+
						"                TIMEOUT);\n"+
						"        try {\n"+
						"            infiniteLoopTimeout.evaluate();\n"+
						"        } catch (Exception timeoutException) {\n"+
						"            sleep(20); // time to interrupt the thread\n"+
						"            int firstCount = InfiniteLoopStatement.COUNT;\n"+
						"            sleep(20); // time to increment the count\n"+
						"            assertTrue(\"Thread has not been stopped.\",\n"+
						"                    firstCount == InfiniteLoopStatement.COUNT);\n"+
						"        }\n"+
						"    }\n"+
						"\n"+
						"    private static final class InfiniteLoopStatement extends Statement {\n"+
						"        private static int COUNT = 0;\n"+
						"\n"+
						"        @Override\n"+
						"        public void evaluate() throws Throwable {\n"+
						"            while (true) {\n"+
						"                sleep(10); // sleep in order to enable interrupting thread\n"+
						"                ++COUNT;\n"+
						"            }\n"+
						"        }\n"+
						"    }\n"+
						"\n"+
						"    @Test\n"+
						"    public void stackTraceContainsRealCauseOfTimeout() throws Throwable {\n"+
						"        StuckStatement stuck = new StuckStatement();\n"+
						"        FailOnTimeout stuckTimeout = new FailOnTimeout(stuck, TIMEOUT);\n"+
						"        try {\n"+
						"            stuckTimeout.evaluate();\n"+
						"            // We must not get here, we expect a timeout exception\n"+
						"            fail(\"Expected timeout exception\");\n"+
						"        } catch (Exception timeoutException) {\n"+
						"            StackTraceElement[] stackTrace = timeoutException.getStackTrace();\n"+
						"            boolean stackTraceContainsTheRealCauseOfTheTimeout = false;\n"+
						"            boolean stackTraceContainsOtherThanTheRealCauseOfTheTimeout = false;\n"+
						"            for (StackTraceElement element : stackTrace) {\n"+
						"                String methodName = element.getMethodName();\n"+
						"                if (\"theRealCauseOfTheTimeout\".equals(methodName)) {\n"+
						"                    stackTraceContainsTheRealCauseOfTheTimeout = true;\n"+
						"                }\n"+
						"                if (\"notTheRealCauseOfTheTimeout\".equals(methodName)) {\n"+
						"                    stackTraceContainsOtherThanTheRealCauseOfTheTimeout = true;\n"+
						"                }\n"+
						"            }\n"+
						"            assertTrue(\n"+
						"                    \"Stack trace does not contain the real cause of the timeout\",\n"+
						"                    stackTraceContainsTheRealCauseOfTheTimeout);\n"+
						"            assertFalse(\n"+
						"                    \"Stack trace contains other than the real cause of the timeout, which can be very misleading\",\n"+
						"                    stackTraceContainsOtherThanTheRealCauseOfTheTimeout);\n"+
						"        }\n"+
						"    }\n"+
						"\n"+
						"    private static final class StuckStatement extends Statement {\n"+
						"\n"+
						"        @Override\n"+
						"        public void evaluate() throws Throwable {\n"+
						"            try {\n"+
						"                // Must show up in stack trace\n"+
						"                theRealCauseOfTheTimeout();\n"+
						"            } catch (InterruptedException e) {\n"+
						"            } finally {\n"+
						"                // Must _not_ show up in stack trace\n"+
						"                notTheRealCauseOfTheTimeout();\n"+
						"            }\n"+
						"        }\n"+
						"\n"+
						"        private void theRealCauseOfTheTimeout() throws InterruptedException {\n"+
						"            sleep(MAX_VALUE);\n"+
						"        }\n"+
						"\n"+
						"        private void notTheRealCauseOfTheTimeout() {\n"+
						"            for (long now = currentTimeMillis(), eta = now + 1000L; now < eta; now = currentTimeMillis()) {\n"+
						"                // Doesn't matter, just pretend to be busy\n"+
						"                atan(now);\n"+
						"            }\n"+
						"        }\n"+
						"    }\n"+
						"}";
		
		metric.calculate(source, toInputStream(c));
		
	}
	
	@Test
	public void ttt() {
		String c = 
				"package org.junit.runner.notification;"+
						""+
						"import static org.junit.Assert.assertEquals;"+
						"import static org.junit.Assert.assertFalse;"+
						"import static org.junit.Assert.assertNotEquals;"+
						"import static org.junit.Assert.assertThat;"+
						"import static org.junit.Assert.assertTrue;"+
						""+
						"import java.lang.reflect.Method;"+
						"import java.util.Arrays;"+
						"import java.util.HashSet;"+
						"import java.util.List;"+
						"import java.util.Set;"+
						""+
						"import org.junit.Test;"+
						""+
						"/**"+
						" * Tests for {@link SynchronizedRunListener}."+
						" * "+
						" * @author kcooney (Kevin Cooney)"+
						" */"+
						"public class SynchronizedRunListenerTest {"+
						""+
						"    private static class MethodSignature {"+
						"        private final Method fMethod;"+
						"        private final String fName;"+
						"        private final List<Class<?>> fParameterTypes;"+
						""+
						"        public MethodSignature(Method method) {"+
						"            fMethod = method;"+
						"            fName = method.getName();"+
						"            fParameterTypes = Arrays.asList(method.getParameterTypes());"+
						"        }"+
						"        "+
						"        @Override"+
						"        public String toString() {"+
						"            return fMethod.toString();"+
						"        }"+
						""+
						"        @Override"+
						"        public int hashCode() {"+
						"            return fName.hashCode();"+
						"        }"+
						""+
						"        @Override"+
						"        public boolean equals(Object obj) {"+
						"            if (this == obj) {"+
						"                return true;"+
						"            }"+
						"            if (!(obj instanceof MethodSignature)) {"+
						"                return false;"+
						"            }"+
						"            MethodSignature that = (MethodSignature) obj;"+
						"            return fName.equals(that.fName) && fParameterTypes.equals(that.fParameterTypes);"+
						"        }"+
						"    }"+
						""+
						"    private Set<MethodSignature> getAllDeclaredMethods(Class<?> type) {"+
						"        Set<MethodSignature> methods = new HashSet<MethodSignature>();"+
						"        for (Method method : type.getDeclaredMethods()) {"+
						"          methods.add(new MethodSignature(method));"+
						"        }"+
						"        return methods;"+
						"    }"+
						"    "+
						"    @Test"+
						"    public void overridesAllMethodsInRunListener() {"+
						"        Set<MethodSignature> runListenerMethods = getAllDeclaredMethods(RunListener.class);"+
						"        Set<MethodSignature> synchronizedRunListenerMethods = getAllDeclaredMethods("+
						"                SynchronizedRunListener.class);"+
						""+
						"        assertTrue(synchronizedRunListenerMethods.containsAll(runListenerMethods));"+
						"    }"+
						""+
						"    private static class NamedListener extends RunListener {"+
						"        private final String fName;"+
						""+
						"        public NamedListener(String name) {"+
						"            fName = name;"+
						"        }"+
						""+
						"        @Override"+
						"        public String toString() {"+
						"          return \"NamedListener\";"+
						"        }"+
						" "+
						"        @Override"+
						"        public int hashCode() {"+
						"            return fName.hashCode();"+
						"        }"+
						""+
						"        @Override"+
						"        public boolean equals(Object obj) {"+
						"            if (this == obj) {"+
						"                return true;"+
						"            }"+
						"            if (!(obj instanceof NamedListener)) {"+
						"                return false;"+
						"            }"+
						"            NamedListener that = (NamedListener) obj;"+
						"            return this.fName.equals(that.fName);"+
						"        }"+
						"    }"+
						""+
						"    @Test"+
						"    public void namedListenerCorrectlyImplementsEqualsAndHashCode() {"+
						"        NamedListener listener1 = new NamedListener(\"blue\");"+
						"        NamedListener listener2 = new NamedListener(\"blue\");"+
						"        NamedListener listener3 = new NamedListener(\"red\");"+
						"        "+
						"        assertTrue(listener1.equals(listener1));"+
						"        assertTrue(listener2.equals(listener2));"+
						"        assertTrue(listener3.equals(listener3));"+
						"        "+
						"        assertFalse(listener1.equals(null));"+
						"        assertFalse(listener1.equals(new Object()));"+
						""+
						"        assertTrue(listener1.equals(listener2));"+
						"        assertTrue(listener2.equals(listener1));"+
						"        assertFalse(listener1.equals(listener3));"+
						"        assertFalse(listener3.equals(listener1));"+
						""+
						"        assertEquals(listener1.hashCode(), listener2.hashCode());"+
						"        assertNotEquals(listener1.hashCode(), listener3.hashCode());"+
						"    }"+
						""+
						"    @Test"+
						"    public void toStringDelegates() {"+
						"        NamedListener listener = new NamedListener(\"blue\");"+
						"        "+
						"        assertEquals(\"NamedListener\", listener.toString());"+
						"        assertEquals(\"NamedListener (with synchronization wrapper)\", wrap(listener).toString());"+
						"    }"+
						""+
						"    @Test"+
						"    public void equalsDelegates() {"+
						"        NamedListener listener1 = new NamedListener(\"blue\");"+
						"        NamedListener listener2 = new NamedListener(\"blue\");"+
						"        NamedListener listener3 = new NamedListener(\"red\");"+
						"        "+
						"        assertEquals(wrap(listener1), wrap(listener1));"+
						"        assertEquals(wrap(listener1), wrap(listener2));"+
						"        assertNotEquals(wrap(listener1), wrap(listener3));"+
						"        assertNotEquals(wrap(listener1), listener1);"+
						"        assertNotEquals(listener1, wrap(listener1));"+
						"    }"+
						""+
						"    @Test"+
						"    public void hashCodeDelegates() {"+
						"        NamedListener listener = new NamedListener(\"blue\");"+
						"        assertEquals(listener.hashCode(), wrap(listener).hashCode());"+
						"    }"+
						""+
						"    private SynchronizedRunListener wrap(RunListener listener) {"+
						"        return new SynchronizedRunListener(listener, this);"+
						"    }"+
						"}";
		
		metric.calculate(source, toInputStream(c));
	}

}
