package com.example.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAO {
	
	private final SQLiteDatabase db;
	
	public TodoDAO(final SQLiteDatabase db) {
		this.db = db;
	}
	
	public boolean persist(final TodoItem item) {
		ContentValues values = new ContentValues();
		values.put("texto", item.getText());
		values.put("done", item.getDone());
		if (item.getId() == null) {
			return db.insert("todo", null, values) > 0;
		} else {
			return db.update("todo", values, "_id = "+item.getId(), null) > 0;
		}
	}
	
	public boolean remove(long id) {
		return db.delete("todo", "_id = "+id, null)>0;
	}
	
	public List<TodoItem> list() {
		List<TodoItem> itens = new ArrayList<TodoItem>();
		Cursor c = db.query("todo", 
				new String[] {"_id","texto","done"}, 
				null, 
				null, 
				null, 
				null, 
				"_id DESC");
		c.moveToFirst();
		while(!c.isAfterLast()) {
			TodoItem item = new TodoItem();
			item.setId(c.getLong(c.getColumnIndex("_id")));
			item.setText(c.getString(c.getColumnIndex("texto")));
			item.setDone(c.getInt(c.getColumnIndex("done")));
			itens.add(item);
			c.moveToNext();
		}
		c.close();
		return itens;
	}
}
