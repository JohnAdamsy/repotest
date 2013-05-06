package edu.strathmore.egeza3;

import org.json.JSONException;
import org.json.JSONObject;

import edu.strathmore.egeza.data.DatabaseHandler;
import edu.strathmore.egeza.lib.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity{
	String fname, lname, natid, pwd;
	Button reg ,back, home;
	TextView errorLabel;
	EditText f_name,l_name,national_id,password,cpwd;
	JSONObject json_user; //json object
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "clientId";
	private static String KEY_FNAME = "Fname";
	private static String KEY_LNAME = "Lname";
	private static String KEY_NATIONALID = "nationalId";
	private static String TAG="Register";
	
	@SuppressWarnings("unused")
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeregeza);
		reg=(Button) findViewById(R.id.register);
		back=(Button) findViewById(R.id.egeza);
		home=(Button) findViewById(R.id.details);
		f_name=(EditText)findViewById(R.id.etfname);
		l_name=(EditText)findViewById(R.id.etlname);
		national_id=(EditText)findViewById(R.id.etnatid);
		password=(EditText)findViewById(R.id.etPassword);
		cpwd=(EditText)findViewById(R.id.etCPassword);
		errorLabel=(TextView) findViewById(R.id.validation_status);
	
	}
	
	public void btnBack_Clicked(View v){//on back pressed
        // Launch Login Screen
		Intent login = new Intent(getApplicationContext(), LoginActivity.class);
		// Close all views before launching login Screen
		login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(login);
		finish();//end this activity
	}
	
	public void btnRegister_Clicked(View v){//on login pressed
		//check if all entries have been made
		if(this.isInputProvided()==true){
		
		natid=national_id.getText().toString();
		pwd=password.getText().toString();
		//initiate login data transfer request
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.registerUser(fname, lname, natid,pwd);
		
		// check for login response
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				errorLabel.setText("");
				json_user = json;
				
				if(Integer.parseInt(json.getString(KEY_SUCCESS)) == 1){
					// user successfully registered
					// Store user details in SQLite Database
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					
					
					Log.i(TAG,"Server Response:"+json_user.toString());
					
					// Clear all previous data in database
					userFunction.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_ID), json_user.getString(KEY_FNAME), json.getString(KEY_LNAME), json_user.getString(KEY_NATIONALID));						
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
	
	//method to ensure all required fields are provided by the user
	private boolean isInputProvided(){
		if(f_name.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_first_name_required));
			return false;
		}
		
		if(l_name.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_last_name_required));
			return false;
		}
		
		if(password.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_password_required));
			return false;
		}
		
		if(cpwd.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_cpassword_required));
			return false;
		}
		
		if(!password.getText().toString().equals(cpwd.getText().toString())){
			errorLabel.setText(this.getResources().getString(R.string.alert_password_no_match));
			return false;
		}
		
		if(national_id.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_national_id_required));
			return false;
		}
		
		if(national_id.getText().toString().trim().length() <8){
			errorLabel.setText(this.getResources().getString(R.string.alert_national_id_invalid_format));
			return false;
		}
		return true;
	}
	
	}
