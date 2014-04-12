package com.example.view;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.TodoDAO;
import com.example.model.TodoDB;
import com.example.model.TodoItem;
import com.example.todolist.R;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MainActivity extends Activity 
implements View.OnKeyListener
{
	
	private TodoListAdapter listAdapter;
	private TodoDB todoDB;
	private TodoDAO todoDAO;
	//private List<TodoItem> listTodo; trocado por DB e DAO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		todoDB = new TodoDB(this);
		todoDAO = new TodoDAO(todoDB.getWritableDatabase());
		
		//listTodo = new ArrayList<TodoItem>();
		listAdapter = new TodoListAdapter(this, 
				R.layout.listrow, 
				todoDAO.list());
		
		ListView listView = (ListView)findViewById(R.id.list_todo);
		listView.setAdapter(listAdapter);
		
		EditText edit= (EditText) findViewById(R.id.new_todo);
		edit.setOnKeyListener(this);
				
				
	}
	
	public void refresh() {
		listAdapter.clear();
		List<TodoItem> list = todoDAO.list();
		for (TodoItem item : list) {
			listAdapter.add(item);
		}
		listAdapter.notifyDataSetInvalidated();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		todoDB.close();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class TodoListAdapter extends 
	        ArrayAdapter<TodoItem> implements
	        View.OnClickListener {

		public TodoListAdapter(Context context, int textViewResourceId,
				List<TodoItem> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TodoItem item = (TodoItem)v.getTag();
			
			if (item != null) {
				if (v.getId()==R.id.done) {
					if (item.getDone()==0) {
						item.setDone(1);
					} else {
						item.setDone(0);
					}
					todoDAO.persist(item);
					refresh();
				} else if (v.getId()==R.id.delete) {
					//listTodo.remove(item);
					todoDAO.remove(item.getId());
					refresh();
				}
				listAdapter.notifyDataSetChanged();
			}
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//return super.getView(position, convertView, parent);
			if (convertView==null) {
				LayoutInflater inflater = 
						(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				
				convertView = inflater.inflate(R.layout.listrow, null);
			}
			
			TodoItem todoItem = getItem(position);
			
			CheckBox done = (CheckBox)convertView.findViewById(R.id.done);
			done.setOnClickListener(this);
			done.setTag(todoItem);
			
			TextView text =  (TextView)convertView.findViewById(R.id.text);
			text.setOnClickListener(this);
			text.setTag(todoItem);
			
			ImageView delete =  (ImageView)convertView.findViewById(R.id.delete);
			delete.setOnClickListener(this);
			delete.setTag(todoItem);
			
			
			done.setChecked(todoItem.getDone()==1);
			
			
			if (todoItem.getDone()==1) {
				SpannableString span = new SpannableString(todoItem.getText());
				span.setSpan(new StrikethroughSpan(), 0, todoItem.getText().length(), 0);
				text.setText(span, BufferType.SPANNABLE);
			} else {
				text.setText(todoItem.getText());
			}
			
			return convertView;
		}
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if ( keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
				EditText edit = (EditText)findViewById(R.id.new_todo);
				if (edit.getText().length() > 0) {
					TodoItem item = new TodoItem();
					item.setText( edit.getText().toString());
					//listTodo.add(item);
					todoDAO.persist(item);
					refresh();
					listAdapter.notifyDataSetChanged();
					edit.setText("");
				} 
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		switch(item.getItemId()) { 
		case R.id.action_settings:
			startActivity(
					new Intent(this,SettingsActivity.class)
					);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
		String listName = prefs.getString("pref_listname", "");
		setTitle(listName);
	}
	
}
