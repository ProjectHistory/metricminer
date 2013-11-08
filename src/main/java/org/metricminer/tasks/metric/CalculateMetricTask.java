package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.infra.dao.MetricDao;
import org.metricminer.infra.dao.SourceCodeDao;
import org.metricminer.model.CalculatedMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateMetricTask extends SourcesIteratorAbstractTask {

	private final Metric metric;
	private final StatelessSession statelessSession;
	private MetricDao metricDao;

	public CalculateMetricTask(Task task, Metric metric, Session session, 
			StatelessSession statelessSession, MetricDao metricDao, SourceCodeDao sourceCodeDao) {
		super(task, sourceCodeDao);
		this.metric = metric;
		this.statelessSession = statelessSession;
		this.metricDao = metricDao;
	}

	@Override
	protected void manipulate(SourceCode sourceCode, String name) {
		calculateAndSaveResultsOf(sourceCode, name);
	}

	private void calculateAndSaveResultsOf(SourceCode sourceCode, String name) {
		if (!metric.matches(name))
			return;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceCode.getSourceBytesArray());
			metric.calculate(sourceCode, inputStream);
			inputStream.close();
			Collection<MetricResult> results = metric.results();
			statelessSession.beginTransaction();
			for (MetricResult result : results) {
				metricDao.save(result);
			}
			statelessSession.getTransaction().commit();
		} catch (Throwable t) {
			log.error("Unable to calculate metric: ", t);
		}
	}

	@Override
	protected void onComplete() {
		statelessSession.beginTransaction();
		CalculatedMetric calculatedMetric = new CalculatedMetric(task.getProject(),
				metric.getFactoryClass());
		statelessSession.insert(calculatedMetric);
		statelessSession.getTransaction().commit();
	}

}
