package org.metricminer.tasks.metric.entropy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class EntropyPerMethodResult implements MetricResult {
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private SourceCode source;
	
	private double entropy;

	private String method;

	public EntropyPerMethodResult(SourceCode source, String method, double entropy) {
		this.source = source;
		this.method = method;
		this.entropy = entropy;
	}
	
	

}
