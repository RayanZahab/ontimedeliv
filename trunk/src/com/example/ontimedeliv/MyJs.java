package com.example.ontimedeliv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
	private Object objectToAdd;

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			String method) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.method = method;
	}

	public MyJs(ProgressDialog dialog2, String returnFunction, Activity m,
			String method, Object o) {
		this.returnFunction = returnFunction;
		this.Dialog = dialog2;
		this.mc = m;
		this.method = method;
		this.objectToAdd = o;
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
			if(this.method=="Upload")
				conn.setRequestMethod("POST");
			else{
				conn.setRequestMethod(this.method);
				conn.setRequestProperty("Content-Type",
					"application/json; charset=utf-8");
			}
			conn.addRequestProperty("auth_token", "abaed8959dedfccc79b5");
			// Get the server response
			if (this.method.equals("GET")) {
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

				JSONObject jsonObjSend = (new APIManager()).objToCreate(this.objectToAdd);
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
			}else if (this.method.equals("Upload"))
			{
				Product p = (Product) this.objectToAdd;
				String path =p.getPhoto().getUrl();
				FileInputStream fileInputStream = new FileInputStream(path);
                // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
       
                //dos.writeBytes(twoHyphens + boundary + lineEnd); 
                String formData = ""
                  		+ " --form 'name="+p.getName()+"' --form 'shop_id="+p.getShop_id()+"' --form 'category_id="+p.getCategory().getId()+
                  		"' --form 'price="+p.getPrice()+"' --form 'unit_id="+p.getUnit().getId()+"' --form 'description="+p.getDescription()
                  		+ "' --form 'photo="+path+"'";
                Log.d("rays","upload"+formData);
                dos.writeBytes(formData);
                //--form name="uploaded_file";filename=" + fileName + "" + lineEnd);
                //dos.writeBytes(lineEnd);
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024; 
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available(); 
       
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
       
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                   
                while (bytesRead > 0) {
                     
                  dos.write(buffer, 0, bufferSize);
                  bytesAvailable = fileInputStream.available();
                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                   
                 }
       
                // send multipart form data necesssary after file data...
               // dos.writeBytes(lineEnd);
               // dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
       
                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                  
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                fileInputStream.close();
                dos.flush();
                dos.close();
                 
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
