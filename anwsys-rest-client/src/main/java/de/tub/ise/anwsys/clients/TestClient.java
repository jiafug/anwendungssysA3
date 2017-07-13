package de.tub.ise.anwsys.clients;

import java.io.IOException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestClient {

	public static void main(String[] args) throws IOException, UnirestException {

		HttpResponse<JsonNode> response = Unirest.get("http://localhost:7878/meters").asJson();
		Unirest.post("http://localhost:8080/smartmeter").field("smartmeter", response.getBody().getObject()).asJson();

		HttpResponse<JsonNode> metric1 = Unirest.get("http://localhost:7878/meters/ise1224hi5630/").asJson();
		HttpResponse<JsonNode> metric2 = Unirest.get("http://localhost:7878/meters/ise1224hi5631/").asJson();
		HttpResponse<JsonNode> metric3 = Unirest.get("http://localhost:7878/meters/ise1224hi5632/").asJson();
		Unirest.post("http://localhost:8080/smartmeter/ise1224hi5630").field("measurand", metric1.getBody().getArray())
				.asJson();
		Unirest.post("http://localhost:8080/smartmeter/ise1224hi5631").field("measurand", metric2.getBody().getArray())
				.asJson();
		Unirest.post("http://localhost:8080/smartmeter/ise1224hi5632").field("measurand", metric3.getBody().getArray())
				.asJson();

		// Calculates the average strength of an electric current in amperes
		double avg = 0;
		for (int i = 0; i < 1000; i++) {
			HttpResponse<JsonNode> response3 = Unirest.get("http://localhost:7878/meters/ise1224hi5632/data").asJson();
			avg += (Double) response3.getBody().getObject().getJSONArray("measurements").getJSONObject(0).get("value");
		}
		double result = avg / 1000;
		System.out.println("Durchschnittliche Stromstärke: " + result + " Current(mA)");

	}

}
