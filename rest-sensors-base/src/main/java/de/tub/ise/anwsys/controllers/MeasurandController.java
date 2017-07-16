package de.tub.ise.anwsys.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tub.ise.anwsys.models.Measurand;
import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.MeasurandRepository;
import de.tub.ise.anwsys.repos.SmartMeterRepository;

@RestController
public class MeasurandController {

	@Autowired
	MeasurandRepository repository;
	@Autowired
	SmartMeterRepository smRepository;

	/**
	 * gets a map of all measurands of a smart meter
	 * 
	 * @param smartmeter
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter/{smartmeter}")
	public Map<String, String> getAllMeasurands(@PathVariable String smartmeter) {
		HashMap<String, String> map = new HashMap<String, String>();
		List<Measurand> list = repository.findBySmartmeter(smRepository.findByName(smartmeter));
		for (Measurand m : list) {
			map.put(m.getMetricId(), m.getMetricText());
		}
		return map;
	}

	/**
	 * creates a new measurand or adds a measurand to a smart meter
	 * 
	 * @param smartmeter
	 * @param measurand
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/smartmeter/{smartmeter}")
	public void createMeasurand(@PathVariable String smartmeter, @RequestParam(value = "measurand") JSONArray measurand)
			throws JSONException {
		for (int i = 0; i < measurand.length(); i++) {
			String metricId = measurand.getJSONObject(i).getString("metricId").toString();
			String metricText = measurand.getJSONObject(i).getString("metricText").toString();
			Measurand m = repository.findByMetricId(metricId);
			SmartMeter sm = smRepository.findByName(smartmeter);
			if (m == null)
				m = new Measurand(metricId, metricText, sm);
			else {
				if (!m.getSmartmeter().contains(sm))
					m.addToSmartMeterList(sm);
			}
			repository.save(m);
		}
	}

}