package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int branchId, shopId;
	ArrayList<Category> categories;
	ArrayList<Item> categoryItems;
	String url = new myURL("categories", null, 0, 30).getURL();
	DialogInterface dialog;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds = new ArrayList<Integer>();
	Activate myCat;
	ProgressDialog Dialog;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);
		this.Dialog = new ProgressDialog(CategoriesActivity.this);
		Dialog.setCancelable(false);
		if (getIntent().hasExtra("branchId")) {
			Bundle extras = getIntent().getExtras();
			try {
				this.branchId = Integer.parseInt((String) extras
						.getString("branchId"));
				this.shopId = Integer.parseInt((String) extras
						.getString("shopId"));
				url = new myURL("categories", "branches", branchId, 30)
						.getURL();

			} catch (Exception e) {

			}
		}
		getCategories();

	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public void setDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	public void submit(View v) {

		StringBuffer responseText = new StringBuffer();
		responseText.append("Selected Categories are...\n");

		ArrayList<Item> stateList = dataAdapter.getCurrentList();

		for (int i = 0; i < stateList.size(); i++) {
			Item cat = stateList.get(i);
			if (cat.isSelected()) {
				selectedIds.add(cat.getId());
			} else {
				unselectedIds.add(cat.getId());
			}
		}
		myCat = new Activate("categories", unselectedIds);

		Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG)
				.show();

		String serverURL = new myURL("deactivate_categories", "branches",
				branchId, 0).getURL();
		new MyJs(Dialog, "afterDeactivate", this, "PUT", (Object) myCat, true)
				.execute(serverURL);
	}

	public void afterDeactivate(String s, String error) {
		Toast.makeText(getApplicationContext(), "DeActivate : " + s,
				Toast.LENGTH_SHORT).show();
		myCat = new Activate("categories", selectedIds);
		String serverURL = new myURL("activate_categories", "branches",
				branchId, 0).getURL();

		new MyJs(Dialog, "afterActivate", this, "PUT", (Object) myCat)
				.execute(serverURL);
	}

	public void afterActivate(String s, String error) {
		Toast.makeText(getApplicationContext(), "Activate : " + s,
				Toast.LENGTH_SHORT).show();
	}

	public void getCategories() {
		String serverURL = this.url;

		new MyJs(Dialog, "setCategories", this, "GET").execute(serverURL);
	}

	public void setCategories(String s, String error) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		categories = new APIManager().getCategoriesByBranch(s);
		categoryItems = new ArrayList<Item>();

		for (int i = 0; i < categories.size(); i++) {
			categoryItems
					.add(new Item(categories.get(i).getId(), picture,
							categories.get(i).toString(), categories.get(i)
									.isActive()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info,
				categoryItems);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		registerForContextMenu(listView);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						"Selected: " + branchId, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getBaseContext(), ProductsActivity.class);
				i.putExtra("categoryId", ""
						+ categoryItems.get(position).getId());
				i.putExtra("branchId", "" + branchId);
				i.putExtra("shopId", "" + shopId);
				startActivity(i);
			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cat_context_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Toast.makeText(this, "edit: " + item.getItemId(), Toast.LENGTH_SHORT)
				.show();

		switch (item.getItemId()) {
		case R.id.edit:
			Edit(categoryItems.get((int) info.id));
			break;
		case R.id.delete:
			Delete((categoryItems.get((int) info.id)).getId());
			break;
		default:
			break;

		}
		return true;
	}

	

	public void Edit(Item item) {
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.prompt_cancel, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				CategoriesActivity.this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
		final TextView title = (TextView) promptsView
				.findViewById(R.id.textView1);
		title.setText("Category Name");
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editText1);
		userInput.setHint(item.getTitle());

		final int itemId = item.getId();
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						editCategory(itemId, userInput.getText().toString());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {

			// get prompts.xml view
			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.prompt_addcategory, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.editTextDialogUserInput);

			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									setDialog(dialog);
									Toast.makeText(getApplicationContext(),
											userInput.getText(),
											Toast.LENGTH_LONG).show();
									addCategory(userInput.getText().toString(),
											shopId);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(CategoriesActivity.this, NavigationActivity.class);
		startActivity(i);
	}

	public void addCategory(String categoryName, int shopId) {
		String serverURL = new myURL("categories", null, 0, 0).getURL();
		Category newCategory = new Category(0, categoryName, true, shopId);
		new MyJs(Dialog, "afterCreation", this, "POST", (Object) newCategory)
				.execute(serverURL);
	}

	public void editCategory(int categoryId, String categoryName) {
		String serverURL = new myURL(null, "categories", categoryId, 0)
				.getURL();
		Category newCategory = new Category(categoryId, categoryName, true,
				shopId);
		new MyJs(Dialog, "afterCreation", this, "PUT", (Object) newCategory)
				.execute(serverURL);
	}

	public void afterCreation(String s, String error) {
		Intent i = new Intent(this, CategoriesActivity.class);
		i.putExtra("branchId", "" + branchId);

		i.putExtra("shopId", "" + shopId);
		startActivity(i);
	}
	
	public void Delete(final int catId) {

		new AlertDialog.Builder(this)
				.setTitle("Delete this category?")
				.setIcon(R.drawable.categories)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "categories",
										catId, 0).getURL();
								new MyJs(Dialog, "afterDelete",
										CategoriesActivity.this, "DELETE")
										.execute(serverURL);
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		backToActivity(CategoriesActivity.class);
	}
	public void backToActivity(Class activity) {
		Intent i = new Intent(CategoriesActivity.this, activity);
		i.putExtra("shopId", shopId);
		i.putExtra("branchId", "" + branchId);
		startActivity(i);
	}
}