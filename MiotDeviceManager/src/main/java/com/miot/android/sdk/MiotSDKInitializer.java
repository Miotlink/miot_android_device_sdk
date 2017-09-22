package com.miot.android.sdk;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.binder.BinderManager;
import com.miot.android.binder.PlatformBind;
import com.miot.android.listener.DeviceStateOnReceiver;
import com.miot.android.service.SmartService;
import com.miot.android.task.HostAddressTask;
import com.miot.android.utils.ACache;
import com.miot.android.utils.JSONUitls;
import com.miot.android.utils.MLContent;
import com.miot.android.utils.MacUtils;
import com.miot.android.utils.MmwParseUartUtils;
import com.miot.android.utils.SharedPreferencesUtil;
import com.miot.android.webservice.WebServerManager;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class MiotSDKInitializer {

	private static MiotSDKInitializer instance=null;

	public static String MAC="";

	public static final String SERVICE_ACTION="com.miot.android.service.Smart";

	private static final int SERVICE_HAS_START=10001;
	private static final int SERVICE_START_MACERROR=10001;
	private static final int SERVICE_START_FAIL=10002;

	SharedPreferencesUtil sharedPreferencesUtil=null;

	public static synchronized MiotSDKInitializer getInstance() {
		if (instance==null){
			synchronized (MiotSDKInitializer.class){
				if (instance==null){
					instance=new MiotSDKInitializer();
				}
			}
		}
		return instance;
	}


	public Context context=null;

	private MyServiceConnection myServiceConnection=null;

	/**
	 * 初始化方式
	 * @param context  上下文
	 * @param mac  【设备的唯一MAC】  设置的MAC 必须向妙联平台进行认证，如不认证 登录则失败。
	 * @return
	 * @throws Exception
	 */
	public int init(Context context,String mac) throws Exception{
		new HostAddressTask().execute(MLContent.FORMAL_URL);
		if (mac.equals("")){
			throw new Exception("mac is error");
		}
		if(MacUtils.isMacAddress(mac)){

			return SERVICE_START_MACERROR;
		}
		SharedPreferencesUtil.getInstance(context).setMac(mac.toUpperCase());
		MiotSDKInitializer.MAC=mac.toUpperCase();
		if (context==null){
			return SERVICE_START_FAIL;
		}
		sharedPreferencesUtil=SharedPreferencesUtil.getInstance(context);
		this.context=context;
		if (myServiceConnection==null){
			myServiceConnection=new MyServiceConnection();
		}
		Intent smartService=new Intent(context, SmartService.class);
		smartService.setAction(MiotSDKInitializer.SERVICE_ACTION);
		smartService.setPackage(context.getPackageName());
		context.startService(smartService);
		context.bindService(smartService,myServiceConnection, Service.BIND_AUTO_CREATE);
		return 1;
	}


	private DeviceStateOnReceiver onReceiver=null;

	public void setOnReceiver(DeviceStateOnReceiver onReceiver) {
		this.onReceiver = onReceiver;
		VspOperation.deviceStateOnReceiver=onReceiver;
	}
	/**
	 *获取登录设备的唯一ID
	 * @return
	 * @throws Exception
	 */
	public int getId()throws Exception{
		if (context==null){
			throw new Exception("context is null");
		}
		if (!sharedPreferencesUtil.getLogin()){
			return MLContent.MIOT_INIT_GET_PULIST_LOGIN;
		}
		if (sharedPreferencesUtil.getPu().equals("")){
			return MLContent.MIOT_INIT_GET_PULIST_SEENEDID;
		}
		return JSONUitls.getId(sharedPreferencesUtil.getPu());
	}

	/**
	 * 发送数据控制设备
	 * @param id 需要向设备发送的唯一ID
	 * @param uart 串口数据格式为F1F1*********7E结尾
	 * @return
	 */
	public void miotlinkPlatform_sendToPu(Integer id,String puName,String uart) throws Exception{
		if (onReceiver==null){
			throw new Exception("onReceiver null");
		}
		if (!sharedPreferencesUtil.getLogin()){
			if (onReceiver!=null){
				onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_GET_PULIST_LOGIN,SmartService.errorMessage,""));
			}
		}
		String state=ACache.get(context).getAsString(id+"");
		if (state==null){
			String res=WebServerManager.getInstance().getPuState
					(MiotSDKInitializer.MAC,JSONUitls.getSessionId(sharedPreferencesUtil.getPu()),puName,id+"");
			if (res.equals("")){
				if (onReceiver!=null){
					onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_GET_PULIST_SERVICE_FAIL,"请求服务器失败",""));
				}
				return ;
			}
			JSONObject jsonObject=new JSONObject(res);
			if (jsonObject==null){
				if (onReceiver!=null){
					onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_GET_PULIST_SERVICE_FAIL,"请求服务器失败",""));
				}
				return ;
			}
			JSONObject body=new JSONObject(jsonObject.getString("body"));
			if (body==null){
				if (onReceiver!=null){
					onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_GET_PULIST_SERVICE_FAIL,"请求服务器失败",""));
				}
			}
			if (body.getString("resultCode").equals("1")){
				JSONObject jsonObject1=new JSONObject(body.getString("data"));
				if (!jsonObject1.isNull("state")){
					ACache.get(context).put(id+"",jsonObject1.getString("state"),2*60);
					if (jsonObject1.getString("state").equals("0")){
						if (onReceiver!=null){
							onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_PU_ONSTATE,"设备已经离线",""));
						}
						return;
					}
					BinderManager.getInstance().getPlatformBind().sendPuToPu(id,MmwParseUartUtils.doLinkBindMake(uart));
				}
				return;
			}
			if (onReceiver!=null){
				onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(Integer.parseInt(jsonObject.getString("resultCode")),jsonObject.getString("resultMsg"),""));
			}
			return;
		}
		if (state.equals("0")){
			if (onReceiver!=null){
				onReceiver.onReceiverDeviceRes(JSONUitls.getJSONResult(MLContent.MIOT_INIT_PU_ONSTATE,"设备已经离线",""));
			}
			return;
		}
		String s=BinderManager.getInstance().getPlatformBind().sendPuToPu(id,MmwParseUartUtils.doLinkBindMake(uart));
		if (onReceiver!=null){
			onReceiver.onReceiverDeviceRes(s);
		}
		return;
	}

	/**
	 * 发送数据控制场景
	 * @param deamonCuId 场景的唯一ID
	 * @param sceneId 场景执行的数据
	 * @return
	 */
	public String miotlinkPlatform_sendToScene(Integer deamonCuId,Integer sceneId)throws Exception{

		if (sceneId.equals("")||deamonCuId==0){
			return JSONUitls.getJSONResult(0,"deamonCuId||sceneId  null ","");
		}
		if (!sharedPreferencesUtil.getLogin()){
			return JSONUitls.getJSONResult(MLContent.MIOT_INIT_GET_PULIST_LOGIN,"未登录","");
		}
		String data=JSONUitls.getSceneCu(sceneId+"");
		return BinderManager.getInstance().getPlatformBind().sendPuToCu(deamonCuId,data);
	}

	/**
	 * 获取设备列表和场景列表
	 * @return
	 * @throws Exception
	 *

	 */
	public String miotlinkPlatform_getDeviceList() throws Exception{
		String result="";
		if (!sharedPreferencesUtil.getLogin()){
			return JSONUitls.getErrorMessage(MLContent.MIOT_INIT_GET_PULIST_LOGIN+"",SmartService.errorMessage);
		}
		if (sharedPreferencesUtil.getPu().equals("")){
			return JSONUitls.getErrorMessage(MLContent.MIOT_INIT_GET_PULIST_SEENEDID+"","sessionId  error");
		}
		if (sharedPreferencesUtil.getMac().equals("")){
			return JSONUitls.getErrorMessage(MLContent.MIOT_INIT_GET_PULIST_MAC_ERROR+"","mac is error");
		}
		result=WebServerManager.getInstance().getOpenApiReverseGetThings(MAC,JSONUitls.getSessionId(sharedPreferencesUtil.getPu()));
		if (result.equals("")){
			return JSONUitls.getErrorMessage(MLContent.MIOT_INIT_GET_PULIST_SERVICE_FAIL+"","请求服务器失败");
		}
		JSONUitls.aCacheAll(context,result);
		return result;
	}



	class  MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			if (iBinder instanceof PlatformBind) {
				BinderManager.getInstance().setPlatformBind((PlatformBind) iBinder);
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName componentName) {
		}
	}

}
