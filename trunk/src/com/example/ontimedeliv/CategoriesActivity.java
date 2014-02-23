package com.example.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);

		// Generate list View from ArrayList
		displayListView();
		checkButtonClick();

	}

	private void displayListView() {

		Bitmap lou7um = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		Bitmap albanajban = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		Bitmap drinks = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		// Array list of Categories
		ArrayList<Item> categories = new ArrayList<Item>();

		Item _Item = new Item(lou7um,"Lou7um");
		categories.add(_Item);
		_Item = new Item(albanajban, "Alban&Ajban");
		categories.add(_Item);
		_Item = new Item(drinks, "Drinks");
		categories.add(_Item);
		

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.category_info, categories);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Item cat = (Item) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on : " + cat.getTitle(), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private class MyCustomAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> catList;

		public MyCustomAdapter(Context context, int textViewResourceId,

		ArrayList<Item> catList) {
			super(context, textViewResourceId, catList);
			this.catList = new ArrayList<Item>();
			this.catList.addAll(catList);
		}

		private class ViewHolder {
			CheckBox name;
			Bitmap picture;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.category_info, null);

				holder = new ViewHolder();
				View v = convertView.findViewById(R.id.item_image);
				v.setDrawingCacheEnabled(true);
				
				v.buildDrawingCache();
				
				Bitmap picture = v.getDrawingCache();
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				holder.picture = picture;

				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Item _cat = (Item) cb.getTag();

						Toast.makeText(
								getApplicationContext(),
								"Checkbox: " + cb.getText() + " -> "
										+ cb.isChecked(), Toast.LENGTH_LONG)
								.show();

						_cat.setSelected(cb.isChecked());
					}
				});

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item cat = catList.get(position);

			
			holder.name.setText(cat.getTitle());
			holder.name.setChecked(cat.isSelected());

			holder.name.setTag(cat);

			return convertView;
		}
	}
	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.submit);

		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				responseText.append("Selected Categories are...\n");
		
				ArrayList<Item> stateList = dataAdapter.catList;
		
				for (int i = 0; i < stateList.size(); i++) {
					Item cat = stateList.get(i);
		
					if (cat.isSelected()) {
						responseText.append("\n" + cat.getTitle());
					}
				}
		
				Toast.makeText(getApplicationContext(), responseText,
						Toast.LENGTH_LONG).show();

			}
		});
	}
}

