package de.tub.ise.anwsys.clients;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestClient {

	public static void main(String[] args) throws IOException, UnirestException {

		// creates all smart meters
		HttpResponse<JsonNode> response = Unirest.get("http://localhost:7878/meters").asJson();
		Unirest.post("http://localhost:8080/smartmeter").field("smartmeter", response.getBody().getObject()).asJson();

		// creates measurands
		for (int i = 0; i < 3; i++) {
			HttpResponse<JsonNode> metric = Unirest.get("http://localhost:7878/meters/ise1224hi563" + i).asJson();
			Unirest.post("http://localhost:8080/smartmeter/ise1224hi563" + i)
					.field("measurand", metric.getBody().getArray()).asJson();
		}

		// calculates the average value
		int k = 0;
		while (k < 1000) {
			double avgCurr = 0;
			double avgVolt = 0;
			for (int j = 0; j < 3; j++) {
				int time = 0;
				for (int i = 0; i < 900; i++) {
					HttpResponse<JsonNode> record = Unirest
							.get("http://localhost:7878/meters/ise1224hi563" + j + "/data").asJson();
					avgCurr += (Double) record.getBody().getObject().getJSONArray("measurements").getJSONObject(0)
							.get("value");
					avgVolt += (Double) record.getBody().getObject().getJSONArray("measurements").getJSONObject(1)
							.get("value");
					time = (int) record.getBody().getObject().get("unixTimestamp");
				}
				double resultCurr = avgCurr / 900;
				double resultVolt = avgVolt / 900;
				// average current of the smart meter which id ends with 2 
				if (j == 2) {
					System.out.println("average current (15 min interval): " + resultCurr);
				}
				// creates a JSON array
				JSONArray array = new JSONArray();
				JSONObject metric1 = new JSONObject();
				metric1.put("metricId", "MX-11460-01");
				metric1.put("value", resultCurr);
				JSONObject metric2 = new JSONObject();
				metric2.put("metricId", "MX-11463-01");
				metric2.put("value", resultVolt);
				JSONObject timeObject = new JSONObject();
				timeObject.put("unixTimestamp", time);
				array.put(timeObject);
				array.put(metric1);
				array.put(metric2);
				Unirest.post("http://localhost:8080/smartmeter/ise1224hi563" + j + "/record").field("record", array)
						.asJson();
			}
			k++;
		}
	}

}
