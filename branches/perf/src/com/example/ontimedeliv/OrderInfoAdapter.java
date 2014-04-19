package com.example.ontimedeliv;

import java.util.ArrayList;





import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderInfoAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> orderList;
	Context context;
	View convertView;
	int adapterView;
	private TextView totalTxt;
	private boolean old=false;

	public OrderInfoAdapter(Context context, int adapterView,
			ArrayList<Item> navList) {
		super(context, adapterView, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;
		this.adapterView = adapterView;
	}

	public OrderInfoAdapter(Context context, int adapterView,
			ArrayList<Item> navList, boolean old) {
		super(context, adapterView, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;
		this.adapterView = adapterView;
		this.old = old;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		OldViewHolder oldHolder = null;

		Log.v("ConvertView", String.valueOf(position));
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(adapterView, null);

			if(!old)
			{				
				holder = new ViewHolder();
				if (this.orderList.get(position).isNew) {
					RelativeLayout main = (RelativeLayout) convertView
							.findViewById(R.id.roworder);
					main.setBackgroundColor(Color.parseColor("#FF9999"));
				}
				holder.id = this.orderList.get(position).getId();
				holder.quantity = (EditText) convertView
						.findViewById(R.id.quantity);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.itemname = (CheckBox) convertView
						.findViewById(R.id.itemname);
				holder.quantity.addTextChangedListener(new addListenerOnTextChange(context, holder));
				holder.itemname.setOnCheckedChangeListener(new addCheckListener(context,holder));
				convertView.setTag(holder);
			}
			else
			{
				oldHolder = new OldViewHolder();	
				oldHolder.quantity = (TextView) convertView
						.findViewById(R.id.quantity);
				oldHolder.price = (TextView) convertView.findViewById(R.id.price);
				oldHolder.itemname = (TextView) convertView
						.findViewById(R.id.itemname);
				convertView.setTag(oldHolder);
			}
			

		} else {
			if(!old)
			{
				holder = (ViewHolder) convertView.getTag();
			}
			else
			{
				oldHolder = (OldViewHolder) convertView.getTag();
			}
			this.convertView = convertView;
		}

		Item orderitem = orderList.get(position);
		if(!old)
		{
			holder.itemname.setText(orderitem.getTitle());
			holder.quantity.setText("" + orderitem.getQuantity());
			holder.price.setText("" + orderitem.getPrice()*orderitem.getQuantity());			
			holder.unitPrice=orderitem.getPrice();
		}
		else
		{
			oldHolder.itemname.setText(orderitem.getTitle());
			oldHolder.quantity.setText("" + orderitem.getQuantity());
			oldHolder.price.setText("" + orderitem.getPrice()*orderitem.getQuantity());
			oldHolder.unitPrice=orderitem.getPrice();
			oldHolder.unitPrice=orderitem.getPrice();
		}
		return convertView;
	}
	

	public TextView getTotal() {
		return totalTxt;
	}

	public void setTotal(TextView total) {
		this.totalTxt = total;
	}
	class ViewHolder {
		int id;
		CheckBox itemname;
		TextView price;
		EditText quantity;
		double unitPrice;
	}
	class OldViewHolder {
		TextView price, itemname,quantity;
		double unitPrice;
	}
	class addCheckListener implements OnCheckedChangeListener{
		private Context mContext;	
		ViewHolder holder;
		public addCheckListener(Context context, ViewHolder holder) {
			super();
			this.mContext = context;
			this.holder=holder;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!buttonView.isChecked()) { 
				holder.quantity.setText("0");
			} 
			
		}
		
	}

	class addListenerOnTextChange implements TextWatcher {
		private Context mContext;
		EditText mEdittextview;
		double oldValue=0;
		ViewHolder holder;
		ViewHolder oldHolder;

		public addListenerOnTextChange(Context context, ViewHolder holder) {
			super();
			this.mContext = context;
			this.mEdittextview = holder.quantity;
			this.holder=holder;
		}

		@Override
		public void afterTextChanged(Editable s) {
			double newPrice;
			if(!s.toString().isEmpty())
			{
				newPrice = holder.unitPrice*Integer.parseInt(s.toString());
				this.holder.price.setText(""+newPrice);
			}
			else
			{
				this.holder.price.setText("0");
				newPrice=0;
			}

			double total=Double.parseDouble(totalTxt.getText().toString())+newPrice;
			totalTxt.setText(""+total);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			double total=Double.parseDouble(totalTxt.getText().toString());
			if(!s.toString().isEmpty())
			{
				this.oldValue =Double.parseDouble(holder.price.getText().toString());
				total=total-this.oldValue;
			}
			else
			{
				this.oldValue = 0;
			}
			totalTxt.setText(""+total);			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// What you want to do
		}


	}
}

