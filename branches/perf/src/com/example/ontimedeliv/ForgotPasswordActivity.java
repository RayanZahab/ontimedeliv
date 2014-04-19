package com.example.ontimedeliv;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity {

	private EditText username;
	boolean isChecked = false;
	ProgressDialog Dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		Dialog = new ProgressDialog(ForgotPasswordActivity.this);
		Dialog.setCancelable(false);
	}

	public void reset(View view) {
		username = (EditText) findViewById(R.id.user_name);
		sendSMS(username.getText().toString(), "Your password is now 123");

	}

	private void sendSMS(String phoneNumber, String message) {
		TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		Toast.makeText(getApplicationContext(), "Phone: " + mPhoneNumber,
				Toast.LENGTH_SHORT).show();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage("+96170908498", null, message, null, null);
	}
	private void sendEmail()
	{
		username = (EditText) findViewById(R.id.user_name);
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{username.getText().toString()});		  
		email.putExtra(Intent.EXTRA_SUBJECT, "Reset Passwod");
		email.putExtra(Intent.EXTRA_TEXT, "message");
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
}
