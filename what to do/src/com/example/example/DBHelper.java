package com.example.example;

import java.util.ArrayList;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


class DBHelper extends SQLiteOpenHelper {
	
	static final String DATABASE_NAME 			= "what_to_do.db";
	static final String WORKS_TABLE_NAME 		= "works_table";
	static final String WORKS_COLUMN_ID 		= "id";
	static final String WORKS_COLUMN_WORK 		= "work";
	static final String WORKS_COLUMN_FINISHED 	= "finished";
	static final int 	DATABASE_VERSION 		= 2;
	
	//Other table
	static final String SETTINGS_TABLE_NAME 		= "settings_table";
	static final String SHOW_FULL_COLUMN			= "show_full";
	
	DBHelper(Context context)
		{
	      super(context, DATABASE_NAME , null, DATABASE_VERSION );
		}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(
			      "CREATE TABLE "+ WORKS_TABLE_NAME +" ("
			      + WORKS_COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
			      + WORKS_COLUMN_WORK     + " varchar (50) NOT NULL, "
			      + WORKS_COLUMN_FINISHED + " boolean NOT NULL DEFAULT 0"
			      + ");"
			      );
		 
		 db.execSQL(
				 "CREATE TABLE "+ SETTINGS_TABLE_NAME +" ("
				 + WORKS_COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " 
				 + SHOW_FULL_COLUMN + " boolean"
				 + ");"
				 );
		 this.insertSettings(true);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + WORKS_TABLE_NAME );
		db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE_NAME );
	    onCreate(db);
	}
	
	//---------------------------------(seetings table part)------------------------------------------------------------------------------------------------------------
	void insertSettings  (Boolean full)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    
		db.delete(SETTINGS_TABLE_NAME, SHOW_FULL_COLUMN + " <> ? ",  new String[] { "2" });
	    
		ContentValues contentValues = new ContentValues();
      
		if(full)
			contentValues.put(SHOW_FULL_COLUMN, 1);
      	else
      		contentValues.put(SHOW_FULL_COLUMN, 0);

      	db.insert(SETTINGS_TABLE_NAME, null, contentValues);
	}
	
	boolean isShowFull()
	{
	
		try {
		
	
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res =  db.rawQuery( "select " + SHOW_FULL_COLUMN + " from " + SETTINGS_TABLE_NAME , null );
			res.moveToFirst();
	            
			if(res.getInt(res.getColumnIndex(SHOW_FULL_COLUMN)) == 0)
				return false;
			return true;
	  		} catch (Exception e) {return true;} 
	}
	

	//---------------------------------(seetings table part)------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------( like a boss. there will bee my own functions  )-------------------------------------------------
	//function for insert work in data base 
	boolean insertWork  (String work)
		{
	      	SQLiteDatabase db = this.getWritableDatabase();
	      	ContentValues contentValues = new ContentValues();
	      	contentValues.put(WORKS_COLUMN_WORK, work);
	      	contentValues.put(WORKS_COLUMN_FINISHED, 0);
	      	db.insert(WORKS_TABLE_NAME, null, contentValues);
	      	return true;
	   }
	
	void updateElement(String worksName, int id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put(WORKS_COLUMN_WORK, worksName);
	    db.update(WORKS_TABLE_NAME, contentValues, WORKS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );	
	}

	void updateElement(int id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor res =  db.rawQuery( "select "+ WORKS_COLUMN_FINISHED +" from " + WORKS_TABLE_NAME  + " where " + WORKS_COLUMN_ID + " = " + Integer.toString(id), null );
	    res.moveToFirst();
	            
	    int state = 1;
	    if(res.getInt(res.getColumnIndex(WORKS_COLUMN_FINISHED)) == 1)
	    	state = 0;
	    
	    db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put(WORKS_COLUMN_FINISHED, state);
	    db.update(WORKS_TABLE_NAME, contentValues, WORKS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );	
	}

	ArrayList<Model> getAllWorks(boolean full)
	{
	      ArrayList<Model> array_list = new ArrayList<Model>();
	  
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res;
	      		
	      if(full)
	    	  res =  db.rawQuery( "select * from " + WORKS_TABLE_NAME , null );
	      else
	    	  res =  db.rawQuery( "select * from " + WORKS_TABLE_NAME + " where " + WORKS_COLUMN_FINISHED + " = 0"  , null );
	      res.moveToFirst();
	            
	      while(res.isAfterLast() == false)
	      {
	    	  boolean finished;
	    	  if(res.getInt(res.getColumnIndex(WORKS_COLUMN_FINISHED)) == 1)
	    		  finished = true;
	    	  else
	    		  finished = false;
	    	  
	    	  Model Model1 = new Model(
	    			  res.getInt(res.getColumnIndex(WORKS_COLUMN_ID)),
	    			  res.getString(res.getColumnIndex(WORKS_COLUMN_WORK)),
	    			  finished
	    			  );
	    	  array_list.add(Model1);
	    	  res.moveToNext();
	      }
	   return array_list;
	}
	
	void deleteElement (int id)
	{	
		SQLiteDatabase db = this.getWritableDatabase();
	   	db.delete(WORKS_TABLE_NAME, WORKS_COLUMN_ID + " = ? ",  new String[] { Integer.toString(id) });
	}
	
}
