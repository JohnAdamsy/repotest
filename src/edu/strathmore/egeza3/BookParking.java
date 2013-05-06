package edu.strathmore.egeza3;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.strathmore.egeza.data.DatabaseHandler;
import edu.strathmore.egeza.lib.JSONParser;
import edu.strathmore.egeza.lib.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BookParking extends Activity {
	
	// Progress Dialog
		private ProgressDialog pDialog;

		// Creating JSON Parser object
		JSONParser jsonParser = new JSONParser();


		// group names JSONArray
		JSONArray group = null;
		JSONArray phone = null;

		// zones JSON url
		private static final String ZONES_URL = "http://10.0.2.2/egeza/";
		
		private static String service_type="zones";
		
		
		// group names JSON nodes
		private static final String TAG_ZONE = "zones";
		private static final String TAG_ID = "zoneId";
		private static final String TAG_NAME = "zoneName";
		private static String [] items;
		
		
		//custom variables
		private static Spinner sp;
		private static EditText vehid;
		private static TextView errorLabel;
		public String action="",pz,vid;
		
		private static String KEY_SUCCESS = "success";
		private static String KEY_ERROR = "error";
		private static String KEY_ERROR_MSG = "error_msg";
		private static String TAG="BookParking";
		JSONObject json_user; //json object
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookparking);
        
        sp=(Spinner) findViewById(R.id.spinnerParkingZone);
        vehid=(EditText)findViewById(R.id.etvh);
        errorLabel=(TextView)findViewById(R.id.validation_status);
        //register a listener
        sp.setOnItemSelectedListener(new OnClassSpinnerSelectedListener());
         action="on_load";
        // Loading Retrieve Zone Names in Background Thread
         
        new LoadZoneNamesThread().execute();
    }
    
    /**
	 * Background Async Task to Load all Parking zones by making HTTP Request
	 * */
	class LoadZoneNamesThread extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookParking.this);
			if(action=="on_load"){
			pDialog.setMessage("Contacting server...");
			}
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Events JSON
		 * */
		@Override
		protected String doInBackground(String...arg) {
			if(action=="on_load"){
				//service to retrieve class names
				this.retrieveZoneNamesFromServer();
			}
			
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into Spinner
					 * */
                    if(action=="on_load"){
                    	//populate spinner
                    	ArrayAdapter<String> adapter = 
    	                        new ArrayAdapter<String> (BookParking.this, android.R.layout.simple_spinner_item, items);       
    	                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	                sp.setAdapter(adapter);
                    }
	            
				}
			});

		}
		
		//method to retrieve class names
		private void retrieveZoneNamesFromServer(){
			// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("service", service_type));
						params.add(new BasicNameValuePair("qservice", service_type));
						
						// getting JSON string from URL
						JSONObject json = jsonParser.makeHttpRequest(ZONES_URL, "GET",params);
						

						// Check your log cat for JSON reponse
						Log.d("Group JSON: ", json.toString());

						try {
							group  = json.getJSONArray(TAG_ZONE);
							// looping through All zone names
							
							//are there any zone names?
							if(group.length()>0){
								Log.d("Zone JSON: ", group.length()+" Zone found");
								
								items = new String[group.length()+1];
								items [0]="Select Parking Zone";
								Log.d("Zone JSON: ", items.length+" array size");
							for (int i=1; i <= group.length(); i++) {
								JSONObject c = group.getJSONObject(i-1);
			                 
								// Storing each json item in variable
								String id = c.getString(TAG_ID);
								String name = c.getString(TAG_NAME);
								
								
								items [i]=name;
								System.out.println("Hello Zones "+items);
							}
							
							}//close 
							else{
								Log.d("Zone JSON: ", "0 zones found");	
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						
		}//end of retrieveClassNamesFromServer
		
		
	}//end of the inner class MessageActivityThread
	
	//inner class to register a listener to our spinner
	public class OnClassSpinnerSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	      //Toast.makeText(parent.getContext(), "The class is " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	      action="on_select";
	      if(pos!=0){
	     
	      }
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}//end of the inner class OnClassSpinnerSelectedListener
	
	public void btnEgeza_Clicked(View v){
		//check if all entries have been made
				if(this.isInputProvided()==true){
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				Cursor cursor=db.getClientInfo();
				
				pz=sp.getSelectedItem().toString();
				vid=vehid.getText().toString();
				//initiate login data transfer request
				UserFunctions userFunction = new UserFunctions();
				 cursor.moveToFirst();
				if(cursor.getCount()>0){
					Log.i(TAG,"Sqlite Found: "+cursor.getInt(0));
				}
				JSONObject json = userFunction.bookParking(String.valueOf(cursor.getInt(0)), vid, pz);
				cursor.close();
				db.close();
				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						errorLabel.setText("");
						json_user = json;
						
						if(Integer.parseInt(json.getString(KEY_SUCCESS)) == 1){
							// booking successfully done
							
							
							Log.i(TAG,"Server Response:"+json_user.toString());
							
			              //add booking to the local db
							//db.addBooking(json_user.getString(KEY_ID), json_user.getString(KEY_FNAME), json.getString(KEY_LNAME), json_user.getString(KEY_NATIONALID));						
							// Launch Main Screen
							Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
							// Close all views before launching Main Screen
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							// Close Registration Screen
							finish();
						}else{
							// Error in registration
							errorLabel.setText(json_user.getString(KEY_ERROR_MSG));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				}
	}
	
	//on back pressed --go to dashboard
    public void btnBack_Clicked(View v){
    	finish();
    }
	
	//confirm all field are present
	private boolean isInputProvided(){
		if(sp.getSelectedItemPosition()==0){
			errorLabel.setText(this.getResources().getString(R.string.alert_parkingzone_required));
			return false;
		}
		
		if(vehid.getText().toString().trim().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_vehicleid_required));
			return false;
		}
		
		return true;
	}
	
	 
	
	
}