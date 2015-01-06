package com.ontimedeliv;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	public HashMap<Integer, String> froms, tos;
	public HashMap<Integer, Boolean> openDays;
	private boolean populate = false;

	@SuppressLint("UseSparseArrays")
	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		froms = new HashMap<Integer, String>();
		tos = new HashMap<Integer, String>();
		openDays = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 7; i++) {
			openDays.put(i, false);
		}
	}

	@SuppressLint("UseSparseArrays")
	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData, OpenHours oh) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		if (oh != null) {
			froms = oh.froms;
			tos = oh.tos;
			openDays = oh.openDays;
			populate = true;
		} else {
			openDays = new HashMap<Integer, Boolean>();
			for (int i = 0; i < 7; i++) {
				openDays.put(i, false);
			}
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildAt(int position) {
		return getChildView(0, position, false, null, null);
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		CheckBox chbListChild = (CheckBox) convertView
				.findViewById(R.id.weekCheck);

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.weekCheckTxt);
		chbListChild.setOnCheckedChangeListener(new addCheckListener(
				childPosition));

		final Button from = (Button) convertView.findViewById(R.id.fromBtnn);
		from.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				TimePickerDialog tpd = new TimePickerDialog(_context,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								int hour = hourOfDay;
								NumberFormat f = new DecimalFormat("00");
								String format = "";
								if (hourOfDay == 0) {
									hourOfDay += 12;
									format = "AM";
								} else if (hourOfDay == 12) {
									format = "PM";
								} else if (hourOfDay > 12) {
									hourOfDay -= 12;
									format = "PM";
								} else {
									format = "AM";
								}

								from.setText(f.format(hourOfDay) + ":"
										+ f.format(minute) + " " + format);
								froms.put(childPosition, hour + "." + minute);
							}
						}, 0, 0, false);
				tpd.show();
			}
		});

		final Button to = (Button) convertView.findViewById(R.id.toBtn);
		to.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TimePickerDialog tpd = new TimePickerDialog(_context,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								int hour = hourOfDay;
								NumberFormat f = new DecimalFormat("00");
								String format = "";
								if (hourOfDay == 0) {
									hourOfDay += 12;
									format = "AM";
								} else if (hourOfDay == 12) {
									format = "PM";
								} else if (hourOfDay > 12) {
									hourOfDay -= 12;
									format = "PM";
								} else {
									format = "AM";
								}
								to.setText(f.format(hourOfDay) + ":"
										+ f.format(minute) + " " + format);
								tos.put(childPosition, hour + "" + minute);
							}
						}, 0, 0, false);
				tpd.show();
			}
		});
		if (populate) {
			if (froms.get(childPosition) != null)
				from.setText(froms.get(childPosition).replace(".", ":"));
			if (tos.get(childPosition) != null)
				to.setText(tos.get(childPosition).replace(".", ":"));
			chbListChild.setChecked(openDays.get(childPosition));
		}
		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class addCheckListener implements OnCheckedChangeListener {
		int position;

		public addCheckListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			openDays.put(position, isChecked);
		}

	}
}
