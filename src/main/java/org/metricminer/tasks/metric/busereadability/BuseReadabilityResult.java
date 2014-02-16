package org.metricminer.tasks.metric.busereadability;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class BuseReadabilityResult implements MetricResult {
	
	@Id @GeneratedValue
	private Long id;
	
	private String methodName;
	
	private Double readability;

	@Deprecated
	BuseReadabilityResult() {
	}

	public BuseReadabilityResult(String methodName, Double readability) {
		this.methodName = methodName;
		this.readability = readability;
	}

}
