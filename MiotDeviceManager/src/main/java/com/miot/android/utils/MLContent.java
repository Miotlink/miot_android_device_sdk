package com.miot.android.utils;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class MLContent {


	public static boolean DEVELOPMENT_ENVIRONMENT=false;
//	public static final String FORMAL_SERVER_URL="118.190.67.214";
//	public static String FORMAL_SERVER_IP="118.190.67.214";
//	public final static String FORMAL_URL="118.190.67.214";
	public static  String FORMAL_SERVER_URL="60.191.23.28";
	public static String FORMAL_SERVER_IP="60.191.23.28";


//	public final static String FORMAL_URL="dev.51miaomiao.com";
	public static  String FORMAL_URL="60.191.23.28";
	public static String ENDPOINT = "http://"
			+ FORMAL_URL
			+ ":88/axis2/services/openBuzService";
	public final static String NAMESPACE = "http://www.miotlink.org/openBuzService/";

	public static final void setDevelopmentEnvironment(boolean isTest){
		if (isTest){
			FORMAL_SERVER_URL="60.191.23.28";
			FORMAL_SERVER_IP="60.191.23.28";
			FORMAL_URL="60.191.23.28";
			ENDPOINT = "http://" + FORMAL_URL + ":88/axis2/services/openBuzService";
		}else {
			FORMAL_SERVER_URL="www.51miaomiao.com";
			FORMAL_SERVER_IP="118.190.67.214";
			FORMAL_URL="dev.51miaomiao.cn";
			ENDPOINT = "http://" + FORMAL_URL + ":80/axis2/services/openBuzService";
		}
	}

	public static final int MIOT_INIT_GET_PULIST_LOGIN=10002;
	public static final int MIOT_INIT_GET_PULIST_SEENEDID=10003;
	public static final int MIOT_INIT_GET_PULIST_MAC_ERROR=10004;
	public static final int MIOT_INIT_GET_PULIST_SERVICE_FAIL=10005;
	public static final int MIOT_INIT_GET_PULIST_=10002;
	public static final int MIOT_INIT_PU_ONSTATE=10013;
	public static final int MIOT_INIT_PU_NO_EXIT=10014;
	public static final int MIOT_INIT_PU_CLIENT_FAIL=10015;
}
