package com.ontimedeliv;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class RZHelper {
	private static AjaxCallback<JSONObject> callBack;
	private Activity currentActivity;
	private String returnMethod;
	private String url;
	private AQuery myAQuery;
	GlobalM glob = new GlobalM();
	protected TransparentProgressDialog loader;
	private boolean silent = false;

	RZHelper(String myurl, Activity a, String method, boolean isSilent) {
		url = myurl;
		currentActivity = a;
		returnMethod = method;
		silent = isSilent;
		myAQuery = new AQuery(currentActivity);
		Log.d("RZ","URL: "+url+"->"+returnMethod);
		if(!isNetworkAvailable())
		{
			Toast t = Toast.makeText(myAQuery.getContext(), "Errorss: " + currentActivity.getString(R.string.no_net),
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		}
		else
		{
			if (!silent) {
				loader = new TransparentProgressDialog(currentActivity,
						R.drawable.spinner);
			}
	
			callBack = new AjaxCallback<JSONObject>() {
	
				@Override
				public void callback(String url, JSONObject html, AjaxStatus status) {
					String reply = "", error = null, stringType = "";
					if (html != null)
						reply = html.toString();
					if (status != null)
						error = status.getError();
					Log.d("RZ","UR:"+url+" REPLY: "+reply+"->Error: "+error);
					if (error != null) {
						Toast.makeText(myAQuery.getContext(), "Error2: " + error,
								Toast.LENGTH_LONG).show();
						
					} else {
						Method returnFunction;
						try {
							returnFunction = currentActivity.getClass().getMethod(
									returnMethod, stringType.getClass(),
									stringType.getClass());
							returnFunction.invoke(currentActivity, reply, error);
							Log.d("ray callback", url + ": " + "error: " + error
									+ " => reply: " + reply);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			callBack.header("Accept", "application/json");
			callBack.header("Accept-Encoding", "gzip");
			callBack.header("Cache-Control", "max-stale=0,max-age=60");
			callBack.header("Content-Type", "application/json; charset=utf-8");
	
			SharedPreferences settings1 = currentActivity.getSharedPreferences(
					"PREFS_NAME", 0);
			String token = settings1.getString("token", "");
			if (!token.isEmpty()) {
				callBack.header("auth_token", token);
				Log.d("ray token: ", "token: " + token);
			}
		}
	}

	public void post(Object obj) {
		JSONObject params = (new APIManager()).objToCreate(obj);
		if (!silent) {
			myAQuery.progress(loader).post(url, params, JSONObject.class,
					callBack);
		} else {
			myAQuery.post(url, params, JSONObject.class, callBack);
		}
	}

	public void get() {
		if (!silent) {
			myAQuery.progress(loader).ajax(url, JSONObject.class, callBack);
		} else {
			myAQuery.ajax(url, JSONObject.class, callBack);
		}

	}

	public void put(Object obj) {
		JSONObject params = (new APIManager()).objToCreate((Object) obj);
		if (!silent) {
			myAQuery.progress(loader).put(url, params, JSONObject.class,
					callBack);
		} else {
			myAQuery.put(url, params, JSONObject.class, callBack);
		}

	}

	public void delete() {
		if (!silent) {
			myAQuery.progress(loader).delete(url, JSONObject.class, callBack);
		} else {
			myAQuery.delete(url, JSONObject.class, callBack);
		}

	}

	public void aync_multipart(Object obj) {
		String USER_AGENT = "Mozilla/5.0";
		String boundary = "*****";
		callBack.header("User-Agent", USER_AGENT);
		callBack.header("Accept-Charset", "utf-8");
		callBack.header("Accept-Language", "en-us;q=0.9");
		callBack.header("Connection", "Keep-Alive");
		callBack.header("Content-Type",
				"multipart/form-data; charset=utf-8;boundary=" + boundary);

		Product p = (Product) obj;
		Map<String, Object> paramsVal = new HashMap<String, Object>();
		paramsVal.put("name", "" + p.getName());
		paramsVal.put("category_id", "" + p.getCategory().getId());
		paramsVal.put("shop_id", "" + p.getShop_id());
		paramsVal.put("price", "" + p.getPrice());
		paramsVal.put("unit_id", "" + p.getUnit().getId());
		String fileUrl = "", iFileName = "";
		paramsVal.put("description", "" + p.getDescription());
		if (p.getPhoto() != null) {
			paramsVal.put("photo_name", "" + iFileName);
			iFileName = p.getPhoto().getName();
			fileUrl = p.getPhoto().getUrl();

			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(fileUrl);

				int bufferSize = fileInputStream.available();

				byte[] buffer = new byte[bufferSize];
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				paramsVal.put("photo", buffer);

				fileInputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("ray error ", "ERROR");
			}
		}
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("message", "Message");

		// Simply put a byte[] to the params, AQuery will detect it and treat it
		// as a multi-part post
		// byte[] data = getImageData();

		// Alternatively, put a File or InputStream instead of byte[]
		// File file = getImageFile();
		// params.put("source", file);

		myAQuery.ajax(url, paramsVal, JSONObject.class, callBack);

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) currentActivity
				.getApplicationContext()
				.getSystemService(
						currentActivity.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}
}
