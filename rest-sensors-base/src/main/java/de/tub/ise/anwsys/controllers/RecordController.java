package de.tub.ise.anwsys.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
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

	/**
	 * returns a map of a specific smart meter which is grouped by time
	 * 
	 * @param smartmeter
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter/{smartmeter}/record")
	public Map<Integer, List<Map<String, Double>>> getMapOfRecord(@PathVariable String smartmeter)
			throws JSONException {
		// gets a list of all records of this smart meter
		List<Record> list = repository.findBySmartmeter(smartmeter_repository.findByName(smartmeter));
		// gets the latest record
		Optional<Record> latestRecord = list.stream()
				.max((r1, r2) -> Integer.compare(r1.getTime(), r2.getTime()));
		int latestTime = latestRecord.get().getTime();
		// gets all records with the same time stamp
		Map<Integer, List<Record>> newMap = list.stream()
				.filter(r -> r.getTime() == latestTime)
				.collect(Collectors.groupingBy(Record::getTime));
		// initializes the map that is going to be returned with a time as key
		Map<Integer, List<Map<String, Double>>> map = new HashMap<Integer, List<Map<String, Double>>>();
		// collects all records that have the same time
		List<Record> allRecordsOfSameTime = newMap.get(latestTime);
		// initializes a value list of the map that is going to be returned
		List<Map<String, Double>> toAdd = new ArrayList<Map<String, Double>>();
		// iterates over the list of all records that have the same time
		for (Record r2 : allRecordsOfSameTime) {
			// creates a new value for the list above
			Map<String, Double> mapValue = new HashMap<String, Double>();
			mapValue.put(r2.getMeasurand().getMetricId(), r2.getValue());
			// adds the map to the list above
			toAdd.add(mapValue);
		}
		// puts the list toAdd into the map that is going to be returned
		map.put(latestTime, toAdd);
		// returns a map
		return map;
	}

	/**
	 * creates a new record
	 * 
	 * @param smartmeter
	 * @param record
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/smartmeter/{smartmeter}/record")
	public void createNewRecord(@PathVariable String smartmeter, @RequestParam(value = "record") JSONArray record)
			throws JSONException {
		// finds the smart meter by name
		SmartMeter sm = smartmeter_repository.findByName(smartmeter);
		// gets time
		int time = (int) record.getJSONObject(0).get("unixTimestamp");
		// creates a new record
		for (int i = 1; i < record.length(); i++) {
			String metricId = record.getJSONObject(i).getString("metricId");
			Measurand measurand = measurand_repository.findByMetricId(metricId);
			double value = record.getJSONObject(i).getDouble("value");
			Record r = new Record(measurand, value, sm, time);
			repository.save(r);
		}
	}

	/**
	 * returns a map of a specific smart meter and measurand which is grouped by
	 * time
	 * 
	 * @param smartmeter
	 * @param metric
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter/{smartmeter}/record/{metric}")
	public Map<Integer, List<Map<String, Double>>> getRecordOfSpecificMetric(@PathVariable String smartmeter,
			@PathVariable String metric) throws JSONException {
		// gets a list of all records of this smart meter
		List<Record> list = repository.findBySmartmeter(smartmeter_repository.findByName(smartmeter));
		// gets the latest record
		Optional<Record> latestRecord = list.stream()
				.max((r1, r2) -> Integer.compare(r1.getTime(), r2.getTime()));
		int latestTime = latestRecord.get().getTime();
		// gets all records with the same time stamp
		List<Record> newList = list.stream()
				.filter(r -> r.getMeasurand().getMetricId().equals(metric))
				.filter(r -> r.getTime() == latestTime)
				.collect(Collectors.toList());
		Map<Integer, List<Map<String, Double>>> map = new HashMap<Integer, List<Map<String, Double>>>();
		// iterates over the list of all records that have the same time
		List<Map<String, Double>> toAdd = new ArrayList<Map<String, Double>>();
		for (Record r : newList) {
			// creates a new value for the list above
			Map<String, Double> mapValue = new HashMap<String, Double>();
			mapValue.put(r.getMeasurand().getMetricId(), r.getValue());
			// adds the map to the list above
			toAdd.add(mapValue);
		}
		// puts the list toAdd into the map that is going to be returned
		map.put(latestTime, toAdd);
		// returns a map
		return map;
	}

}