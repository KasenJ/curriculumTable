package com.example.datamodle;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.*;

public class NetworkHandler {
	private static AsyncHttpClient client = new AsyncHttpClient(){};

	public static void login(String sid,String psw,AsyncHttpResponseHandler handler)
	{
		Map<String,String> map = new HashMap<String, String>();
		map.put("j_username", sid);
		map.put("j_password", psw);
		RequestParams params = new RequestParams(map);
		client.post("http://uems.sysu.edu.cn/jwxt/j_unieap_security_check.do", params, handler);
	}

	public static void getInfo(JsonHttpResponseHandler handler)
	{
		String content = 
				"{"+
						"header: {"+
							"\"code\": -100," +
							"\"message\": {" +
								"\"title\": \"\","+
								"\"detail\": \"\""+
								"}"+
						"},"+
						"body: {"+
							"dataStores: {"+
								"xsxxStore: {"+
									"rowSet: {"+
											"\"primary\": [],"+
											"\"filter\": [],"+
											"\"delete\": []"+
											"},"+
									"name: \"xsxxStore\","+
									"pageNumber: 1,"+
									"pageSize: 10,"+
									"recordCount: 0,"+
									"rowSetName: \"pojo_com.neusoft.education.sysu.xj.grwh.model.Xsgrwhxx\""+
									"}"+
						"},"+
						"parameters: {"+
						"\"args\": [\"\"],"+
								"}"+
							"}"+
						"}";
		try {
			StringEntity entity = new StringEntity(content);
			client.addHeader("Accept", "*/*");
			client.addHeader("ajaxRequest", "true");
			client.addHeader("render", "unieap");
			client.addHeader("Accept-Encoding", "gzip,deflate");
			client.post(null, "http://uems.sysu.edu.cn/jwxt/WhzdAction/WhzdAction.action?method=getGrwhxxList", entity, "multipart/form-data", handler);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void getTimetable(String year,String term, JsonHttpResponseHandler handler)
	{
		JSONObject send = new JSONObject();
		try {
			JSONArray array =  new JSONArray();
			array.put(0,term);
			array.put(1,year);
			

			JSONObject mes = new JSONObject();
			mes.put("detail", "");
			mes.put("title", "");

			JSONObject header = new JSONObject();
			header.put("code", -100);
			header.put("message", mes);

			JSONObject dataStores= new JSONObject();
			JSONObject parameters= new JSONObject();
			parameters.put("responseParam", "rs");
			parameters.put("args", array);

			JSONObject body = new JSONObject();
			body.put("dataStores", dataStores);
			body.put("parameters", parameters);

			send.put("body", body);
			send.put("header", header);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			StringEntity entity = new StringEntity(send.toString());
			client.addHeader("Accept", "*/*");
			client.addHeader("ajaxRequest", "true");
			client.addHeader("render", "unieap");
			client.addHeader("Accept-Encoding", "gzip,deflate");
			client.post(null, "http://uems.sysu.edu.cn/jwxt/KcbcxAction/KcbcxAction.action?method=getList", entity, "multipart/form-data", handler);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
