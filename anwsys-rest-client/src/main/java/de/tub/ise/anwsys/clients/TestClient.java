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
		
		
		//Calculates the average strength of an electric current in amperes
		double avg = 0;
		for (int i = 0; i < 1000; i++) {
			HttpResponse<JsonNode> response2 = Unirest.get("http://localhost:7878/meters/ise1224hi5632/data").asJson();
			avg += (Double)response2.getBody().getObject().getJSONArray("measurements").getJSONObject(0).get("value");
		}
		double result = avg/1000;
		System.out.println("Durchschnittliche Stromstärke: " + result + " Current(mA)");

	}

}
