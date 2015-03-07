package com.ontimedeliv;

import java.io.File;
import java.lang.reflect.Method;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
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
	private boolean show = false;
	private boolean first = true, last = true;

	RZHelper(String myurl, Activity a, String method, boolean isshow) {
		url = myurl;
		currentActivity = a;
		returnMethod = method;
		show = isshow;
		myAQuery = new AQuery(currentActivity);
		Log.d("RZ", "URL: " + url + "->" + returnMethod);
		if (!isNetworkAvailable()) {
			Toast t = Toast.makeText(myAQuery.getContext(), "Errorss: "
					+ currentActivity.getString(R.string.no_net),
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		} else {
			if (show) {
				loader = new TransparentProgressDialog(currentActivity,
						R.drawable.spinner);
			}

			callBack = new AjaxCallback<JSONObject>() {

				@Override
				public void callback(String url, JSONObject html,
						AjaxStatus status) {
					String reply = "", error = null, stringType = "";
					if (html != null)
						reply = html.toString();
					if (status != null)
						error = status.getError();
					Log.d("RZ", "UR:" + url + " REPLY: " + reply + "->Error: "
							+ error);
					if (error != null) {
						Toast.makeText(myAQuery.getContext(),
								"Error2: " + error, Toast.LENGTH_LONG).show();

					} else {
						Method returnFunction;
						try {
							returnFunction = currentActivity.getClass()
									.getMethod(returnMethod,
											stringType.getClass(),
											stringType.getClass());
							returnFunction
									.invoke(currentActivity, reply, error);
							Log.d("ray callback", url + ": " + "error: "
									+ error + " => reply: " + reply);
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

	RZHelper(String myurl, Activity a, String method, boolean isfirst,
			boolean islast) {
		url = myurl;
		currentActivity = a;
		returnMethod = method;
		first = isfirst;
		last = islast;
		show = false;
		myAQuery = new AQuery(currentActivity);
		Log.d("RZ", "URL: " + url + "->" + returnMethod);
		if (!isNetworkAvailable()) {
			Toast t = Toast.makeText(myAQuery.getContext(), "Errorss: "
					+ currentActivity.getString(R.string.no_net),
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		} else {
			if (first) {
				loader = new TransparentProgressDialog(currentActivity,
						R.drawable.spinner);
				ontimedeliv.loader = loader;
				loader.show();
			}

			callBack = new AjaxCallback<JSONObject>() {

				@Override
				public void callback(String url, JSONObject html,
						AjaxStatus status) {

					String reply = "", error = null, stringType = "";
					if (html != null)
						reply = html.toString();
					if (status != null)
						error = status.getError();
					Log.d("RZ", "UR:" + url + " REPLY: " + reply + "->Error: "
							+ error);
					if (error != null) {
						Toast.makeText(myAQuery.getContext(),
								"Error2: " + error, Toast.LENGTH_LONG).show();

					} else {
						Method returnFunction;
						try {
							returnFunction = currentActivity.getClass()
									.getMethod(returnMethod,
											stringType.getClass(),
											stringType.getClass());
							returnFunction
									.invoke(currentActivity, reply, error);
							Log.d("ray callback", url + ": " + "error: "
									+ error + " => reply: " + reply);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (last && ontimedeliv.loader != null) {
						ontimedeliv.loader.dismiss();
					}
				}
			};

			SharedPreferences settings1 = currentActivity.getSharedPreferences(
					"PREFS_NAME", 0);
			String token = settings1.getString("token", "");
			if (!token.isEmpty()) {
				callBack.header("auth_token", token);
				Log.d("ray token: ", "token: " + token);
			}
		}
	}

	RZHelper(final ImageView imageView, final String myurl, Activity a) {
		url = myurl;
		currentActivity = a;
		show = false;
		myAQuery = new AQuery(currentActivity);

		if (!isNetworkAvailable()) {
			Toast t = Toast.makeText(myAQuery.getContext(), "Errorss: "
					+ currentActivity.getString(R.string.no_net),
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		} else {

			myAQuery.ajax(url, File.class, new AjaxCallback<File>() {

				public void callback(String url, File file, AjaxStatus status) {

					if (file != null) {
						myAQuery.progress(loader).id(imageView.getId())
								.image(myurl, false, false);
					}
				}

			});

		}
	}

	public void post(Object obj) {
		JSONObject params = (new APIManager()).objToCreate(obj);
		Log.d("ray","obj: "+params.toString());
		if (loader != null && show) {
			myAQuery.progress(loader).post(url, params, JSONObject.class,
					callBack);
		} else {
			myAQuery.post(url, params, JSONObject.class, callBack);
		}
	}

	public void get() {
		if (loader != null && show) {
			myAQuery.progress(loader).ajax(url, JSONObject.class, callBack);
		} else {
			myAQuery.ajax(url, JSONObject.class, callBack);
		}

	}

	public void put(Object obj) {
		JSONObject params = (new APIManager()).objToCreate((Object) obj);
		Log.d("ray","obj: "+params.toString());
		if (loader != null && show) {
			myAQuery.progress(loader).put(url, params, JSONObject.class,
					callBack);
		} else {
			myAQuery.put(url, params, JSONObject.class, callBack);
		}

	}

	public void delete() {
		if (loader != null && show) {
			myAQuery.progress(loader).delete(url, JSONObject.class, callBack);
		} else {
			myAQuery.delete(url, JSONObject.class, callBack);
		}

	}

	private boolean isNetworkAvailable() {
		currentActivity.getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) currentActivity
				.getApplicationContext()
				.getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public boolean isshow() {
		return show;
	}

	public void setshow(boolean show) {
		this.show = show;
	}
}
