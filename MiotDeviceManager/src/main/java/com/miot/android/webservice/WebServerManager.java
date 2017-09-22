package com.miot.android.webservice;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class WebServerManager extends ServiceOpenInterface {

	private static  WebServerManager instance=new WebServerManager();

	public static WebServerManager getInstance() {
		return instance;
	}

	private static final String methodName="service";

	private WebService webService=new WebService();
	@Override
	public String getOpenApiReverseGetThings(String mac, int sessionId) throws Exception{
		String result="";
		result = webService.call(methodName, ParmasUitls.getParmas(ParmasUitls.getPuListAndSceneList(mac,sessionId)));
		return result ;
	}

	@Override
	public String getPuState(String mac,int sessionId,String puName,String puId) throws Exception {
		return webService.call(methodName, ParmasUitls.getParmas(ParmasUitls.getPuState(mac,sessionId,puName,puId)));
	}
}
