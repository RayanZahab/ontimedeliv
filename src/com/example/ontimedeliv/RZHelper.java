package com.example.ontimedeliv;

import java.lang.reflect.Method;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class RZHelper {
	private static AjaxCallback<JSONObject> callBack;
	private Activity currentActivity;
	private String returnMethod;
	private String url;
	private static AQuery myAQuery;

	RZHelper(String myurl, Activity a, String method) {
		url = myurl;
		currentActivity = a;
		returnMethod = method;
		myAQuery = new AQuery(currentActivity);
		callBack = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject html, AjaxStatus status) {
				String reply = "", error = null, stringType = "";
				if (html != null)
					reply = html.toString();
				if (status != null)
					error = status.getError();

				Method returnFunction;
				try {
					returnFunction = currentActivity.getClass().getMethod(
							returnMethod, stringType.getClass(),
							stringType.getClass());
					returnFunction.invoke(currentActivity, reply, error);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		}
	}

	public void post(Object obj) {
		JSONObject params = (new APIManager()).objToCreate(obj);
		myAQuery.post(url, params, JSONObject.class, callBack);
	}

	public void get() {
		myAQuery.ajax(url, JSONObject.class, callBack);

	}

	public void put(Object obj) {
		JSONObject params = (new APIManager()).objToCreate((Object) obj);
		myAQuery.put(url, params, JSONObject.class, callBack);

	}
	
	public void delete() {
		//myAQuery.delete(url, JSONObject.class, (Ob, returnMethod);
		myAQuery.delete(url, JSONObject.class, callBack);

	}
}
