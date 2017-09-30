package com.miot.android.utils;

import android.content.Context;

import com.miot.android.sdk.MiotSDKInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class JSONUitls {
	/**
	 *
	 * @param error
	 * @param errorMessage
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String getJSONResult(Integer error,String errorMessage,Object data)throws Exception{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("error",error);
		jsonObject.put("errorMessage",errorMessage);
		jsonObject.put("data",data);
		return jsonObject.toString();
	}

	public static int getId(String data)throws Exception{
		JSONObject jsonObject=new JSONObject(data);
		return jsonObject.getInt("id");
	}
	public static int getSessionId(String data)throws Exception{
		JSONObject jsonObject=new JSONObject(data);
		return jsonObject.getInt("sessionId");
	}

	public static boolean parseBindPu(String data){
		boolean isBind=false;
		JSONObject jsonObject= null;
		try {
			jsonObject = new JSONObject(data);
			if (jsonObject==null){
				return false;
			}
			if (jsonObject.getString("body").equals("")){
				return false;
			}
			JSONObject jsonObject1=new JSONObject(jsonObject.getString("body"));
			if (jsonObject1==null){
				return false;
			}
			if (jsonObject1.getString("resultCode").equals("1")){
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();

		}
		return false;
	}

	public static String getErrorMessage(String errorCode,String errorMessage){
		String s="{\"body\":{\"resultMsg\":\"errorMessage\",\"data\":{\"puList\":[]},\"resultCode\":\"errorCode\"}}";
		String res="";
		res=s.replaceAll("errorMessage",errorMessage);
		res=res.replaceAll("errorCode",errorCode);
		return res;
	}

	public static String getSceneCu(String sceneId){
		String result="";
		try {
			JSONObject jsonObject=new JSONObject();
			JSONObject data=new JSONObject();
			data.put("sceneId",sceneId);
			jsonObject.put("code","triggerScene");
			jsonObject.put("data",data);
			result=jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void aCacheAll(Context context,String data) throws Exception{
		if (data.equals("")){
			return;
		}
		JSONObject jsonObject=new JSONObject(data);
		if ( jsonObject==null){
			return;
		}
		JSONObject body=new JSONObject(jsonObject.getString("body"));
		if (body.isNull("data")){
			return;
		}
		if (body.getString("data").equals("")){
			return;
		}
		JSONObject jsonObject1=new JSONObject(body.getString("data"));
		if (jsonObject1==null){
			return;
		}
		ACache ache=ACache.get(context);
		ache.clear();
		JSONArray jsonArray=new JSONArray(jsonObject1.getString("puList"));
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObjectPu=new JSONObject(jsonArray.get(i).toString());
			ache.put(jsonObjectPu.getString("puId"),jsonObjectPu.getString("state"),2*60);
		}
	}

	public static JSONObject getSmartConfigMessage()throws Exception{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("host",NetUtil.getWifiMacAddress());
		jsonObject.put("mac", MiotSDKInitializer.MAC);
		jsonObject.put("codeName","smartConnected");
		return  jsonObject;
	}
	public static String getSmartConfigMessageRes(JSONObject jsonObject)throws Exception{
		JSONObject jsonObjectBody=new JSONObject();
		jsonObjectBody.put("resultCode","1");
		jsonObjectBody.put("resultMessage","message");
		jsonObjectBody.put("data",jsonObject);
		return  jsonObjectBody.toString();
	}

	public static JSONObject getSmartConfigMessage(String userId,String mac)throws Exception{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("host",NetUtil.getWifiIpAddress());
		jsonObject.put("mac", MiotSDKInitializer.MAC);
		jsonObject.put("userId", userId);
		jsonObject.put("codeName","smartConfig");
		return  jsonObject;
	}

}
