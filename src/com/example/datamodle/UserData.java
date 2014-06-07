package com.example.datamodle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


public class UserData {
	private static UserData _UserData = null;
	private String _studentID;
	private String _password;
	public Map<String, String> userInfo;
	public Classes classes;
	private Boolean hasSet;
	
	public String yeard;
	public int term;
	
	public UserData()
	{
		userInfo = new HashMap<String, String>();
		classes = new Classes();
		hasSet = false;
	}

	//UserData
	public static UserData shareUserDate()
	{
		if(_UserData == null)
			_UserData = new UserData();

		return _UserData;
	}
	public static void removeUserData()
	{
		_UserData = null;
	}

	//StudentID
	public String getStudentID()
	{
		return _studentID;
	}
	public void setStudentID(String studentID)
	{
		_studentID = studentID;
	}

	//password
	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		_password = password;
	}

	//UserInfo
	public void setUserInfo(JSONObject info)
	{
		try {
			userInfo.put("xm", info.getString("xm"));
			userInfo.put("njmc", info.getString("njmc"));
			userInfo.put("xymc", info.getString("xymc"));
			userInfo.put("rxny", info.getString("rxny"));
			userInfo.put("bjmc", info.getString("bjmc"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getUserInfo()
	{
		return userInfo;
	}

	//classes
	public void setClasses(String xml)
	{
		if(hasSet==false){
			classes.setClassFromXML(xml);
			hasSet = true;
		}
	}
	public void setNewClasses(String xml)
	{
		classes.clear();
		classes.setClassFromXML(xml);
	}
	public List<ClassItem> getClassByWeekIndex(int dayOfweek)
	{
		return classes.getClassbyWeekindex(dayOfweek);
	}
}
