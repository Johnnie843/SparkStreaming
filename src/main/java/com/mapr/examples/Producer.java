package com.mapr.examples;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.io.Resources;

public class Producer {
	public static void main(String[] args) throws IOException {

		KafkaProducer<String, String> producer;
		try (InputStream props = Resources.getResource("producer.props").openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			producer = new KafkaProducer<>(properties);
		}
		try {
			while (true) {
				long messageCount = 0;
				JSONObject json = null;
				try {
					json = readJsonFromUrl();
					System.out.println(json.get("rates"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				producer.send(new ProducerRecord<String, String>("bitcoin", json.toString()));
				messageCount++;
				System.out.println("Sent msg number " + messageCount);
				Thread.sleep(20000);
			}

		} catch (Throwable throwable) {
			System.out.printf("%s", throwable.getStackTrace());
		} finally {
			producer.close();
		}

	}
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl() throws IOException, JSONException {
		InputStream is = new URL("http://data.fixer.io/api/latest?access_key=97736f4ca882ac5ca472e708507c96e3").openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}
