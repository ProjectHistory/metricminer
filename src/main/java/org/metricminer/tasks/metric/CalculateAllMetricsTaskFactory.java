package org.metricminer.tasks.metric;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.MetricMinerExeption;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.MetricDao;
import org.metricminer.infra.dao.SourceCodeDao;
import org.metricminer.model.RegisteredMetric;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.PrototypeScoped;

@Component @PrototypeScoped
public class CalculateAllMetricsTaskFactory implements RunnableTaskFactory {

	@Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession, MetricMinerConfigs config) {
    	List<RegisteredMetric> registeredMetrics = config.getRegisteredMetrics();
    	List<Metric> metrics = new ArrayList<Metric>();
    	for (RegisteredMetric registeredMetric : registeredMetrics) {
    		MetricFactory metricFactory;
    		String className = registeredMetric.getMetricFactoryClassName();
			try {
				metricFactory = (MetricFactory) Class.forName(className).newInstance();
			} catch (Exception e) {
				throw new MetricMinerExeption("Could not instatiate metric factory for: "+ className, e);
			}
    		metrics.add(metricFactory.build());
		}
		MetricDao metricDao = new MetricDao(session, null, statelessSession);
		SourceCodeDao sourceCodeDao = new SourceCodeDao(statelessSession);
		return new CalculateAllMetricsTask(task, metricDao, metrics, sourceCodeDao, statelessSession);
    }
}
