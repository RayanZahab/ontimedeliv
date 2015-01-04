package com.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderInfoAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> orderList;
	Context context;
	View convertView;
	int adapterView;
	private TextView totalTxt;
	private boolean old = false;
	double myTotal = 0;

	public boolean isOld() {
		return this.old;
	}

	public void setOld(boolean old) {
		this.old = old;
	}

	public OrderInfoAdapter(Context context, int adapterView,
			ArrayList<Item> navList) {
		super(context, adapterView, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;
		this.adapterView = adapterView;
		myTotal = 0;
	}

	public OrderInfoAdapter(Context context, int adapterView,
			ArrayList<Item> navList, boolean old) {
		super(context, adapterView, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;
		this.adapterView = adapterView;
		this.old = old;
		myTotal = 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		OldViewHolder oldHolder = null;

		Log.d("ConvertView", old + "");

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (position == 0)
				myTotal = 0;

			if (!old) {
				convertView = vi.inflate(R.layout.row_order_info, null);
				holder = new ViewHolder();
				if (this.orderList.get(position).isNew) {
					RelativeLayout main = (RelativeLayout) convertView
							.findViewById(R.id.roworder);
					main.setBackgroundColor(Color.parseColor("#FF9999"));
				}
				holder.id = this.orderList.get(position).getId();
				holder.quantity = (TextView) convertView
						.findViewById(R.id.quantity);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.unit = (TextView) convertView.findViewById(R.id.unit);
				holder.itemname = (TextView) convertView
						.findViewById(R.id.itemname);
				holder.plus = (ImageView) convertView.findViewById(R.id.plus);
				holder.minus = (ImageView) convertView.findViewById(R.id.minus);
				holder.plus
						.setOnClickListener(new PlusListener(context, holder));
				holder.minus.setOnClickListener(new MinusListener(context,
						holder));

				holder.quantity
						.addTextChangedListener(new addListenerOnTextChange(
								context, holder));
				// holder.itemname
				// .setOnCheckedChangeListener(new addCheckListener(
				// context, holder));
				convertView.setTag(holder);
			} else {
				convertView = vi.inflate(R.layout.row_old_order_info, null);
				oldHolder = new OldViewHolder();
				oldHolder.quantity = (TextView) convertView
						.findViewById(R.id.quantity);
				oldHolder.price = (TextView) convertView
						.findViewById(R.id.price);
				oldHolder.unit = (TextView) convertView.findViewById(R.id.unit);
				oldHolder.itemname = (TextView) convertView
						.findViewById(R.id.itemname);
				convertView.setTag(oldHolder);
			}

		} else {
			if (!old) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				oldHolder = (OldViewHolder) convertView.getTag();
			}
			this.convertView = convertView;
		}

		Item orderitem = orderList.get(position);
		if (!old) {
			holder.itemname.setText(orderitem.getTitle());
			holder.quantity.setText("" + orderitem.getQuantity());
			holder.price.setText("" + orderitem.getPrice()
					* orderitem.getQuantity());
			holder.unitPrice = orderitem.getPrice();
			holder.unit.setText(holder.unitPrice + "");
			myTotal = myTotal
					+ Converter.toDouble(holder.price.getText().toString());
			totalTxt.setText("" + myTotal);

			Log.d("Ray", "Total: " + myTotal);
		} else {
			oldHolder.itemname.setText(orderitem.getTitle());
			oldHolder.quantity.setText("" + orderitem.getQuantity());
			oldHolder.price.setText("" + orderitem.getPrice()
					* orderitem.getQuantity());
			oldHolder.unitPrice = orderitem.getPrice();
			oldHolder.unit.setText(oldHolder.unitPrice + "");
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
		TextView itemname;
		TextView price, unit;
		TextView quantity;
		double unitPrice;
		ImageView plus, minus;
	}

	class OldViewHolder {
		TextView price, itemname, quantity, unit;
		double unitPrice;
	}

	class addCheckListener implements OnCheckedChangeListener {
		private Context mContext;
		ViewHolder holder;

		public addCheckListener(Context context, ViewHolder holder) {
			super();
			this.setmContext(context);
			this.holder = holder;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!buttonView.isChecked()) {
				holder.quantity.setText("0");
			}

		}

		public Context getmContext() {
			return mContext;
		}

		public void setmContext(Context mContext) {
			this.mContext = mContext;
		}

	}

	class PlusListener implements OnClickListener {
		Context mContext;
		TextView mEdittextview;
		double oldValue = 0;
		ViewHolder holder;
		ViewHolder oldHolder;

		public PlusListener(Context context, ViewHolder holder) {
			super();
			this.mContext = context;
			this.mEdittextview = holder.quantity;
			this.holder = holder;
		}

		@Override
		public void onClick(View arg0) {
			int oldQuantity = Converter.toInt(holder.quantity.getText()
					.toString());
			if (oldQuantity < 10) {
				int newQuantity = oldQuantity + 1;
				double oldPrice = Converter.toDouble(holder.price.getText()
						.toString());

				double newPrice = holder.unitPrice * newQuantity;
				this.holder.price.setText("" + newPrice);
				holder.quantity.setText(newQuantity + "");
				updateTotal(oldPrice, newPrice);
			}
		}
	}

	public void updateTotal(double oldP, double newP) {
		double p = Converter.toDouble(totalTxt.getText().toString());
		p = p - oldP + newP;
		totalTxt.setText(p + "");
	}

	class MinusListener implements OnClickListener {
		private Context mContext;
		TextView mEdittextview;
		double oldValue = 0;
		ViewHolder holder;
		ViewHolder oldHolder;

		public MinusListener(Context context, ViewHolder holder) {
			super();
			this.setmContext(context);
			this.mEdittextview = holder.quantity;
			this.holder = holder;
		}

		@Override
		public void onClick(View arg0) {
			int oldQuantity = Converter.toInt(holder.quantity.getText()
					.toString());
			if (oldQuantity > 0) {
				int newQuantity = oldQuantity - 1;
				double oldPrice = Converter.toDouble(holder.price.getText()
						.toString());
				double newPrice = holder.unitPrice * newQuantity;
				this.holder.price.setText("" + newPrice);
				holder.quantity.setText(newQuantity + "");
				updateTotal(oldPrice, newPrice);
			}
		}

		public Context getmContext() {
			return mContext;
		}

		public void setmContext(Context mContext) {
			this.mContext = mContext;
		}

	}

	class addListenerOnTextChange implements TextWatcher {
		private Context mContext;
		TextView mEdittextview;
		double oldValue = 0;
		ViewHolder holder;
		ViewHolder oldHolder;

		public addListenerOnTextChange(Context context, ViewHolder holder) {
			super();
			this.setmContext(context);
			this.mEdittextview = holder.quantity;
			this.holder = holder;
		}

		@Override
		public void afterTextChanged(Editable s) {
			double newPrice;
			if (!s.toString().isEmpty()) {
				newPrice = holder.unitPrice * Converter.toInt(s.toString());
				this.holder.price.setText("" + newPrice);
			} else {
				this.holder.price.setText("0");
				newPrice = 0;
			}

			double total = Converter.toDouble(totalTxt.getText().toString())
					+ newPrice;
			totalTxt.setText("" + total);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			double total = Converter.toDouble(totalTxt.getText().toString());
			if (!s.toString().isEmpty()) {
				this.oldValue = Converter.toDouble(holder.price.getText()
						.toString());
				total = total - this.oldValue;
			} else {
				this.oldValue = 0;
			}
			totalTxt.setText("" + total);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// What you want to do
		}

		public Context getmContext() {
			return mContext;
		}

		public void setmContext(Context mContext) {
			this.mContext = mContext;
		}

	}
}
