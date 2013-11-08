package org.metricminer.infra.dao;

import java.util.List;









import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.CalculatedMetric;
import org.metricminer.tasks.metric.AvaiableMetricResults;
import org.metricminer.tasks.metric.MetricInfo;
import org.metricminer.tasks.metric.common.MetricResult;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class MetricDao {

	private Session session;
	private AvaiableMetricResults avaiableMetrics;
	private StatelessSession statelessSession;

	public MetricDao(Session session, AvaiableMetricResults avaiableMetrics, StatelessSession statelessSession) {
		this.session = session;
		this.avaiableMetrics = avaiableMetrics;
		this.statelessSession = statelessSession;
	}
	
	public List<ColumnMetadata> getColumns(Class<?> metric) {
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "
				+ "information_schema.COLUMNS "
				+ "WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=:class";
		return ColumnMetadata.build(session.createSQLQuery(sql)
			.setString("class", metric.getSimpleName())
			.list());
	}

	public List<MetricInfo> listAvaiableResults() {
		return avaiableMetrics.getMetrics();
	}

	public void save(MetricResult result) {
		statelessSession.insert(result);
	}

	public void save(CalculatedMetric calculatedMetric) {
		statelessSession.insert(calculatedMetric);
		
	}
}
