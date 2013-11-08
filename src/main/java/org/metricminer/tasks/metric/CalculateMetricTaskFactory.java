package org.metricminer.tasks.metric;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.MetricDao;
import org.metricminer.infra.dao.SourceCodeDao;
import org.metricminer.model.Task;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.PrototypeScoped;

@Component @PrototypeScoped
public class CalculateMetricTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Task task, Session session, StatelessSession statelessSession,
			MetricMinerConfigs config) {
		String metricFactoryName = task
				.getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS);
		try {
			MetricFactory metricFactory = (MetricFactory) Class.forName(metricFactoryName)
					.newInstance();

			Metric metricToCalculate = metricFactory.build();
			MetricDao metricDao = new MetricDao(session, null, statelessSession);
			SourceCodeDao sourceCodeDao = new SourceCodeDao(statelessSession);
			return new CalculateMetricTask(task, metricToCalculate, session, statelessSession, metricDao, sourceCodeDao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
