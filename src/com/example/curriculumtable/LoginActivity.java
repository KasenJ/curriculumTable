package com.example.curriculumtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.dataBase.DBManager;
import com.example.datamodle.*;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private TextView studentid;
	private TextView password;
	private Time time;
	Dialog alertDialog;
	int term ;
	String year;
	DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		time = new Time("GMT+8");
		time.setToNow();
		studentid = (TextView)findViewById(R.id.usernameInput);
		password = (TextView)findViewById(R.id.passwordInput);
		term = 0;
		year =null;
		dbManager = new DBManager(this);
		//获取只能被本应用读写的SharedPreferences对象
		//用于记录用户的用户名和密码
		SharedPreferences preferences = getSharedPreferences("crazyit",MODE_PRIVATE);
		String username = preferences.getString("username", null);
		String pass = preferences.getString("password", null);
		studentid.setText(username);
		password.setText(pass);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void LoginClick(View v)
	{
		String sid = studentid.getText().toString();
		String psw = password.getText().toString();
		SetPar();
		if (sid.length() != 8 || psw.length() == 0)
		{
			alertDialog = new AlertDialog.Builder(this). 
					setTitle("错误"). 
					setMessage("您输入的信息有误"). 
					setIcon(R.drawable.ic_launcher). 
					create(); 
			alertDialog.show(); 
		}
		else if(dbManager.IsHasUser(studentid.getText().toString(), password.getText().toString(), year, term)){
			UserData.shareUserDate().setStudentID(studentid.getText().toString());
			UserData.shareUserDate().setPassword(password.getText().toString());
			dbManager.LoadUserInfoFromDB(studentid.getText().toString());
			dbManager.LoadAllclassFromDB(studentid.getText().toString(), year, term);
			Intent intent = new Intent(LoginActivity.this, Table.class);
			startActivity(intent);
		}
		else {
			SharedPreferences preferences = getSharedPreferences("crazyit",MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("username", sid);
			editor.putString("password", psw);
			editor.commit();
			alertDialog = new AlertDialog.Builder(this). 
					setTitle("正在登录"). 
					setMessage("请耐心等待..."). 
					//setIcon(R.drawable.ic_launcher). 
					create(); 
			alertDialog.show();
			NetworkHandler.login(sid, psw, new AsyncHttpResponseHandler()
			{
				@Override
				public void onSuccess(String content)
				{
					alertDialog.setTitle("正在载入数据");
					UserData.shareUserDate().setStudentID(studentid.getText().toString());
					UserData.shareUserDate().setPassword(password.getText().toString());

					if(VerifyLogin(content)){
						UserData.shareUserDate().setStudentID(studentid.getText().toString());
						UserData.shareUserDate().setPassword(password.getText().toString());
						setUserInfo();
					}
					else{
						alertDialog.dismiss();
						alertDialog = new AlertDialog.Builder(LoginActivity.this).
								setTitle("错误").
								setMessage("用户名或密码错误").
								setPositiveButton("确定", null).
								create();
						alertDialog.show();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					alertDialog.dismiss();
					alertDialog = new AlertDialog.Builder(LoginActivity.this).
							setTitle("错误1").
							setMessage("认证失败").
							setPositiveButton("确定", null).
							create();
					alertDialog.show();
				}
			});
		}
	}

	public void ExitClick(View v)
	{
		finish();
	}

	private void setUserInfo(){
		NetworkHandler.getInfo(new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(JSONObject content) {
				alertDialog.dismiss();
				try {
					JSONArray primary = content.getJSONObject("body").getJSONObject("dataStores").getJSONObject("xsxxStore").getJSONObject("rowSet").getJSONArray("primary");
					JSONObject data = primary.getJSONObject(0);
					UserData.shareUserDate().setUserInfo(data);
					gettimeTable();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				alertDialog.dismiss();
				alertDialog = new AlertDialog.Builder(LoginActivity.this).
						setTitle("错误2").
						setMessage("认证失败").
						setPositiveButton("确定", null).
						create();
				alertDialog.show();
			}
		});
	}

	private void gettimeTable()
	{
		SetPar();
		NetworkHandler.getTimetable(year,String.valueOf(term),new JsonHttpResponseHandler()
		{
			@Override 
			public void onSuccess(JSONObject content) {
				try {
					String rs = content.getJSONObject("body").getJSONObject("parameters").getString("rs");
					UserData.shareUserDate().setClasses(rs);
					if(!dbManager.IsHasUser(studentid.getText().toString(), password.getText().toString(), year, term)){
						dbManager.UpdateUser(studentid.getText().toString(), password.getText().toString(), year, term);
						dbManager.UpdateUserInfo(studentid.getText().toString(), UserData.shareUserDate().getUserInfo());
						dbManager.InsertAllClasses(studentid.getText().toString(), year, term);
					}
					dbManager.SelectAllClasses(studentid.getText().toString(), year, term);
					dbManager.SelectUserInfo(studentid.getText().toString());

					Intent intent = new Intent(LoginActivity.this, Table.class);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				alertDialog = new AlertDialog.Builder(LoginActivity.this).
						setTitle("错误").
						setMessage("认证失败").
						setPositiveButton("确定", null).
						create();
				alertDialog.show();
			}
		});
	}


	private Boolean VerifyLogin(String content) {
		Document doc = Jsoup.parse(content,"GB2312");
		Elements span = doc.select("span");
		if(span.size()==0)
			return true;
		else 
			return false;
	}

	private void SetPar()
	{
		switch((time.month+1))
		{
		case 2:case 3:case 4:case 5:case 6:
			term = 3;
			year = String.valueOf(time.year-1)+"-"+String.valueOf(time.year);
			break;
		case 7:case 8:
			term = 1;
			year = String.valueOf(time.year)+"-"+String.valueOf(time.year+1);
			break;
		case 9:case 10:case 11:case 12: 
			term = 2;
			year = String.valueOf(time.year)+"-"+String.valueOf(time.year+1);
			break;
		case 1:
			term = 2;
			year = String.valueOf(time.year-1)+"-"+String.valueOf(time.year);
			break;
		}
		UserData.shareUserDate().yeard = year;
		UserData.shareUserDate().term = term;
	}
}
