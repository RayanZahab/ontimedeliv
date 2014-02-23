package com.example.ontimedeliv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

// Class with extends AsyncTask class

public class MyJs extends AsyncTask<String, Void, Void> {

	private String Content;
	private String returnFunction, Error;
	private ProgressDialog Dialog;
	String data = "";
	public TextView uiUpdate;
	int sizeData = 0;
	private Activity mc;
	private String method;

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			String method) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.method = method;
	}

	protected void onPreExecute() {
		Dialog.setMessage("Please wait..");
		Dialog.show();
		try {
			Log.d("Output : ", "Ray &" + URLEncoder.encode("data", "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Call after onPreExecute returnFunction
	protected Void doInBackground(String... urls) {

		/************ Make Post Call To Web Server ***********/
		BufferedReader reader = null;

		// Send data
		try {

			// Defined URL where to send data
			URL url = new URL(urls[0]);

			// Send POST data request

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(this.method);
			conn.setRequestProperty("Content-Type",
					"application/json; charset=utf-8");
			conn.addRequestProperty("auth_token", "abaed8959dedfccc79b5");

			// Get the server response
			if (this.method == "GET") {
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + "\n");
				}
				Content = sb.toString();
				Log.d("ray", "ray cont: " + Content);
			} else if (this.method == "POST") {
				conn.addRequestProperty("Accept", "application/json");
				conn.addRequestProperty("Accept-Encoding", "gzip");
				conn.addRequestProperty("Cache-Control",
						"max-stale=0,max-age=60");
				conn.setDoOutput(true);
				conn.setDoInput(true);

				JSONObject jsonObjSend = new JSONObject();
				JSONObject body = new JSONObject();
				body.put("name", "Tunisi8");
				jsonObjSend.put("country", body);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				Log.d("ray", "ray posting" + conn.toString());

				wr.write(jsonObjSend.toString());
				wr.flush();
				wr.close();
				Log.d("ray",
						"ray resp" + conn.getResponseCode() + "=>"
								+ conn.getResponseMessage());

				if (conn.getResponseCode() != 201) {
					Log.d("ray", "ray mazabat" + conn.getResponseMessage());
					Content = conn.getResponseMessage();
					// throw new Exception("POST method failed: " +
					// conn.getResponseCode() + "\t" +
					// conn.getResponseMessage());
				} else {
					// BufferedReader responseContent = (BufferedReader)
					// conn.getContent();
					BufferedReader responseContent = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;

					// Read Server Response
					while ((line = responseContent.readLine()) != null) {
						// Append server response in string
						sb.append(line + "\n");
					}
					Content = sb.toString();
					Log.d("ray", "ray WIW : " + Content);
				}
			}
		} catch (Exception ex) {
			Error = ex.getLocalizedMessage();
			ex.printStackTrace();
			Log.d("ray", "ray err:" + Error);
		} finally {
			try {
				reader.close();
			} catch (Exception ex) {
			}
		}

		/*****************************************************/
		return null;
	}

	protected void onPostExecute(Void unused) {
		Log.d("ray", "Ray Post: " + this.returnFunction + ":" + Content);
		Dialog.dismiss();
		try {
			if (Content == null)
				Content = "";
			Method returnFunction = this.mc.getClass().getMethod(
					this.returnFunction, Content.getClass());
			returnFunction.invoke(this.mc, Content);
			Log.d("ray", "Ray zabt");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ray", "Ray excep: " + e.getMessage());

		}

	}

}
