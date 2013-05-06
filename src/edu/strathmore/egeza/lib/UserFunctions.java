/**
 * @description class contains functions used to retrieve data and push data to server from the client
 * */
package edu.strathmore.egeza.lib;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import edu.strathmore.egeza.data.DatabaseHandler;

import android.content.Context;
import android.util.Log;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://10.0.2.2/egeza/";
	private static String registerURL = "http://10.0.2.2/egeza/";
	
	private static String login_tag = "login";
	private static String service = "data";
	private static String register_tag = "register";
	private static String book_tag = "book";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String national_id, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("service", service));
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("nationalId", national_id));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		//Log.e("JSON", json.toString());
		Log.d("Login Details",json.toString());
		return json;
	}
	
	/**
	 * function make Booking request
	 * @param 
	 * */
	public JSONObject bookParking(String client_id, String veh_id,String zone_id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("service", service));
		params.add(new BasicNameValuePair("tag", book_tag));
		params.add(new BasicNameValuePair("clientId", client_id));
		params.add(new BasicNameValuePair("vehicleNo", veh_id));
		params.add(new BasicNameValuePair("zoneId", zone_id));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		//Log.e("JSON", json.toString());
		Log.d("Booking Details",json.toString());
		return json;
	}
	
	/**
	 * function make Registration Request
	 * @param fname
	 * @param lname
	 * @param nationalId
	 * @param password
	 * */
	public JSONObject registerUser(String fname,String lname, String natid, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("service", service));
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("fname", fname));
		params.add(new BasicNameValuePair("lname", lname));
		params.add(new BasicNameValuePair("nationalId", natid));
		params.add(new BasicNameValuePair("password", password));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
}
