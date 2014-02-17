package org.metricminer.tasks.metric.busereadability;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class BuseReadabilityResult implements MetricResult {
	
	@Id @GeneratedValue
	private Long id;
	
	private String methodName;
	
	private Double readability;
	
	@ManyToOne
	private SourceCode sourceCode;

	@Deprecated
	BuseReadabilityResult() {
	}

	public BuseReadabilityResult(String methodName, Double readability, SourceCode sourceCode) {
		this.methodName = methodName;
		this.readability = readability;
		this.sourceCode = sourceCode;
	}

}
