package main.java.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {

	public static JSONObject executeHttpRequest(String uri, String method, Map<String, String> properties, Map<String, String> parameters) {
		JSONObject response = null;
		try {
			HttpURLConnection connection;
			if (method.equalsIgnoreCase("GET")) {
				// build get request
				URL url = new URL(uri + "?" + buildParameterString(parameters));
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				properties.forEach((key, value) -> connection.setRequestProperty(key, value));
			}
			else if (method.equalsIgnoreCase("POST")) {
				// build post request
				URL url = new URL(uri);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				properties.forEach((key, value) -> connection.setRequestProperty(key, value));
				connection.setDoOutput(true);
			 	DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
			 	outStream.writeBytes(buildParameterString(parameters));
			 	outStream.flush();
			 	outStream.close();
			}
			else {
				// handle invalid request method
				throw new IllegalArgumentException("invalid http request method: " + method);
			}

			// set timeouts
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			// read response
			int status = connection.getResponseCode();
			InputStreamReader streamReader = (status > 299) 
					? new InputStreamReader(connection.getErrorStream())
					: new InputStreamReader(connection.getInputStream());
			BufferedReader connectionReader = new BufferedReader(streamReader);
			String currentLine;
			StringBuffer content = new StringBuffer();
			while ((currentLine = connectionReader.readLine()) != null) content.append(currentLine);
			connectionReader.close();

			// close connection
			connection.disconnect();
			
			// generate and return json
			response = new JSONObject(content.toString());
	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return response;
	}
	
	private static String buildParameterString(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder outString = new StringBuilder();
		for (Map.Entry<String, String> param : parameters.entrySet()) {
			outString.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			outString.append("=");
			outString.append(URLEncoder.encode(param.getValue(), "UTF-8"));
			outString.append("&");
		}
		// remove trailing '&'
		if (outString.length() > 0) outString.setLength(outString.length() - 1);
		return outString.toString();
	}
	
}
