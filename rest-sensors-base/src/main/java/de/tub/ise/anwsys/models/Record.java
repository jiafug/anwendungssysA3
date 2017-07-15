package de.tub.ise.anwsys.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Record implements Serializable {

	private static final long serialVersionUID = 2831438593938521629L;

	private int id;
	private int time; ///second since epoch
	private Measurand measurand;
	private double value;
	private SmartMeter smartmeter;

	protected Record() {
		// empty constructor required by JPA
	}

	public Record(Measurand measurand, double value, int time, SmartMeter smartmeter) {
		this.measurand = measurand;
		this.time=time;
		this.value = value;
		this.smartmeter=smartmeter;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Measurand getMeasurand() {
		return measurand;
	}

	public void setMeasurand(Measurand measurand) {
		this.measurand = measurand;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "smartmeter")
	public SmartMeter getSmartmeter() {
		return smartmeter;
	}

	public void setSmartmeter(SmartMeter smartmeter) {
		this.smartmeter = smartmeter;
	}
	
	public void setSmartmeterName(String smartmetername) {
		this.smartmeter.setName(smartmetername);
	}

	@Override
	public String toString() {
		return this.id + "";
	}

	public String getSmartmeterName() {
		return this.smartmeter.getName();
	}

}