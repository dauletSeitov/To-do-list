package com.example.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private 	DBHelper 	DBHR;
		
	private SaySomething seing;
	private void showList()
	{
		ArrayList<Model> listOfWork = DBHR.getAllWorks(DBHR.isShowFull());
		listOfWork.add(new Model(-1,"+ ADD", false));
					
		CustomAdapter adapter = new CustomAdapter(this, listOfWork);
		
		ListView lv = (ListView) findViewById(R.id.listView1);
		//v.setSelection(listOfWork.size()-1);
		lv.setAdapter(adapter);
	}

	private void createDB()
	{
		DBHR = new DBHelper(this);
	}
	
	private boolean showToast(String message) 
	{
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT); 
		toast.show();
		return true;
	}
	
	boolean createNew()
	{
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final EditText edittext = new EditText(this);
		
		alert.setMessage("Works name");
		alert.setTitle("Create");

		alert.setView(edittext);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	String text = edittext.getText().toString().trim();
		    	
		    	if (text.isEmpty())
		    		showToast("Sorry I could not create");
		    	else
		    	{
		    		DBHR.insertWork(text);
		    		showToast("Created");
		    	}
		    	
		    	showList();
		    }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	showToast("Canceled");
		    }
		});

		alert.show();
		
		return true;
	}
	
	private boolean whenSelectedShowFull()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setMessage("Show full?");
		alert.setTitle("Show full");

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	DBHR.insertSettings(true);
		    	showToast("Show full");
	    		showList();	    	
		    }
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	DBHR.insertSettings(false);
		    	showToast("Show only actual");
		    	showList();
		    }
		});

		alert.show();
		
		return true;
	}
		
	void onClicChekBox(int worksId)
	{
		DBHR.updateElement(worksId);
		showToast("Changed");
		showList();
	}
	
	void onLongClic(final Model editableElement)// called from custom adapter
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText edittext = new EditText(this);
		
		alert.setView(edittext);
		
		//alert.setMessage("");
		alert.setTitle("Edit");
		edittext.setText(editableElement.getName());

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	String text = edittext.getText().toString().trim();
		    	if (text.isEmpty())
		    		showToast("Sorry I could not edit");
		    	else
		    	{
		    		DBHR.updateElement(edittext.getText().toString(), editableElement.getId());
	 		    	showToast("Edition saved");
	 		      	showList();
		    	}
		    }
		});

		alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	DBHR.deleteElement(editableElement.getId());		    	
		    	showToast("Deleted");
		    	showList();
		    }
		    
		});

		alert.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createDB();// conect to the data base
		showList();// show list of works
		seing = new SaySomething(getApplicationContext());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) 
		{
		case 	R.id.exit_s:		showToast("Completed");			System.exit(0);				return true;
		case 	R.id.show_full_s: 	whenSelectedShowFull();										return true;
		case 	R.id.say_s:			seing.say(DBHR.getAllWorks(DBHR.isShowFull())); 												return true;
		default:																				return super.onOptionsItemSelected(item);
		}
			
	}

	
}

class SaySomething {
	private TextToSpeech t1;
	SaySomething(Context context){
		t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
	         @Override
	         public void onInit(int status) {
	            if(status != TextToSpeech.ERROR) {
	               t1.setLanguage(Locale.US);
	            }
	         }
	      });	
	}
	void say(ArrayList <Model> works){
		for(Model work : works)
			t1.speak("   " + work.getName() + "   ", TextToSpeech.QUEUE_ADD, null);
	};
}













