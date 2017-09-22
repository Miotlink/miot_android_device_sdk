package com.miot.android.webservice;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public abstract class ServiceOpenInterface {



	/**
	 * 获取平台设备列表和场景列表
	 * @param mac
	 * @param sessionId
	 * @return
	 */
	public abstract String getOpenApiReverseGetThings(String mac,int sessionId)throws Exception;
	public abstract String getPuState(String mac,int sessionId,String puName,String puId)throws Exception;
}
