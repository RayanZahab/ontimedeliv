package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.adapter.OrderInfoAdapter;
import com.mobilife.delivery.admin.model.Item;
import com.mobilife.delivery.admin.model.Order;
import com.mobilife.delivery.admin.model.OrderItem;
import com.mobilife.delivery.admin.model.OrderStatus;
import com.mobilife.delivery.admin.model.Product;
import com.mobilife.delivery.admin.model.User;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.Converter;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.Helper;
import com.mobilife.delivery.admin.utilities.PreferenecesManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class OrderInfoActivity extends Activity {
	Spinner status;
	Button cancel, submit;
	OrderInfoAdapter dataAdapter;
	int orderId;
	AlertDialog alertDialog;
	Order currentOrder;
	GlobalM glob = new GlobalM();
	ArrayList<OrderItem> orderitem;
	ArrayList<Item> SPitems;
	ListView listView;
	EditText notes;
	Boolean isAdmin = true, isPreparer = true, disabled = false;
	ArrayList<String> statusValueList;
	private TextView statusLbl;
	private User currentUser;
	private Button backBtn, prepareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);
		status = (Spinner) findViewById(R.id.order_status);
		status.setVisibility(View.GONE);
		statusLbl =  (TextView) findViewById(R.id.order_status_lable);
		statusLbl.setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.listView);
		notes = (EditText) findViewById(R.id.noteinput);
		notes.setEnabled(false);
		statusValueList = new ArrayList<String>();
		statusValueList.add(0, OrderStatus.Opened.name());
		statusValueList.add(1, OrderStatus.Prepared.name());
		statusValueList.add(2, OrderStatus.Closed.name());
		isAdmin = DeliveryAdminApplication.isAdmin(this);
		isPreparer = DeliveryAdminApplication.isPrep(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		DeliveryAdminApplication.clear("order");
		this.orderId = DeliveryAdminApplication.getOrderId(this);

		if (orderId != 0) {
			getCurrentOrder(orderId);
			submit = (Button) findViewById(R.id.close);
		//	submit.setText("Update");
		}

		String orderStatus = DeliveryAdminApplication.getOrderStatus(this);
		actionBar.setTitle(orderStatus);
		prepareBtn= (Button) findViewById(R.id.prepare);
		
		backBtn = (Button) findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		currentUser = PreferenecesManager.getInstance().getUserFromPreferences(this);
		if (isAdmin || currentUser.isSuperAdmin()) {
			if (orderStatus.equalsIgnoreCase(OrderStatus.Closed.name()) || orderStatus.equalsIgnoreCase(OrderStatus.Cancelled.name()))
				disable(true);
			
		} else {
			disable(false);			
		}
		
		
	}

	public void cancel(View v) {
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.prompt_cancel, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				OrderInfoActivity.this);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editText1);
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", null)
				.setNegativeButton("Cancel", null);

		alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button cancel_btn = alertDialog
						.getButton(AlertDialog.BUTTON_NEGATIVE);
				cancel_btn.setBackgroundColor(getResources().getColor(
						R.color.turquoise));
				cancel_btn.setTextColor(getResources().getColor(
						R.color.textview));
				Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setBackgroundColor(getResources().getColor(R.color.turquoise));
				b.setTextColor(getResources().getColor(R.color.textview));
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						if (userInput.getText().toString() != null
								&& !userInput.getText().toString().isEmpty()) {
							Order order = new Order();
							order.setId(orderId);
							order.setCancel(true);
							order.setCancelReason(userInput.getText().toString());
							String serverURL = new myURL("cancel", "orders",
									orderId, 0).getURL();

							RZHelper p = new RZHelper(serverURL,
									OrderInfoActivity.this, "cancelOrder", true);
							p.put(order);

						} else {
							Toast.makeText(getApplicationContext(),
									R.string.cancelreason, Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}
		});

		alertDialog.show();

	}

	public void cancelOrder(String s, String Error) {
		alertDialog.dismiss();
		if (Error == null) {
			glob.bkToNav(OrderInfoActivity.this,
					getString(R.string.ordercanceled));
		}
	}

	public void addItemsOnStatus() {
//		List<String> list = new ArrayList<String>();
//		list.add(getString(R.string.open_status));
//		list.add(getString(R.string.prepared_status));
//		list.add(getString(R.string.closed_status));

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, statusValueList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		status.setAdapter(dataAdapter);
		if (statusValueList.indexOf(currentOrder.getStatus()) > -1)
			status.setSelection(statusValueList.indexOf(currentOrder.getStatus()));
	}

	public void getCurrentOrder(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setOrderInfo", true);
		p.get();
	}

	public void setOrderInfo(String s, String error) {
		currentOrder = new APIManager().getOrder(s);
		addItemsOnStatus();
		orderitem = currentOrder.getOrderItems();
		SPitems = new ArrayList<Item>();
		Item _Item;
		double total = 0;
		TextView totalTxt = (TextView) findViewById(R.id.allTotal);
		if(orderitem!=null && orderitem.size()>0)
			for (int i = 0; i < orderitem.size(); i++) {
				_Item = new Item(orderitem.get(i).getId(), orderitem.get(i)
						.toString(), orderitem.get(i).getQuantity(), orderitem.get(
						i).getUnitPrice());
				SPitems.add(_Item);
				total = total + orderitem.get(i).getTotalPrice();
			}
		dataAdapter = new OrderInfoAdapter(OrderInfoActivity.this,
				R.layout.row_order_info, SPitems, disabled);
		dataAdapter.setTotal(totalTxt);

		listView.setAdapter(dataAdapter);
		new Helper().getListViewSize(listView);
		totalTxt.setText(total + "");
		TextView customerName = (TextView) findViewById(R.id.customerName);
		TextView customerphone = (TextView) findViewById(R.id.customerphone);
		
		customerName.setText(" " + currentOrder.getCustomer().toString());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd.setText(currentOrder.getAddress().toString());
		customerphone.setText(currentOrder.getCustomer().getMobile());
		notes.setText(currentOrder.getNote());
		if(currentOrder.getPreparer()!=null){
			prepareBtn.setEnabled(false);
			prepareBtn.setClickable(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_info, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void close(View view) {
		if (isAdmin) {
			ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
			OrderItem item;
			int quantity;
			View single;
			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				single = listView.getChildAt(i);
				quantity = Converter.toInt(((TextView) single
						.findViewById(R.id.quantity)).getText().toString());
				item = new OrderItem();
				item.setQuantity(quantity);
				item.setId(orderitem.get(i).getProduct().getId());
				item.setProduct(new Product(orderitem.get(i).getProduct()
						.getId()));
				newItems.add(item);
			}
			String serverURL = new myURL(null, "orders", orderId, 0).getURL();

			Order newOrder = new Order();
			newOrder.setId(currentOrder.getId());
			newOrder.setOrderItems(newItems);
			newOrder.setAddress_id(currentOrder.getAddress().getId());
			newOrder.setCustomer_id(currentOrder.getCustomer().getId());
			double total = Double
					.parseDouble(((TextView) findViewById(R.id.allTotal))
							.getText().toString());
			newOrder.setTotal(total);
			if (!newOrder.equals(currentOrder)) {
				RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this,
						"closeOrder", true);
				p.put(newOrder);
			} else
				closeOrder(null, null);
		} else
			assign();
	}

	public void assign() {
		Order newOrder = new Order();
		if (isPreparer)
			newOrder.setStatus(statusValueList.get(1));
		else
			newOrder.setStatus(statusValueList.get(2));
		String serverURL = new myURL("change_status", "orders", orderId + "", 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "done", true);
		p.put(newOrder);
	}
	
	/**
	 * Similar to close function since the user can update item list then click prepare. 
	 */
	public void prepare(View view) {
		
		ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
		OrderItem item;
		int quantity;
		View single;
		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			single = listView.getChildAt(i);
			quantity = Converter.toInt(((TextView) single
					.findViewById(R.id.quantity)).getText().toString());
			item = new OrderItem();
			item.setQuantity(quantity);
			item.setId(orderitem.get(i).getProduct().getId());
			item.setProduct(new Product(orderitem.get(i).getProduct()
					.getId()));
			newItems.add(item);
		}
		String serverURL = new myURL(null, "orders", orderId, 0).getURL();

		Order newOrder = new Order();
		newOrder.setId(currentOrder.getId());
		newOrder.setOrderItems(newItems);
		newOrder.setAddress_id(currentOrder.getAddress().getId());
		newOrder.setCustomer_id(currentOrder.getCustomer().getId());
		double total = Double
				.parseDouble(((TextView) findViewById(R.id.allTotal))
						.getText().toString());
		newOrder.setTotal(total);
		newOrder.setPreparer(currentUser);
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "prepareOrder", true);
		p.put(newOrder);
	}
	
	public void prepareOrder(String s, String error) {
		currentOrder.setPreparer(currentUser);
		currentOrder.setDelivery(currentUser);
		currentOrder.setNote(notes.getText().toString());
		currentOrder.setStatus("assigned");
		String serverURL = new myURL("assign", "orders", orderId + "", 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "done",
				true);
		p.put(currentOrder);
	}
	
	public void closeOrder(String s, String error) {
		Order newOrder = new Order();
		newOrder.setPreparer(currentUser);
		newOrder.setDelivery(currentUser);
		newOrder.setNote(notes.getText().toString());
		newOrder.setStatus(OrderStatus.Closed.name());
		String serverURL = new myURL("assign", "orders", orderId + "", 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "done",
				true);
		p.put(newOrder);
	}

	public void done(String s, String error) {
		Intent i = new Intent(getBaseContext(),OrdersActivity.class);
		startActivity(i);
	}

	public void disable(boolean closed) {
		statusLbl.setVisibility(View.VISIBLE);
		status.setEnabled(false);
		status.setVisibility(View.VISIBLE);
		status.setClickable(false);
		listView.setEnabled(false);
		listView.setClickable(false);
		notes.setEnabled(false);
		notes.setClickable(false);
		cancel = (Button) findViewById(R.id.cancel);
		ViewGroup layout = (ViewGroup) cancel.getParent();
		layout.removeView(cancel);
//		if (isPreparer)
//			submit.setText("Prepared");
//		else if (!isAdmin)
//			submit.setText("Delivered");
		if (closed) {
			((ViewGroup) submit.getParent()).removeView(submit);
			((ViewGroup) prepareBtn.getParent()).removeView(prepareBtn);
			((ViewGroup) backBtn.getParent()).removeView(backBtn);
		}
		disabled = true;
	}
	
}
