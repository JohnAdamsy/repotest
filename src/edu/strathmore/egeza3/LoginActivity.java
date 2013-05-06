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

public class LoginActivity extends Activity{
	String natid, pwd;
	Button login ,register;
	TextView errorLabel;
	EditText national_id,password;
	JSONObject json_user; //json object
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "clientId";
	private static String KEY_FNAME = "fname";
	private static String KEY_LNAME = "lname";
	private static String KEY_NATIONALID = "nationalId";
	private static String TAG="Login";
	
	@SuppressWarnings("unused")
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		register=(Button) findViewById(R.id.btnRegister);
		login=(Button) findViewById(R.id.btnLogin);
		national_id=(EditText)findViewById(R.id.etnatid);
		password=(EditText)findViewById(R.id.etPassword);
		errorLabel=(TextView) findViewById(R.id.validation_status);
	
	}
	
	public void btnRegister_Clicked(View v){//on back pressed
		// Launch Register Screen
		Intent register = new Intent(getApplicationContext(), Register.class);
		// Close all views before launching register Screen
		register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(register);
		finish();//end this activity
	}
	
	public void btnLogin_Clicked(View v){//on register pressed
		//check if all entries have been made
		if(this.isInputProvided()==true){
		natid=national_id.getText().toString();
		pwd=password.getText().toString();
		//initiate registration data transfer request
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.loginUser(natid,pwd);
		
		// check for login response
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				errorLabel.setText("");
				json_user = json;
				
				if(Integer.parseInt(json.getString(KEY_SUCCESS)) == 1){
					// user successfully logged on
					// Store user details in SQLite Database
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					json_user = json.getJSONObject("user");
					
					Log.i(TAG,"Login Response:"+json.toString());
					
					// Clear all previous data in database
					userFunction.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_ID), json_user.getString(KEY_FNAME), json_user.getString(KEY_LNAME), json_user.getString(KEY_NATIONALID));						
					// Launch Main Screen
					Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
					// Close all views before launching Main Screen
				    dashboard.putExtra("national_id", json_user.getString(KEY_NATIONALID));//pass data to the main activity
				    dashboard.putExtra("client_id", json_user.getString(KEY_ID));//
					dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(dashboard);
					// Close Login Screen
					finish();
				}else{
					// Error in login
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
		
		if(national_id.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_national_id_required));
			return false;
		}
		
		if(national_id.getText().toString().trim().length() <8){
			errorLabel.setText(this.getResources().getString(R.string.alert_national_id_invalid_format));
			return false;
		}
		

		if(password.getText().toString().equals("")){
			errorLabel.setText(this.getResources().getString(R.string.alert_password_required));
			return false;
		}
		
		return true;
	}
	
	}
