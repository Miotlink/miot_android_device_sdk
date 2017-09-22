package com.cncrit.qiaoqiao.utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class VspDataUtils {
	public static JSONObject getJSONObject(int id,Object... objects)throws Exception{
		JSONObject jsonObject=new JSONObject();
		if (objects.length>1){
			jsonObject.put("type",objects[0]);
			jsonObject.put("id",id);
			jsonObject.put("uart",objects[1]);
			return jsonObject;
		}
		jsonObject.put("id",id);
		jsonObject.put("sessionId",objects[0]);
		return jsonObject;
	}


}
