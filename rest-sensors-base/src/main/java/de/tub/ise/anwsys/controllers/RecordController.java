package de.tub.ise.anwsys.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tub.ise.anwsys.models.Measurand;
import de.tub.ise.anwsys.models.Record;
import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.MeasurandRepository;
import de.tub.ise.anwsys.repos.RecordRepository;
import de.tub.ise.anwsys.repos.SmartMeterRepository;

@RestController
public class RecordController {

	@Autowired
	RecordRepository repository;
	
	@Autowired
	MeasurandRepository measurand_repository;
	
	@Autowired
	SmartMeterRepository smartmeter_repository;
	
	@RequestMapping(method = RequestMethod.GET, path = "/record/{smartmeter}/{metricId}/{time}")
	public List<Record> answer() {
		// to do
		return repository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/record/{smartmeter}")
	public List<Record> list(@PathVariable String smartmeter) {
		List<Record> list=repository.findAll();
		List<Record> list2= new ArrayList<Record>();
		for(Record r:list){
			if(r.getSmartmeterName().equals(smartmeter)){
				list2.add(r);
			}
		}
		return list2;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/record")
	public void register(@RequestParam(value = "record") JSONObject record) throws JSONException {
	    Measurand measurand = measurand_repository.findByMetricId(record.getString("metricId").toString());
	    String smartmetername = record.getString("smartmeter").toString();
	    SmartMeter smartmeter=smartmeter_repository.findByName(smartmetername);
	    double value = record.getDouble("value");
	    int time = record.getInt("time");
	    Record r = new Record(measurand,value,time,smartmeter);
	    repository.save(r);
	}
	
	
}