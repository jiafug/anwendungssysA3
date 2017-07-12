package de.tub.ise.anwsys.controllers;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.SmartMeterRepository;

@RestController
public class SmartMeterController {

	@Autowired
	SmartMeterRepository repository;

	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter")
	public List<SmartMeter> answer() {
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartmeter")
	public void register(@RequestParam(value = "smartmeter") JSONObject smartmeter) throws JSONException {
		for (int i = 0; i < smartmeter.getJSONArray("meters").length(); i++) {
			String name = smartmeter.getJSONArray("meters").get(i).toString();
			SmartMeter sm = new SmartMeter(name);
			repository.save(sm);
		}
		answer();
	}

}