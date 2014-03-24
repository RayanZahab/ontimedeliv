package com.example.ontimedeliv;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
	boolean secondMethod = false;
	private Object objectToAdd;
	String token;
	private ontimedeliv global;

	private void MyJs() {
		try {
			token = global.getToken();
		} catch (Exception exp) {			
			Log.d("exep","excep"+exp.toString());
		}
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mc.getApplicationContext().getSystemService(mc.getApplicationContext().CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			ontimedeliv mg, String method) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.global = mg;
		this.method = method;
		MyJs();
	}

	public void timerDelayRemoveDialog(long time, final ProgressDialog d) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				d.dismiss();
			}
		}, time);
	}

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			ontimedeliv mg, String method, boolean sm) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.global = mg;
		this.method = method;
		this.secondMethod = sm;
		MyJs();
	}

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			ontimedeliv mg, String method, Object o, boolean secondMethod) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.global = mg;
		this.method = method;
		this.objectToAdd = o;
		this.setSecondMethod(secondMethod);
		MyJs();
	}

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			ontimedeliv mg, String method, Object o) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.global = mg;
		this.method = method;
		this.objectToAdd = o;
		Log.d("shi", "shi: " + mg.toString());
		MyJs();
	}

	protected void onPreExecute() {
		if(!isNetworkAvailable())
		{
			cancel(true);
			new GlobalM().bkToNav(mc);
		}
		else{
			Dialog.setMessage(mc.getApplicationContext().getString(R.string.loding));
			if (!this.secondMethod) {
				Dialog.show();
				timerDelayRemoveDialog(10000, Dialog);
			}
		}
	}

	// Call after onPreExecute returnFunction
	protected Void doInBackground(String... urls) {

		/************ Make Post Call To Web Server ***********/
		BufferedReader reader = null;

		try {

			URL url = new URL(urls[0]);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.addRequestProperty("auth_token", token);

			if (this.method.equals("Upload")) {
				conn.setRequestMethod("POST");
			} else {
				conn.setRequestMethod(this.method);
				conn.setRequestProperty("Content-Type",
						"application/json; charset=utf-8");
			}

			if (this.method.equals("GET")) {
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				Content = sb.toString();
				Error = null;
			} else if (this.method.equals("POST")) {
				conn.addRequestProperty("Accept", "application/json");
				conn.addRequestProperty("Accept-Encoding", "gzip");
				conn.addRequestProperty("Cache-Control",
						"max-stale=0,max-age=60");
				conn.setDoOutput(true);
				conn.setDoInput(true);

				JSONObject jsonObjSend = (new APIManager())
						.objToCreate(this.objectToAdd);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				Log.d("ray", "ray writing: " + jsonObjSend.toString());
				wr.write(jsonObjSend.toString());
				wr.flush();
				wr.close();

				if (conn.getResponseCode() != 201
						&& conn.getResponseCode() != 200) {
					Log.d("ray",
							"Failed: " + url + "\n" + conn.getResponseMessage());
					Content = conn.getResponseMessage();
					Error = conn.getResponseMessage();
				} else {
					BufferedReader responseContent = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;

					while ((line = responseContent.readLine()) != null) {
						sb.append(line + "\n");
					}
					Content = sb.toString();
					Error = null;
				}
			} else if (this.method.equals("PUT")) {
				conn.addRequestProperty("Accept", "application/json");
				conn.addRequestProperty("Accept-Encoding", "gzip");
				conn.addRequestProperty("Cache-Control",
						"max-stale=0,max-age=60");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				Log.d("ray", "ray req: "
						+ conn.getRequestProperties().toString());
				JSONObject jsonObjSend = (new APIManager())
						.objToCreate(this.objectToAdd);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				Log.d("ray", "ray writing: " + jsonObjSend.toString());
				wr.write(jsonObjSend.toString());
				wr.flush();
				wr.close();

				if (conn.getResponseCode() != 200) {
					Log.d("ray",
							"Failed: " + url + "\n" + conn.getResponseMessage());
					Content = null;
					Error = conn.getResponseMessage();
				} else {
					Content = "done";
					Error = null;
				}

			} else if (this.method.equals("DELETE")) {
				conn.addRequestProperty("Accept", "application/json");
				conn.addRequestProperty("Accept-Encoding", "gzip");
				conn.addRequestProperty("Cache-Control",
						"max-stale=0,max-age=60");
				if (conn.getResponseCode() != 204) {
					Log.d("ray",
							"Failed: " + url + "\n" + conn.getResponseMessage());
					Content = null;
					Error = conn.getResponseMessage();
				} else {
					Content = "done";
					Error = null;
				}
			} else if (this.method.equals("Upload")) {				
				Product p = (Product) this.objectToAdd;
				Content = uploadProduct(p);
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
		Log.d("ray", "ray url: " + urls[0]);
		/*****************************************************/
		return null;
	}

	protected void onPostExecute(Void unused) {
		if (!this.secondMethod)
			Dialog.dismiss();
		try {
			if (Content == null)
				Content = "";
			Method returnFunction = this.mc.getClass()
					.getMethod(this.returnFunction, Content.getClass(),
							Content.getClass());
			returnFunction.invoke(this.mc, Content, Error);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ray", "Ray excep: " + e.getMessage());

		}

	}

	public boolean getSecondMethod() {
		return secondMethod;
	}

	public void setSecondMethod(boolean secondMethod) {
		this.secondMethod = secondMethod;
	}

	public ontimedeliv getGlobal() {
		return global;
	}

	public void setGlobal(ontimedeliv global) {
		this.global = global;
	}
	
	private String uploadProduct(Product p) throws Exception {
		
		String USER_AGENT = "Mozilla/5.0";
        String boundary = "*****";
        String iFileName = "";
        String fileUrl = "";
        if(p.getPhoto()!=null)
        {
        	iFileName = p.getPhoto().getName();
        	fileUrl = p.getPhoto().getUrl();
        }
        String lineEnd = "\r\n";
         String twoHyphens = "--";
        String url = "http://enigmatic-springs-5176.herokuapp.com/api/v1/items";
        //String url = "http://localhost:3000/api/v1/items";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("auth_token", token);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
 
        
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
       
        Map<String,String> paramsVal = new HashMap<String,String>();
        paramsVal.put("name", ""+p.getName());
        paramsVal.put("category_id",""+p.getCategory().getId());
        paramsVal.put("shop_id", ""+p.getShop_id());
        paramsVal.put("price", ""+p.getPrice());
        paramsVal.put("unit_id", ""+p.getUnit().getId());
        paramsVal.put("photo_name", ""+iFileName);
        paramsVal.put("description",""+ p.getDescription());
        Iterator iterator = paramsVal.entrySet().iterator();
        while (iterator.hasNext()) {
        	Map.Entry mapEntry = (Map.Entry) iterator.next();

    		dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\""+mapEntry.getKey()+"\""+ lineEnd);
            System.out.println("\n Content-Disposition: form-data; name=\""+mapEntry.getKey()+"\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(mapEntry.getValue().toString());
            System.out.println("\n :"+mapEntry.getValue().toString());
            dos.writeBytes(lineEnd);
    	}
        
        
        if(p.getPhoto()!=null)
        {
        	dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + iFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);
	        FileInputStream fileInputStream = new FileInputStream(fileUrl);
	        int bytesAvailable = fileInputStream.available();
	            
	        int maxBufferSize = 1024;
	        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        byte[ ] buffer = new byte[bufferSize];
	
	        // read file and write it into form...
	        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	        System.out.println("\nRay:"+buffer);
	
	        while (bytesRead > 0)
	        {
	                dos.write(buffer, 0, bufferSize);
	                bytesAvailable = fileInputStream.available();
	                bufferSize = Math.min(bytesAvailable,maxBufferSize);
	                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
	        }
	        dos.writeBytes(lineEnd);
	        dos.writeBytes( "\r\n--" + boundary + "--\r\n");
	        // close streams
	        fileInputStream.close();
        }
        dos.flush();

        dos.close();
 
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"->"+con.getResponseMessage());
 
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
 
        //print result
        System.out.println(response.toString());
        return response.toString();
 
    }

}
