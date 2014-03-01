package com.example.ontimedeliv;

import android.os.Bundle;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class AddBranchActivity extends Activity {
	
	Button from, to;
	int mHour,mMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_branch);
		
		from = (Button) findViewById(R.id.button2);
		from.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TimePickerDialog tpd = new TimePickerDialog(AddBranchActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
				 
				            @Override
				            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
				            	from.setText(hourOfDay + ":" + minute);
				            }
				        }, mHour, mMinute, false);
				tpd.show();
			
			}
			
	
		});
		
		to = (Button) findViewById(R.id.button1);
		to.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TimePickerDialog tpd = new TimePickerDialog(AddBranchActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
				 
				            @Override
				            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
				            	from.setText(hourOfDay + ":" + minute);
				            }
				        }, mHour, mMinute, false);
				tpd.show();
			
			}
			
	
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_branch, menu);
		return true;
	}
	
	}
