package org.metricminer.model;

import org.metricminer.MetricMinerExeption;

public class RegisteredMetric {
	private String name;
	private Class<?> metricFactoryClass;

	public RegisteredMetric() {
	}

	public RegisteredMetric(String name, Class<?> metricFactoryClass) {
		this.name = name;
		this.metricFactoryClass = metricFactoryClass;
	}

	public String getName() {
		return name;
	}

	public Class<?> getMetricFactoryClass() {
		return metricFactoryClass;
	}

	public String getMetricFactoryClassName() {
		String className = metricFactoryClass.toString();
		className = className.substring(6); // class org.metricminer.etcetc
		return className;
	}

	private void setMetricFactoryClassName(String className) {
		try {
			this.metricFactoryClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new MetricMinerExeption("Class not" + className + " found", e);
		}
	}

	@Override
	public String toString() {
		return name;
	}

}
