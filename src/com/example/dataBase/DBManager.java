package com.example.dataBase;

import java.util.Map;

import com.example.datamodle.ClassItem;
import com.example.datamodle.UserData;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private Cursor cursor = null; 

	public DBManager(Context context) { 
		helper = new DBHelper(context);  
		db = helper.getWritableDatabase();  
	}

	public void UpdateUser(String studentid,String password,String yeard,int term){
		if(IsHasUser(studentid, password, yeard, term))
			return ;
		else
			db.execSQL("INSERT INTO Users(studentid,password,yearid ,term) VALUES (?,?,?,?)",new Object[]{studentid,password,yeard,term});
	}

	public Boolean IsHasUser(String studentid,String password,String yeard,int term){
		cursor = db.rawQuery("SELECT * FROM Users WHERE studentid  =? AND password = ? AND yearid = ? AND term = ?",  new String[]{studentid,password,yeard,String.valueOf(term)});
		if(cursor.getCount()!=0)
			return true;		
		return false;
	}
	public Boolean LoginVarify(String studentid,String password,String yeard,int term){
		cursor = db.rawQuery("SELECT * FROM Users WHERE studentid  =? AND password = ? AND yearid = ? AND term = ?",  new String[]{studentid,password,yeard,String.valueOf(term)});
		if(cursor.getCount() !=0 )
			return true;
		return false;
	}

	public void UpdateUserInfo(String studentid,Map<String, String> userInfo)
	{
		//xm,njmc,xymc,rxny,bjmc
		cursor = db.rawQuery("SELECT * FROM UserInfo WHERE studentid  = ?", new String[]{studentid});
		//		while(cursor.moveToNext())
		//		{
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("studentid")));
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("xm")));
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("njmc")));
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("xymc")));
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("rxny")));
		//			Log.i("db", cursor.getString(cursor.getColumnIndex("bjmc")));
		//		}
		if(cursor.getCount() ==0)
			db.execSQL("INSERT INTO UserInfo VALUES (?,?,?,?,?,?)",new Object[]{studentid,userInfo.get("xm"),userInfo.get("njmc"),userInfo.get("xymc"),userInfo.get("rxny"),userInfo.get("bjmc")});
	}
	public void SelectUserInfo(String studentid)
	{
		cursor = db.rawQuery("SELECT * FROM UserInfo WHERE studentid  = ?", new String[]{studentid});
		while(cursor.moveToNext())
		{
			Log.i("db", cursor.getString(cursor.getColumnIndex("studentid")));
			Log.i("db", cursor.getString(cursor.getColumnIndex("xm")));
			Log.i("db", cursor.getString(cursor.getColumnIndex("njmc")));
			Log.i("db", cursor.getString(cursor.getColumnIndex("xymc")));
			Log.i("db", cursor.getString(cursor.getColumnIndex("rxny")));
			Log.i("db", cursor.getString(cursor.getColumnIndex("bjmc")));
		}
	}
	public void SelectAllClasses(String studentid,String yeard,int term)
	{
		cursor = db.rawQuery("SELECT * FROM Classes WHERE studentid  = ? AND yearid = ? AND term = ?",  new String[]{studentid,yeard,String.valueOf(term)});
		while(cursor.moveToNext())
		{
			Log.i("db", cursor.getString(cursor.getColumnIndex("classname")));
		}
	}
	public void InsertAllClasses(String studentid,String yeard,int term){
		for(int i =1; i<=7 ;i++){
			int size = UserData.shareUserDate().getClassByWeekIndex(i).size();
			for(int j = 0;j < size; j++){
				ClassItem classItem = UserData.shareUserDate().getClassByWeekIndex(i).get(j);
				db.execSQL("INSERT INTO Classes(studentid,yearid,term,dayofweek,startt,classname,classlocation,classstarttime,classweeks,duration,classid,isSetClock) VALUES (?,?,?,?,?,?,?,?,?,?,?,0)",
						new Object[]{studentid,yeard,term,i,classItem.Start,classItem.getClassName(),classItem.getClassLocation(),classItem.getClassStartTime(),classItem.getClassWeeks(),classItem.duration,classItem.classid});
			}
		}
	}
	public void clear()
	{
		db.execSQL("DROP TABLE IF EXISTS Users");
		db.execSQL("DROP TABLE IF EXISTS UserInfo");
		db.execSQL("DROP TABLE IF EXISTS Classes");
		Log.i("db", "Clear");
	}

	public void LoadUserInfoFromDB(String studentid)
	{
		cursor = db.rawQuery("SELECT * FROM UserInfo WHERE studentid  = ?", new String[]{studentid});
		while(cursor.moveToNext())
		{
			UserData.shareUserDate().userInfo.put("xm", cursor.getString(cursor.getColumnIndex("xm")));
			UserData.shareUserDate().userInfo.put("njmc", cursor.getString(cursor.getColumnIndex("njmc")));
			UserData.shareUserDate().userInfo.put("xymc", cursor.getString(cursor.getColumnIndex("xymc")));
			UserData.shareUserDate().userInfo.put("rxny", cursor.getString(cursor.getColumnIndex("rxny")));
			UserData.shareUserDate().userInfo.put("bjmc", cursor.getString(cursor.getColumnIndex("bjmc")));
		}
	}
	
	public void UpdateClock(int classid)
	{
		db.execSQL("UPDATE  WHERE studentid  = ? AND yearid = ? AND term = ? ",)
	}
	public void LoadAllclassFromDB(String studentid,String yeard,int term)
	{
		cursor = db.rawQuery("SELECT * FROM Classes WHERE studentid  = ? AND yearid = ? AND term = ?",  new String[]{studentid,yeard,String.valueOf(term)});
		UserData.shareUserDate().classes.clear();
		while(cursor.moveToNext())
		{
			int classid = cursor.getInt(cursor.getColumnIndex("classid"));
			ClassItem tempclass = new ClassItem(classid);
			tempclass.setClassName(cursor.getString(cursor.getColumnIndex("classname")));
			tempclass.setClassLocation(cursor.getString(cursor.getColumnIndex("classlocation")));
			tempclass.setClassStartTime(cursor.getString(cursor.getColumnIndex("classstarttime")));
			tempclass.setClassWeeks(cursor.getString(cursor.getColumnIndex("classweeks")));
			tempclass.duration = cursor.getInt(cursor.getColumnIndex("duration"));
			tempclass.Start = cursor.getInt(cursor.getColumnIndex("startt"));
			tempclass.clockHour = cursor.getInt(cursor.getColumnIndex("clockHour"));
			tempclass.clockMinute = cursor.getInt(cursor.getColumnIndex("clockMinute"));
			int temp = cursor.getInt(cursor.getColumnIndex("isSetClock"));
			if(temp == 0)
				tempclass.isSetClock = false;
			else
				tempclass.isSetClock = true;
			
			int dayofweek = cursor.getInt(cursor.getColumnIndex("dayofweek"));
			
			UserData.shareUserDate().classes.allClass.get(dayofweek).add(tempclass);
		}
	}


	public void closeDB() {  
		db.close(); 
	}
}
