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
		Log.d("Areabycity : ", "areabycity " + cont);
		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				
				int id;
				String name;
				int city;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	
						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						city = 1;
						name = jsonChildNode.optString("name").toString();
						gridArray.add(new Area(id, city, name));
					}
					Log.d("OutputData : ", "Rayz " + gridArray.toString());
				}
				else
				{
					id = Integer.parseInt(jsonResponse.optString("id").toString());
					city = 1;
					name = jsonResponse.optString("name").toString();
					gridArray.add(new Area(id, city, name));
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

	public ArrayList<Business> getBusinesses(String cont) {
		JSONObject jsonResponse;
		ArrayList<Business> gridArray = new ArrayList<Business>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				String name;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	
						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						name = jsonChildNode.optString("name").toString();
						gridArray.add(new Business(id, name));
					}
					Log.d("OutputData : ", "Rayz " + gridArray.toString());
				}
				else
				{
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					name = jsonResponse.optString("name").toString();
					gridArray.add(new Business(id, name));
				}
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
			int id, is_available;
			String name, desc,business_str;
			Business business;
			ArrayList<Business> businesses;
			if (!errorCheck(jsonResponse)) {
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
	
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	
						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						name = jsonChildNode.optString("name").toString();
						is_available = 1;//Integer.parseInt(jsonChildNode.optString("is").toString());
						desc = jsonChildNode.optString("name").toString();
	
						// Getting business object
						business_str = jsonChildNode.optString("business").toString();
						businesses= getBusinesses(business_str);
						business =businesses.get(0);
	
						gridArray.add(new Shop(id, name, desc, is_available,
								business));
					}
					Log.d("OutputData : ", "Rayz " + gridArray.toString());
				}
				else
				{
					id = Integer.parseInt(jsonResponse.optString("id").toString());
					name = jsonResponse.optString("name").toString();
					is_available = 1;//Integer.parseInt(jsonResponse.optString("is").toString());
					desc = jsonResponse.optString("name").toString();

					// Getting business object
					business_str = jsonResponse.optString("business").toString();
					businesses= getBusinesses(business_str);
					business =businesses.get(0);									

					gridArray.add(new Shop(id, name, desc, is_available,
							business));
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return gridArray;
		}

		return gridArray;
	}

	public ArrayList<Branch> getBranchesByShop(String cont) {
		JSONObject jsonResponse;
		ArrayList<Branch> gridArray = new ArrayList<Branch>();
		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				String name, address, estimation_time, description, area_str, shop_str, longitude, latitude;
				Area area;
				ArrayList<Area> areas;
				Shop shop;
				ArrayList<Shop> shops;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

						id = Integer.parseInt(jsonChildNode.optString("id").toString());
						name = jsonChildNode.optString("name").toString();
						description = jsonChildNode.optString("description").toString();
						address = jsonChildNode.optString("address").toString();
						estimation_time = jsonChildNode.optString("estimation_time").toString();
						area_str = jsonChildNode.optString("area").toString();
						areas= getAreasByCity(area_str);
						area =areas.get(0);
						shop_str = "";//jsonChildNode.optString("shop").toString();
						//shops= getShopsByArea(shop_str);
						shop =null;//shops.get(0);
						longitude = jsonChildNode.optString("longitude").toString();
						latitude = jsonChildNode.optString("latitude").toString();
						int close_hour, open_hour, is_available;
						open_hour = close_hour = is_available = 1;
						gridArray.add(new Branch(id, name, description, area,
								address, is_available, shop, longitude,
								latitude, open_hour, close_hour,
								estimation_time));

					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id").toString());
					name = jsonResponse.optString("name").toString();
					description = jsonResponse.optString("description").toString();
					address = jsonResponse.optString("address").toString();
					estimation_time = jsonResponse.optString("estimation_time").toString();
					
					area_str = jsonResponse.optString("area");
					areas= getAreasByCity(area_str);
					area =areas.get(0);
					longitude = jsonResponse.optString("longitude").toString();
					latitude = jsonResponse.optString("latitude").toString();
					int close_hour, open_hour, is_available;
					shop_str = "";//jsonResponse.optString("shop").toString();
					//shops= getShopsByArea(shop_str);
					shop =null;//shops.get(0);
					open_hour = close_hour = is_available = 1;
					gridArray.add(new Branch(id, name, description, area,
							address, is_available, shop, longitude, latitude,
							open_hour, close_hour, estimation_time));
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

	public ArrayList<Category> getCategoriesByBranch(String cont) {
		JSONObject jsonResponse;
		ArrayList<Category> gridArray = new ArrayList<Category>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				String name;
				boolean is_active;
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
						is_active = Boolean.valueOf(jsonChildNode.optString("is_active").toString());
						gridArray.add(new Category(id, name,is_active,0));
					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					name = jsonResponse.optString("name").toString();
					is_active = Boolean.valueOf(jsonResponse.optString("is_active").toString());
					gridArray.add(new Category(id, name, is_active,0));
				}

				Log.d("OutputData Category: ", "Rayz Category" + gridArray.toString());
			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return gridArray;
	}

	public Photo getPhoto(String cont) {
		JSONObject jsonResponse;
		String url="",thumb="";
		Photo photo=new Photo(0,url,thumb);
		try {
			jsonResponse = new JSONObject(cont);
		
			 url =jsonResponse.optString("url").toString();
			 thumb = jsonResponse.optString("thumb").toString();
			photo.setUrl(url);
			photo.setThumb(thumb);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		return photo;
	}
	public ArrayList<Product> getItemsByCategoryAndBranch(String cont) {
		JSONObject jsonResponse;
		ArrayList<Product> gridArray = new ArrayList<Product>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				boolean is_available;
				String name,description,photo_str,category_str,unit_str,price;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());						
						price = jsonChildNode.optString("price")
										.toString();
						name = jsonChildNode.optString("name").toString();
						description = jsonChildNode.optString("description").toString();
						photo_str = jsonChildNode.optString("photo").toString();
						is_available = Boolean.valueOf(jsonChildNode.optString("is_available").toString());
						gridArray.add(new Product(id,price,name,description,getPhoto(photo_str),new Category(0,"",true,0),new Unit(0,""),is_available,0));
					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());						
					price = jsonResponse.optString("price")
									.toString();
					name = jsonResponse.optString("name").toString();
					description = jsonResponse.optString("description").toString();
					photo_str = jsonResponse.optString("photo").toString();
					is_available = Boolean.valueOf(jsonResponse.optString("is_available").toString());
					gridArray.add(new Product(id,price,name,description,getPhoto(photo_str),new Category(0,"",true,0),new Unit(0,""),is_available,0));
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

	public ArrayList<Unit> getUnits(String cont) {

		JSONObject jsonResponse;
		ArrayList<Unit> gridArray = new ArrayList<Unit>();

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
						gridArray.add(new Unit(id, name));
					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					name = jsonResponse.optString("name").toString();
					gridArray.add(new Unit(id, name));
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

	public ArrayList<Address> getAddress(String cont) {
		JSONObject jsonResponse;
		ArrayList<Address> gridArray = new ArrayList<Address>();
		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id, customer_id;
				String country, city, area, street, building, floor, details, longitude, latitude, created_at, updated_at;
				if (jsonResponse.has("elements")) {
					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("elements");
					int lengthJsonArr = jsonMainNode.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						id = Integer.parseInt(jsonChildNode.optString("id")
								.toString());
						country = jsonChildNode.optString("country").toString();
						city = jsonChildNode.optString("city").toString();
						area = jsonChildNode.optString("area").toString();
						street = jsonChildNode.optString("street").toString();
						building = jsonChildNode.optString("building")
								.toString();
						floor = jsonChildNode.optString("floor").toString();
						details = jsonChildNode.optString("details").toString();
						longitude = jsonChildNode.optString("longitude")
								.toString();
						latitude = jsonChildNode.optString("latitude")
								.toString();
						created_at = jsonChildNode.optString("create_at")
								.toString();
						updated_at = jsonChildNode.optString("update_at")
								.toString();
						customer_id = 0;// Integer.parseInt(jsonChildNode.optString(
						// "customer_id").toString());
						gridArray.add(new Address(id, country, city, area,
								building, floor, street, details, customer_id,
								longitude, latitude, created_at, updated_at));

					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					country = jsonResponse.optString("country").toString();
					city = jsonResponse.optString("city").toString();
					area = jsonResponse.optString("area").toString();
					street = jsonResponse.optString("street").toString();
					building = jsonResponse.optString("building").toString();
					floor = jsonResponse.optString("floor").toString();
					details = jsonResponse.optString("details").toString();
					longitude = jsonResponse.optString("longitude").toString();
					latitude = jsonResponse.optString("latitude").toString();
					created_at = jsonResponse.optString("create_at").toString();
					updated_at = jsonResponse.optString("update_at").toString();
					customer_id = 0;// Integer.parseInt(jsonResponse.optString(
					// "customer_id").toString());
					gridArray.add(new Address(id, country, city, area,
							building, floor, street, details, customer_id,
							longitude, latitude, created_at, updated_at));
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

	public ArrayList<User> getUsers(String cont) {
		JSONObject jsonResponse,jsonRole;
		Log.d("ray","ray user cont"+cont);
		ArrayList<User> gridArray = new ArrayList<User>();

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				int id;
				boolean is_fired,admin,preparer,delivery;
				String name, phone, password, username, mobile, roles_str;
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
						roles_str = jsonChildNode.optString("roles").toString();
						jsonRole = new JSONObject(roles_str);
						admin=Boolean.parseBoolean(jsonRole.optString("admin").toString());
						preparer=Boolean.parseBoolean(jsonRole.optString("preparer").toString());
						delivery=Boolean.parseBoolean(jsonRole.optString("delivery").toString());
						
						username = "";// jsonChildNode.optString("username").toString();
						password = "";// jsonChildNode.optString("password").toString();
						phone = jsonChildNode.optString("phone").toString();
						mobile = "";// jsonChildNode.optString("mobile").toString();
						is_fired = Boolean.parseBoolean(jsonChildNode.optString("is_fired").toString());
						
						User u = new User(id, name, username, password, phone,
								mobile, (is_fired)?1:0, null,0,admin,preparer,delivery);
						gridArray.add(u);

					}
				} else {
					id = Integer.parseInt(jsonResponse.optString("id")
							.toString());
					name = jsonResponse.optString("name").toString();
					username ="";// jsonResponse.optString("username").toString();
					password = "";//jsonResponse.optString("password").toString();
					phone = jsonResponse.optString("phone").toString();
					mobile ="";// jsonResponse.optString("mobile").toString();
					is_fired = Boolean.parseBoolean(jsonResponse.optString("is_fired").toString());
					roles_str = jsonResponse.optString("roles").toString();
					jsonRole = new JSONObject(roles_str);
					admin=Boolean.parseBoolean(jsonRole.optString("admin").toString());
					preparer=Boolean.parseBoolean(jsonRole.optString("preparer").toString());
					delivery=Boolean.parseBoolean(jsonRole.optString("delivery").toString());
					
					gridArray.add(new User(id, name, username, password, phone,
							mobile, (is_fired)?1:0, null,0,admin,preparer,delivery));

				}

			} else {
				return gridArray;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return gridArray;
	}
	public int getUserId(String cont) {
		JSONObject jsonResponse;

		try {
			jsonResponse = new JSONObject(cont);
			if (!errorCheck(jsonResponse)) {
				{
					return Integer.parseInt(jsonResponse.optString("id")
							.toString());					

				}
			} else {
				return 0;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return 0;
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

	public JSONObject objToCreate(Object o) {
		JSONObject jsonObjSend = new JSONObject();
		if (o instanceof Country) {
			Country c = (Country) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				jsonObjSend.put("country", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof City) {
			City c = (City) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("country_id", c.getCountry_id());
				jsonObjSend.put("city", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Area) {
			Area c = (Area) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("city_id", c.getCity_id());
				jsonObjSend.put("area", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Business) {
			Business c = (Business) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				jsonObjSend.put("business", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Category) {
			Category c = (Category) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("shop_id", c.getShopId());
				jsonObjSend.put("category", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof User) {
			User c = (User) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("phone", c.getPhone());
				body.put("password", c.getPassword());
				body.put("pass", c.getPassword());
				body.put("branch_id", c.getBranch_id());
				body.put("customer_address_id", 0);
				body.put("is_fired", 0);
				
				jsonObjSend.put("user", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Product) {
			Product c = (Product) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("price", c.getPrice());
				body.put("description", c.getDescription());
				body.put("shop_id", c.getShop_id());
				body.put("category_id", c.getCategory().getId());
				body.put("unit_id", c.getUnit().getId());
				body.put("photo", c.getPhoto().getUrl());
				
				jsonObjSend.put("item", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Branch) {
			Branch c = (Branch) o;

			JSONObject body = new JSONObject();
			try {
				body.put("name", c.getName());
				body.put("address", c.getAddress());
				body.put("description", c.getDescription());
				body.put("shop_id", c.getShop().getId());
				body.put("area_id", c.getArea().getId());
				body.put("long", c.getLongitude());
				body.put("lat", c.getLatitude());
				body.put("estimation_time", c.getEstimation_time());
				
				jsonObjSend.put("branch", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}  else if (o instanceof Activate) {
			Activate c = (Activate) o;
			JSONArray jsonArray = new JSONArray();
			try {
				for(int i =0 ;i<c.getToUpdate().size();i++)
				{
					jsonArray.put(c.getToUpdate().get(i));
				}					
				jsonObjSend.put("categories",jsonArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (o instanceof Role) {
			Role c= (Role) o;
			JSONObject body = new JSONObject();
			try {
				body.put("is_admin", c.getAdmin()?1:0);
				body.put("is_preparer", c.getPreparer()?1:0);
				body.put("is_delivery", c.getDelivery()?1:0);

				
				jsonObjSend.put("roles", body);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("ray"," ray put cont:"+jsonObjSend.toString());
		}
		

		return jsonObjSend;
	}
	
}
