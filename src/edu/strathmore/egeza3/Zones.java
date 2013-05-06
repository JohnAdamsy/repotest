package edu.strathmore.egeza3;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Zones extends ListActivity {
	String zones[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(Zones.this, android.R.layout.activity_list_item, zones));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		try{
		 Class<?> pClass = Class.forName("edu.strathmore.BOOKPARKING");
		Intent pIntent = new Intent(Zones.this, pClass);
		startActivity(pIntent);
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}



}
