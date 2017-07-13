package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Measurand implements Serializable {

	private static final long serialVersionUID = 3501450469684231867L;

	private String metricId;
	private String metricText;
	private List<SmartMeter> smartmeter = new ArrayList<SmartMeter>();

	protected Measurand() {
		// empty constructor required by JPA
	}

	public Measurand(String metricId, String metricText, SmartMeter smartmeter) {
		this.metricId = metricId;
		this.metricText = metricText;
		this.smartmeter.add(smartmeter);
	}

	@Id
	public String getMetricId() {
		return this.metricId;
	}

	public void setMetricId(String name) {
		this.metricId = name;
	}

	public String getMetricText() {
		return metricText;
	}

	public void setMetricText(String metricText) {
		this.metricText = metricText;
	}

	@ManyToMany
	public List<SmartMeter> getSmartmeter() {
		return smartmeter;
	}

	public void setSmartmeter(List<SmartMeter> smartmeter) {
		this.smartmeter = smartmeter;
	}

	public void addToSmartMeterList(SmartMeter smartmeter) {
		this.smartmeter.add(smartmeter);
	}

	@Override
	public String toString() {
		return this.metricId;
	}

}