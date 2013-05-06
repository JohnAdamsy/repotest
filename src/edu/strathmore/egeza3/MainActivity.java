package edu.strathmore.egeza3;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button register,egeza,details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.egeza3_main);
		register=(Button) findViewById(R.id.register);
		egeza=(Button) findViewById(R.id.egeza);
		details=(Button) findViewById(R.id.details); 
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View x) {
              //start the registration activity
				Intent i = new Intent(getApplicationContext(), Register.class);
				startActivity(i);
			}
		});
		egeza.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View y) {
				 //start the registration activity
				Intent i = new Intent(getApplicationContext(), BookParking.class);
				startActivity(i);
			}
		});
		details.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View z) {
		// TODO Auto-generated method stub
		setContentView(R.layout.registeregeza);		
	}
});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.egeza3_main, menu);
		return true;
	}

}
