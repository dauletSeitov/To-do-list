package com.example.example;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
class CustomAdapter extends ArrayAdapter {
	
	ArrayList<Model> listOfWork;
	Context context;
	int 	lastIndex;
	
	@SuppressWarnings("unchecked")
	CustomAdapter(Context context,  List objects) {
		super(context,R.layout.row, objects);
		
		this.context 	= super.getContext();
		this.listOfWork = (ArrayList<Model>) objects;
		this.lastIndex 	= this.listOfWork.size() - 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.row, parent, false); 
		convertView.setOnLongClickListener(new OnLongClickListener() 
		{
			@Override
			public boolean onLongClick(View v) 
			{
				((MainActivity)context).onLongClic(listOfWork.get(position));
				return false;
			}
		});
		
		
		CheckBox cb 	= (CheckBox) convertView.findViewById(R.id.checkBox1);
		TextView name 	= (TextView) convertView.findViewById(R.id.textView2);
		//TextView id 	= (TextView) convertView.findViewById(R.id.textView1);
		
		
		if (position == lastIndex)
		{
			name.setFocusable(false);
			name.setTextColor(Color.BLUE);
			name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
			
			name.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					((MainActivity)context).createNew();
				
				}
			});
			
			cb.setVisibility(View.INVISIBLE);
			//id.setVisibility(View.INVISIBLE);
		}
		
	
		cb.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				((MainActivity)context).onClicChekBox(listOfWork.get(position).getId());
			}
		});
		
		//id.setText(Integer.toString(listOfWork.get(position).getId()));
		name.setText(listOfWork.get(position).getName());
		cb.setChecked(listOfWork.get(position).getValue());
		
		return convertView;
	}
	
}
