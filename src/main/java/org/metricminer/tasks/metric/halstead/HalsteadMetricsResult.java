package org.metricminer.tasks.metric.halstead;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class HalsteadMetricsResult implements MetricResult {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	private SourceCode source;
	private String methodName;
	private int operands;
	private int operators;
	private int uniqueOperands;
	private int uniqueOperators;

	@Deprecated
	HalsteadMetricsResult() {
	}

	public HalsteadMetricsResult(SourceCode source, String methodName,
			int operands, int operators, int uniqueOperands, int uniqueOperators) {
				this.source = source;
				this.methodName = methodName;
				this.operands = operands;
				this.operators = operators;
				this.uniqueOperands = uniqueOperands;
				this.uniqueOperators = uniqueOperators;
	}

}
