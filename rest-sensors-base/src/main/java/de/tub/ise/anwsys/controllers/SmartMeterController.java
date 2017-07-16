package de.tub.ise.anwsys.controllers;

import java.util.ArrayList;
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

	/**
	 * gets a list of all smart meters
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/smartmeter")
	public List<String> getAllSmartMeters() {
		List<SmartMeter> list = repository.findAll();
		List<String> nameList = new ArrayList<String>();
		for (SmartMeter sm : list)
			nameList.add(sm.getName());
		return nameList;
	}

	/**
	 * creates new smart meters
	 * 
	 * @param smartmeter
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/smartmeter")
	public void registerNewSmartMeter(@RequestParam(value = "smartmeter") JSONObject smartmeter) throws JSONException {
		for (int i = 0; i < smartmeter.getJSONArray("meters").length(); i++) {
			String name = smartmeter.getJSONArray("meters").get(i).toString();
			SmartMeter sm = new SmartMeter(name);
			repository.save(sm);
		}
	}

}