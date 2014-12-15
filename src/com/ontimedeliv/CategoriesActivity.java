package com.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	static int branchId;
	int shopId;
	ArrayList<Category> categories;
	ArrayList<Item> categoryItems;
	String url = new myURL("categories", null, 0, 30).getURL();
	DialogInterface dialog;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds = new ArrayList<Integer>();
	static Activate myCat;
	AlertDialog alertDialog;
	int editing = -1;
	String newName;
	static RZHelper activate;
	static RZHelper deactivate;
	String serverURL ;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);		
		
		branchId = ontimedeliv.getBranchId(this);
		
		
		ontimedeliv.clear("categories");
		
		this.shopId = ontimedeliv.getShopId(this);
		url = new myURL("categories", "branches", branchId, 30).getURL();

		getCategories();

	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public void setDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	
	public static void activate(ArrayList<Integer> selectedIds){
		if (selectedIds.size() > 0) {
			myCat = new Activate("categories", selectedIds);
			activate.put(myCat);
		} 
	}
	public static void deActivate(ArrayList<Integer> unselectedIds){
		if (unselectedIds.size() > 0) {
			myCat = new Activate("categories", unselectedIds);
			deactivate.put(myCat);
		} 
	}

	public void afterActivate(String s, String error) {
		Toast.makeText(getApplicationContext(), "DONE",
				Toast.LENGTH_SHORT).show();
	}

	public void getCategories() {
		String serverURL = this.url;
		RZHelper p = new RZHelper(serverURL, this, "setCategories", false);
		p.get();
	}

	public Bitmap drawableToBitmap(String uri) {
		Drawable drawable = getResources()
				.getDrawable(
						getResources().getIdentifier(uri, "drawable",
								getPackageName()));
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public void setCategories(String s, String error) {
		categories = new APIManager().getCategories(s);
		categoryItems = new ArrayList<Item>();
		
		ListView listView = (ListView) findViewById(R.id.categorylist);

		listView.setTextFilterEnabled(true);

		String catUrl = null;
		if (categories.size() == 0) {
			categoryItems.add(new Item(0, "", getString(R.string.empty_list)));
		} else {
			for (int i = 0; i < categories.size(); i++) {
				catUrl = (new myURL(null, "cat_images", categories.get(i)
						.toString(), 0)).getURL();
				categoryItems.add(new Item(categories.get(i).getId(), catUrl,
						categories.get(i).toString(), categories.get(i)
								.isActive()));
			}
			registerForContextMenu(listView);
		}
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info,
				categoryItems);
		if (categories.size() == 0) {
			dataAdapter.empty = true;
		}
		SharedMenu.adapter = dataAdapter;
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getBaseContext(), ProductsActivity.class);
				ontimedeliv.setCategoryId(dataAdapter.tmpList.get(position)
						.getId());
				startActivity(i);

			}
		});
		serverURL = new myURL("activate_categories", "branches",
				branchId, 0).getURL();
		activate = new RZHelper(serverURL, this, "afterActivate", true);
		
		
		serverURL = new myURL("deactivate_categories", "branches",
				branchId, 0).getURL();
		deactivate = new RZHelper(serverURL, this, "afterActivate", true);
		 
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.categories, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext(),
				dataAdapter);
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

		switch (item.getItemId()) {
		case R.id.edit:
			Edit(dataAdapter.tmpList.get((int) info.id));
			break;
		case R.id.delete:
			Delete((int) info.id);
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

		alertDialogBuilder.setView(promptsView);
		final TextView title = (TextView) promptsView
				.findViewById(R.id.textView1);
		title.setText(R.string.categoryname);
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editText1);
		userInput.setHint(item.getTitle());

		final int itemId = item.getId();
		editing = categoryItems.indexOf(item);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (userInput.getText().toString() != null
								&& userInput.getText().toString().length() > 3)
							editCategory(itemId, userInput.getText().toString());
						else {
							(new ValidationError(true,
									getString(R.string.invalid_name)))
									.isValid(CategoriesActivity.this);
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			if (item.getItemId() == R.id.add) {

				// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(this);
				View promptsView = li
						.inflate(R.layout.prompt_addcategory, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);

				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);

				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);

				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										setDialog(dialog);
										if (userInput.getText().toString() != null
												&& userInput.getText()
														.toString().length() > 3)
											addCategory(userInput.getText()
													.toString(), shopId);
										else
											(new ValidationError(
													true,
													getString(R.string.invalid_name)))
													.isValid(CategoriesActivity.this);
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		SearchView searchView = (SearchView) SharedMenu.menu.findItem(
				R.id.action_search).getActionView();

		if (!searchView.isIconified()) {
			searchView.setIconified(true);
		} else {
			Intent i = new Intent(CategoriesActivity.this,
					BranchesActivity.class);
			ontimedeliv.setBranchId(0);
			startActivity(i);
		}
	}

	public void addCategory(String categoryName, int shopId) {
		String serverURL = new myURL("categories", null, 0, 0).getURL();
		Category newCategory = new Category(0, categoryName, true, shopId);
		RZHelper p = new RZHelper(serverURL, this, "afterCreation", true);
		p.post(newCategory);
	}

	public void editCategory(int categoryId, String categoryName) {
		String serverURL = new myURL(null, "categories", categoryId, 0)
				.getURL();
		Category newCategory = new Category(categoryId, categoryName, true,
				shopId);
		newName = categoryName;
		RZHelper p = new RZHelper(serverURL, this, "afterCreation", true);
		p.put(newCategory);
	}

	public void afterCreation(String s, String error) {

		Category newCat;
		String catUrl;
		if (editing >= 0) {
			newCat = categories.get(editing);
			newCat.setName(newName);
			catUrl = (new myURL(null, "cat_images", newCat.toString(), 0))
					.getURL();
			categoryItems.remove(editing);
		} else {
			newCat = new APIManager().getCategories(s).get(0);
			catUrl = (new myURL(null, "cat_images", newCat.toString(), 0))
					.getURL();
		}
		categoryItems.add(editing,
				new Item(newCat.getId(), catUrl, newCat.toString(), true));

		dataAdapter.currentList = categoryItems;
		dataAdapter.tmpList = categoryItems;
		dataAdapter.notifyDataSetChanged();
	}

	public void Delete(final int position) {
		final int catId = dataAdapter.tmpList.get(position).getId();
		new AlertDialog.Builder(this)
				.setTitle(
						R.string.deletethiscat + " : "
								+ dataAdapter.tmpList.get(position).toString()
								+ "?")
				.setIcon(R.drawable.categories)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null,
										"categories", catId, 0).getURL();
								categoryItems.remove(position);

								RZHelper p = new RZHelper(serverURL,
										CategoriesActivity.this, "afterDelete",
										true);
								p.delete();
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		dataAdapter.currentList = categoryItems;
		dataAdapter.tmpList = categoryItems;
		dataAdapter.notifyDataSetChanged();
	}

	public void backToActivity(Class activity) {
		Intent i = new Intent(CategoriesActivity.this, activity);
		startActivity(i);
	}
}
