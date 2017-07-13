package de.tub.ise.anwsys.controllers;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tub.ise.anwsys.models.Record;
import de.tub.ise.anwsys.repos.RecordRepository;

@RestController
public class RecordController {

	@Autowired
	RecordRepository repository;

	@RequestMapping(method = RequestMethod.GET, path = "/record")
	public List<Record> answer() {
		// to do
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/record")
	public void register(@RequestParam(value = "smartmeter") JSONObject smartmeter) throws JSONException {
		// to do
	}

}