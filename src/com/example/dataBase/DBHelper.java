package com.example.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "UserData.db";  
	private static final int DB_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  
		db.execSQL("CREATE TABLE IF NOT EXISTS Users(studentid CHAR(9),password CHAR(20) not null," +
				"yearid VARCHAR,term INTEGER,PRIMARY KEY(studentid,yearid,term))");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS UserInfo(studentid CHAR(9) PRIMARY KEY," +
				"xm VARCHAR(255),njmc VARCHAR(255),xymc VARCHAR(255),rxny VARCHAR(255),bjmc VARCHAR(255))");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Classes("+
				"studentid CHAR(9),yearid VARCHAR,term INTEGER,classname VARCHAR(255),dayofweek INTEGER,"+
				"startt INTEGER,classlocation VARCHAR(255),classstarttime VARCHAR(255),"+
				"classweeks VARCHAR(255),duration INTEGER,classid INTEGER,isSetClock INTEGER,clockHour INTEGER,clockMinute INTEGER,PRIMARY KEY(studentid,yearid,term,classname))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Users");
		db.execSQL("DROP TABLE IF EXISTS UserInfo");
		db.execSQL("DROP TABLE IF EXISTS Classes");
        onCreate(db);

	}  
}
