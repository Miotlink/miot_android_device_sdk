package com.miot.android.webservice;



import com.miot.android.utils.MLContent;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;


public class WebService {

	private String nameSpace = MLContent.NAMESPACE;
	private String endPoint = MLContent.ENDPOINT;

	public WebService(){

	}
	
	public WebService(String nameSpace, String endPoint){
		this.nameSpace = nameSpace;
		this.endPoint = endPoint;
	}

	public synchronized String call(String methodName, Object[][] params){
		String soapAction = nameSpace + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		if ( params != null ){
			for(Object[] objs : params){
				rpc.addProperty((String)objs[0],objs[1]);
			}
		}

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版�?
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = rpc;
		envelope.dotNet = false;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint,20000);

		try {
			transport.call(soapAction, envelope);
			Object object = envelope.getResponse();
			return object.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public synchronized String call(String methodName,List<Object> list1, List<Object> list2){
        String soapAction = nameSpace + methodName;
		
		// 指定WebService的命名空间和调用的方法名  
        SoapObject rpc = new SoapObject(nameSpace, methodName);

		if (list1 != null && list2 != null) {
			for (int i = 0; i < list1.size(); i++) {
				rpc.addProperty((String) list1.get(i),list2.get(i));
			}
		}

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版�? 
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut = rpc;  
        envelope.dotNet = false;  
        envelope.setOutputSoapObject(rpc);  
        HttpTransportSE transport = new HttpTransportSE(endPoint,15000);
        try {
              transport.call(soapAction, envelope);  
	          Object object = envelope.getResponse();
	          return object.toString();
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";
        }  
  
	}
	public static boolean isCallSuccess(String responseString){
		return responseString.indexOf("resultCode\":1}]")>=0;
	}
	
	public static String getDataJsonString(String responceString){
        String so = responceString.substring(11,responceString.indexOf("]\",\"errorMsg"));
        so = so.replaceAll("\\\\", "");
        return so;
	}

}
