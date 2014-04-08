package com.example.ontimedeliv;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

public class ValidationError {
	private boolean valid = false;
	private String errorMsg = "";

	public ValidationError(boolean is, String msg) {
		setError(is);
		setErrorMsg(msg);
	}

	public boolean isValid() {
		return valid;
	}

	public void setError(boolean valid) {
		this.valid = valid;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void showError(Activity mc) {
		Toast ts = Toast.makeText(mc, errorMsg, Toast.LENGTH_SHORT);
		ts.setGravity(Gravity.TOP, 0, 0);
		ts.show();
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
