package de.tub.ise.anwsys.controllers;

import java.util.List;

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

	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter/{smartmeter}")
	public List<Measurand> answer(@PathVariable String smartmeter) {
		return repository.findBySmartmeter(smRepository.findByName(smartmeter));
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartmeter/{smartmeter}")
	public void createMeasurand(@PathVariable String smartmeter, @RequestParam(value = "measurand") JSONArray measurand)
			throws JSONException {
		for (int i = 0; i < measurand.length(); i++) {
			String metricId = measurand.getJSONObject(i).getString("metricId").toString();
			Measurand m;
			SmartMeter sm = smRepository.findByName(smartmeter);
			if (repository.findByMetricId(metricId) == null) {
				m = new Measurand(metricId, sm);
			} else {
				m = repository.findByMetricId(metricId);
				m.addToSmartMeterList(sm);
			}
			repository.save(m);
		}
	}

}