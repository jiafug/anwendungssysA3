package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class SmartMeter implements Serializable {

	private static final long serialVersionUID = -6640481949420444264L;

	private String name;
	private List<Measurand> measurand = new ArrayList<Measurand>();
	private List<Record> record = new ArrayList<Record>();

	protected SmartMeter() {
		// empty constructor required by JPA
	}

	public SmartMeter(String name) {
		this.name = name;
	}

	@Id
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany
	public List<Measurand> getMeasurand() {
		return measurand;
	}

	public void setMeasurand(List<Measurand> measurand) {
		this.measurand = measurand;
	}

	@OneToMany(mappedBy = "smartmeter")
	public List<Record> getRecord() {
		return record;
	}

	public void setRecord(List<Record> record) {
		this.record = record;
	}

	@Override
	public String toString() {
		return this.name;
	}

}