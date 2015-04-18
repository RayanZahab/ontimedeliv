package com.mobilife.delivery.admin.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.model.Product;
import com.mobilife.delivery.admin.view.customcomponent.TransparentProgressDialog;

public class MyJs extends AsyncTask<String, Void, Void> {

	private String Content;
	private String returnFunction, Error;
	String data = "";
	public TextView uiUpdate;
	int sizeData = 0;
	private Activity mc;
	private String method;
	boolean first = false;
	private boolean last = true;
	private Object objectToAdd;
	String token;
	private DeliveryAdminApplication global;
	TransparentProgressDialog pd;
	GlobalM glob = new GlobalM();

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mc
				.getApplicationContext().getSystemService(
						mc.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public MyJs(String returnFunction, Activity m, DeliveryAdminApplication mg,
			String method, Object o) {
		this.returnFunction = returnFunction;
		this.mc = m;
		this.global = mg;
		this.method = method;
		this.objectToAdd = o;
		this.last = true;
		this.first = true;
		token = global.getToken(m);
	}

	protected void onPreExecute() {
		if (!isNetworkAvailable()) {
			cancel(true);
			glob.bkToNav(mc, mc.getString(R.string.no_net));
		} else {
			if (this.first) {
				Log.d("rays", "method: " + returnFunction + "->" + last);
				showProg();
			}
		}
	}

	// Call after onPreExecute returnFunction
	protected Void doInBackground(String... urls) {

		try {

			URL url = new URL(urls[0]);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("auth_token", token);

			if (this.method.equals("Upload")) {
				conn.setRequestMethod("POST");
			}

			if (this.method.equals("Upload")) {
				Product p = (Product) this.objectToAdd;
				Content = uploadProduct(p, url, token);
			}
		} catch (Exception ex) {
			Error = ex.getLocalizedMessage();
			ex.printStackTrace();
			Log.d("ray", "ray err:" + Error);
		}
		return null;
	}

	protected void onPostExecute(Void unused) {
		Log.d("raya", "post: " + returnFunction + ": " + last + ": "
				+ global.loader.isShowing());
		if (global.loader != null && last) {
			global.loader.dismiss();
			Log.d("raya", "dismissing: ");
		}
		try {
			if (Content == null)
				Content = "";
			if (Error != null) {
				Log.d("ray", "ray error: " + Error);
				glob.bkToNav(mc, getError(Content, Error));
			}
			Method returnFunction = this.mc.getClass()
					.getMethod(this.returnFunction, Content.getClass(),
							Content.getClass());
			returnFunction.invoke(this.mc, Content, Error);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean getfirst() {
		return first;
	}

	public void setfirst(boolean first) {
		this.first = first;
	}

	public DeliveryAdminApplication getGlobal() {
		return global;
	}

	public void setGlobal(DeliveryAdminApplication global) {
		this.global = global;
	}

	@SuppressWarnings("rawtypes")
	private String uploadProduct(Product p, URL url, String token)
			throws Exception {

		String USER_AGENT = "Mozilla/5.0";
		String boundary = "*****";
		String iFileName = "";
		String fileUrl = "";
		if (p.getPhoto() != null) {
			iFileName = p.getPhoto().getName();
			fileUrl = p.getPhoto().getUrl();
		}
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		URL obj = url;
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("auth_token", token);
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Accept-Language", "en-us;q=0.8");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Content-Type",
				"multipart/form-data; charset=UTF-8;boundary=" + boundary);
		// con.setCharacterEncoding("UTF-8");

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());

		Map<String, String> paramsVal = new HashMap<String, String>();
		paramsVal.put("category_id", "" + p.getCategory().getId());
		paramsVal.put("name", p.getName());
		paramsVal.put("description", p.getDescription());
		paramsVal.put("branch_id", "" + p.getBranchId());
		paramsVal.put("price", "" + p.getPrice());
		paramsVal.put("unit_id", "" + p.getUnit().getId());
		if (p.getPhoto() != null)
			paramsVal.put("photo_name", "" + iFileName);
		Iterator iterator = paramsVal.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\""
					+ mapEntry.getKey() + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			if (mapEntry.getKey().equals("name")
					|| mapEntry.getKey().equals("description")) {
				dos.writeUTF(mapEntry.getValue().toString());
			} else
				dos.writeBytes(mapEntry.getValue().toString());

			dos.writeBytes(lineEnd);
		}
		System.out.println("sending: " + dos.toString());

		if (p.getPhoto() != null) {
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\""
					+ iFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			FileInputStream fileInputStream = new FileInputStream(fileUrl);
			int bytesAvailable = fileInputStream.available();

			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// read file and write it into form...
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			System.out.println("\nRay:" + buffer);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			dos.writeBytes(lineEnd);
			dos.writeBytes("\r\n--" + boundary + "--\r\n");
			// close streams
			fileInputStream.close();
		} else
			dos.writeBytes("\r\n--" + boundary + "--\r\n");
		dos.flush();

		dos.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode + "->"
				+ con.getResponseMessage());

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		return response.toString();

	}

	public void showProg() {
		Handler h;
		Runnable r;
		h = new Handler();
		global.loader = new TransparentProgressDialog(mc, R.drawable.spinner);
		r = new Runnable() {
			@Override
			public void run() {
				if (global.loader.isShowing()) {
					global.loader.dismiss();
				}
			}
		};
		global.loader.show();
		h.postDelayed(r, 10000);
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
		if (last && global.loader != null)
			global.loader.dismiss();
	}

	public String getError(String cont, String Error) {
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(cont);
			if (jsonResponse.has("error"))
				return jsonResponse.optString("error").toString();
			else
				return cont;
		} catch (JSONException e) {
			return cont;
		}
	}

}
