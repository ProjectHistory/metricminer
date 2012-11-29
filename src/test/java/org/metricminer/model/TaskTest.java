package org.metricminer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.tasks.metric.CalculateMetricTaskFactory;
import org.metricminer.tasks.metric.cc.CCMetricFactory;
import org.metricminer.tasks.metric.lcom.LComMetricFactory;

public class TaskTest {

	private Task task;
    private Project project;
    
    @Before
    public void setUp() {
        project = mock(Project.class);
        task = new Task(project, "Task", CalculateMetricTaskFactory.class, 0);
    }

    @Test
	public void shouldFindAConfigurationEntryForAGivenKey() throws Exception {
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS, "someclass");
		String entryValue = task
				.getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS);
		assertNotNull(entryValue);
		assertEquals(entryValue, "someclass");
	}

	@Test
	public void shouldVerifyThatATaskWillCaclulateAMetric() throws Exception {
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS,
				new RegisteredMetric("cc", CCMetricFactory.class).getMetricFactoryClassName());
		assertTrue(task.willCalculate(new RegisteredMetric("some metric", CCMetricFactory.class)));
	}

	@Test
	public void shouldVerifyThatATaskWillNotCaclulateAMetric() throws Exception {
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS,
				new RegisteredMetric("cc", LComMetricFactory.class).getMetricFactoryClassName());
		assertFalse(task.willCalculate(new RegisteredMetric("some metric", CCMetricFactory.class)));
	}
}
