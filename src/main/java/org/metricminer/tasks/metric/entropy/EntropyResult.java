package org.metricminer.tasks.metric.entropy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class EntropyResult implements MetricResult {
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private SourceCode source;
	private double entropy;

	public EntropyResult(SourceCode source, double entropy) {
		this.source = source;
		this.entropy = entropy;
	}
	
	

}
