package com.example.ontimedeliv;

public class ValidationError {
	private boolean valid =false;
	private String errorMsg = "";
	public ValidationError(boolean is, String msg)
	{
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
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
