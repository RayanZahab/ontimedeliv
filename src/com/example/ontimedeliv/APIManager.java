package com.example.ontimedeliv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class APIManager {

	public APIManager() {

	}

	public ArrayList<Country> getCountries(String cont) {

		JSONObject jsonResponse;
		ArrayList<Country> gridArray = new ArrayList<Country>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				String name;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						name = jsonChildNode.optString("name").toString();
						gridArray.add(new Country(id, name));
					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					name = jsonResponse.optString("name").toString();
					gridArray.add(new Country(id, name));
				}

				Log.d("OutputData : ", "Rayz " + gridArray.toString());
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return gridArray;

	}

	public ArrayList<City> getCitiesByCountry(String cont) {
		JSONObject jsonResponse;
		ArrayList<City> gridArray = new ArrayList<City>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				String name;
				int country;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();

					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						country = 1;// Integer.parseInt(jsonResponse.optString("country").toString());
						name = jsonChildNode.optString("name").toString();
						gridArray.add(new City(id, country, name));
					}
					Log.d("OutputData : ", "Rayz " + gridArray.toString());
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					country = 1;// Integer.parseInt(jsonResponse.optString("country").toString());
					name = jsonResponse.optString("name").toString();
					gridArray.add(new City(id, country, name));
				}
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return gridArray;
		}

		return gridArray;
	}

	public ArrayList<Area> getAreasByCity(String cont) {
		JSONObject jsonResponse;
		ArrayList<Area> gridArray = new ArrayList<Area>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
				int lengthJsonArr = jsonMainNode.length();
				int id;
				String name;
				int city;
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					id = Integer.parseInt(jsonChildNode.optString("id")
							.toString());
					city = 1;
					name = jsonChildNode.optString("name").toString();
					gridArray.add(new Area(id, city, name));
				}
				Log.d("OutputData : ", "Rayz " + gridArray.toString());
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return gridArray;
		}

		return gridArray;
	}

	public ArrayList<Business> getBusinesses(String cont) {
		JSONObject jsonResponse;
		ArrayList<Business> gridArray = new ArrayList<Business>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
				int lengthJsonArr = jsonMainNode.length();
				int id;
				String name;
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					id = Integer.parseInt(jsonChildNode.optString("id")
							.toString());
					name = jsonChildNode.optString("name").toString();
					gridArray.add(new Business(id, name));
				}
				Log.d("OutputData : ", "Rayz " + gridArray.toString());
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return gridArray;
	}

	public ArrayList<Shop> getShopsByArea(String cont) {
		JSONObject jsonResponse;
		ArrayList<Shop> gridArray = new ArrayList<Shop>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
				int lengthJsonArr = jsonMainNode.length();
				int id, is_available, business_id;
				String name, desc, business_name;
				Business business;
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					id = Integer.parseInt(jsonChildNode.optString("id")
							.toString());
					name = jsonChildNode.optString("name").toString();
					is_available = Integer.parseInt(jsonChildNode.optString(
							"is").toString());
					desc = jsonChildNode.optString("name").toString();

					// Getting business object
					JSONArray jsonBusinessNode = jsonChildNode
							.optJSONArray("business");
					JSONObject jsonBusChildNode = jsonBusinessNode
							.getJSONObject(0);
					business_id = Integer.parseInt(jsonBusChildNode.optString(
							"id").toString());
					business_name = jsonBusChildNode.optString("name")
							.toString();
					business = new Business(business_id, business_name);

					gridArray.add(new Shop(id, name, desc, is_available,
							business));
				}
				Log.d("OutputData : ", "Rayz " + gridArray.toString());
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return gridArray;
		}

		return gridArray;
	}

	public void getBranchsByShop(Integer shop_id) {
	}

	public void getCategoriesByBranch(Integer branch_id) {
	}

	public void getItemsByCategoryAndBranch(Integer category_id,
			Integer branch_id) {
	}

	public void getItem(Integer item_id) {
	}

	public void getOrdersByUser(Integer user_id) {
	}

	public void getPreparers() {
	}

	public void getDeliverers() {
	}

	public void getOrder(Integer order_id) {
	}

	public void getOrdersByCustomer(Integer customer_id) {
	}

	public void getUser(Integer user_id) {
	}

	public void getUsers() {
	}

	public void getCustomer(Integer customer_id) {
	}

	public void getCustomers() {
	}

	public void getCustomerAddresses(Integer customer_id) {
	}

	public void getCustomerDefaultAddress(Integer customer_id) {
	}

	public void getOrderStatus(Integer order_id) {
	}

	public void getOrderStatusSequence(Integer order_id) {
	}

	public Country createCountry() {
		return new Country(0, "");
	}

	public boolean errorCheck(JSONObject jsonResponse) {
		if (jsonResponse.has("error")) {
			JSONArray jsonMainNode = jsonResponse.optJSONArray("error");
			if (jsonMainNode.length() > 0) {
				return true;
			}
		}
		return false;
	}
	public JSONObject objToCreate(Object o)
	{
		JSONObject jsonObjSend = new JSONObject();
		if(o instanceof Country)
		{
			Country c = (Country) o;
			
			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				jsonObjSend.put("country", body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(o instanceof City)
		{
			City c = (City) o;
			
			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("country_id", c.getCountry_id());
				jsonObjSend.put("city", body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(o instanceof Area)
		{
			Area c = (Area) o;
			
			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("city_id", c.getCity_id());
				jsonObjSend.put("area", body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(o instanceof Business)
		{
			Business c = (Business) o;
			
			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				jsonObjSend.put("business", body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return  jsonObjSend;
	}
}
