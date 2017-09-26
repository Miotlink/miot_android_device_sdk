package com.miot.android.webservice;

import com.miot.android.utils.Base64;
import com.miot.android.utils.MD5;
import com.miot.android.utils.MacUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class ParmasUitls {
	public static String v = "2.0";
	public static String appVersion = "1.0";
	public static String appId = "com.miot.android.smarthome";
	/**
	 * 获取设备列表 和场景列表
	 */

	public static String getPuListAndSceneList(String mac,int sessionId){
		String params="";
		String code = "openApiReverseGetThings";
		String serial = MacUtils.mac2serial(mac);
		String reqTime =System.currentTimeMillis()+"";
		StringBuilder src = new StringBuilder();
		src.append(code).append(reqTime).append(mac).append(v)
				.append(appVersion).append(appId).append(sessionId)
				.append(mac).append(serial);

		String accessToken = MD5.getMD5(src.toString(),"UTF-8");
		params = "{\"head\":{\"code\":\""+code+"\",\"reqTime\":\""+reqTime+"\",\"accessKey\":\""+mac+"\",\"accessToken\":\""+accessToken
				+"\",\"v\":\""+v+"\",\"appVersion\":\""+appVersion+"\",\"appId\":\""+appId+"\"},"
				+ "\"body\":{" +
				"\"sessionId\":\""+sessionId+"\",\"maccode\":\""+mac+"\"}}";
		return params;
	}
	public static String checkPuBind(String mac){
		String params="";
		String code = "checkPuBind";
		String serial = MacUtils.mac2serial(mac);
		String reqTime =System.currentTimeMillis()+"";
		StringBuilder src = new StringBuilder();
		src.append(code).append(reqTime).append(mac).append(v)
				.append(appVersion).append(appId)
				.append(mac).append(serial);

		String accessToken = MD5.getMD5(src.toString(),"UTF-8");
		params = "{\"head\":{\"code\":\""+code+"\",\"reqTime\":\""+reqTime+"\",\"accessKey\":\""+mac+"\",\"accessToken\":\""+accessToken
				+"\",\"v\":\""+v+"\",\"appVersion\":\""+appVersion+"\",\"appId\":\""+appId+"\"},"
				+ "\"body\":{"+"\"maccode\":\""+mac+"\"}}";
		return params;
	}
	public static String getPuState(String mac,int sessionId,String puName,String puId){
		String params="";
		String code = "getReversePuState";
		String serial = MacUtils.mac2serial(mac);
		String reqTime =System.currentTimeMillis()+"";
		StringBuilder src = new StringBuilder();
		src.append(code).append(reqTime).append(mac).append(v)
				.append(appVersion).append(appId).append(sessionId)
				.append(mac).append(puName).append(puId).append(serial);

		String accessToken = MD5.getMD5(src.toString(),"UTF-8");
		params = "{\"head\":{\"code\":\""+code+"\",\"reqTime\":\""+reqTime+"\",\"accessKey\":\""+mac+"\",\"accessToken\":\""+accessToken
				+"\",\"v\":\""+v+"\",\"appVersion\":\""+appVersion+"\",\"appId\":\""+appId+"\"},"
				+ "\"body\":{" +
				"\"sessionId\":\""+sessionId+"\",\"maccode\":\""+mac+"\",\"reqMaccode\":\""+puName+"\",\"reqPuId\":\""+puId+"\"}}";
		return params;
	}
	public static String bindPu(String id,String mac)throws Exception{
		String userId= Base64.decode(id,"UTF-8");
		if (userId.equals("")){
			return "";
		}
		JSONObject jsonObject=new JSONObject(userId);

		String kind="300";
		String model="356";
		String nickname="智能设备";
		String longitude="";
		String cuId="100066";
		String cuSession="1476133";
		String latitude="";
		if (jsonObject!=null){
			kind=jsonObject.getString("kind");
			model=jsonObject.getString("model");
			nickname=jsonObject.getString("nickname");
			longitude=jsonObject.getString("longitude");
			latitude=jsonObject.getString("latitude");
			cuId=jsonObject.getString("cuId");
			cuSession=jsonObject.getString("cuSession");
		}

		String params="";
		String code = "bindByPu";
		String serial = MacUtils.mac2serial(mac);
		String reqTime =System.currentTimeMillis()+"";
		StringBuilder src = new StringBuilder();
		src.append(code).append(reqTime).append(mac).append(v)
				.append(appVersion).append(appId)
				.append(mac).append(kind).append(model).
				append(nickname).append(longitude).
				append(latitude).append(cuId).
				append(cuSession).append(serial);
		String accessToken = MD5.getMD5(src.toString(),"UTF-8");
		params = "{\"head\":{\"code\":\""+code+"\",\"reqTime\":\""+reqTime+"\",\"accessKey\":\""+mac+"\",\"accessToken\":\""+accessToken
				+"\",\"v\":\""+v+"\",\"appVersion\":\""+appVersion+"\",\"appId\":\""+appId+"\"},"
				+ "\"body\":{" +
				"\"cuSession\":\""+cuSession+"\",\"maccode\":\""+mac+"\",\"cuId\":\""+cuId+
				"\",\"latitude\":\""+latitude+"\",\"longitude\":\""+longitude+
				"\",\"kind\":\""+kind+"\",\"model\":\""+model+"\",\"nickname\":\""+nickname+"\"}}";
		return params;
	}

	public static Object[][]  getParmas(String parmas){
		return new Object[][]{
				new Object[] {
						"request",
						parmas },
		};

	}
}
