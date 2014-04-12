package com.example.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDB extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	public TodoDB(Context context) {
		// TODO Auto-generated constructor stub
		super(context, "todolist", null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE todo (" +
		   		   "_id integer primary key autoincrement, " +
				   "texto text not null, done integer not null" +
				   ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(db);
	}
	

}
