package org.metricminer.infra.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.tasks.metric.AvaiableMetricResults;
import org.metricminer.tasks.metric.MetricInfo;
import org.metricminer.tasks.metric.cc.CCMetricFactory;
import org.metricminer.tasks.metric.cc.CCResult;
import org.metricminer.tasks.metric.lcom.LComMetricFactory;
import org.metricminer.tasks.metric.lcom.LComResult;

public class MetricDaoTest extends DaoTest {

	private MetricDao metricDao;

	@Before
	public void setup() {
		AvaiableMetricResults avaiableMetrics = new AvaiableMetricResults(null)
			.add(CCMetricFactory.class, LComMetricFactory.class);
		metricDao = new MetricDao(session, avaiableMetrics, null);
	}

	@Test
	public void shouldFindColumns() {
		List<ColumnMetadata> columns = metricDao.getColumns(CCResult.class);
		
		ColumnMetadata column1 = new ColumnMetadata("avgCc", "double");
		ColumnMetadata column2 = new ColumnMetadata("cc", "int");
		ColumnMetadata column3 = new ColumnMetadata("sourceCode_id", "bigint");
		
		assertTrue(columns.contains(column1));
		assertTrue(columns.contains(column2));
		assertTrue(columns.contains(column3));
	}
	
	@Test
	public void shouldListAvaiableMetrics() throws Exception {
		List<MetricInfo> avaiableResults = metricDao.listAvaiableResults();
		
		assertTrue(avaiableResults.contains(new MetricInfo("Cyclomatic Complexity", CCResult.class)));
		assertTrue(avaiableResults.contains(new MetricInfo("LCOM", LComResult.class)));
//		assertTrue(avaiableResults.contains(LComResult.class));
	}

}
