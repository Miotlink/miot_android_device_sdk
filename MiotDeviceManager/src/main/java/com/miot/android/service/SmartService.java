package com.miot.android.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.base.BaseService;
import com.miot.android.binder.PlatformBind;
import com.miot.android.sdk.MiotSDKInitializer;
import com.miot.android.task.HostAddressTask;
import com.miot.android.task.PuTask;
import com.miot.android.utils.ACache;
import com.miot.android.utils.MLContent;
import com.miot.android.utils.MacUtils;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class SmartService extends BaseService{


	private PlatformBind platformBind=null;

	public static String errorMessage="未登录";


	@Override
	public void onCreate() {
		super.onCreate();
		if (platformBind==null){
			platformBind=new PlatformBind();
		}
		String localHost= ACache.get(this).getAsString(MLContent.FORMAL_SERVER_IP);
		if (localHost==null){
			new HostAddressTask().execute(MLContent.FORMAL_SERVER_URL);
		}else{
			VspOperation.rsIp=localHost;
		}
		if (!MiotSDKInitializer.MAC.equals("")||!sharedPreferencesUtil.getMac().equals("")){
			new PuTask(platformBind,mBaseHandler).execute("100001",MiotSDKInitializer.MAC,
					MacUtils.mac2serial(MiotSDKInitializer.MAC));
		}
		VspOperation.vspCallback=this;
		sharedPreferencesUtil.setLogin(false);
	}

	@Override
	protected void updateUI(Object object, int cmd, boolean isFailed) throws Exception {
		switch (cmd){
			case 100006:
				VspOperation.vspCallback=this;
				if (ACache.get(this).getAsString(MLContent.FORMAL_SERVER_IP)==null){
					new HostAddressTask().execute(MLContent.FORMAL_SERVER_URL);
				}
				new PuTask(platformBind,mBaseHandler).execute("100001",MiotSDKInitializer.MAC,
						MacUtils.mac2serial(MiotSDKInitializer.MAC));
				break;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return platformBind;
	}

	@Override
	public void loginRes(int errorCode, String errorMessage, String data) throws Exception {
		Log.e("error",data);
		Log.e("error",errorMessage);
		if (ACache.get(this).getAsString(MLContent.FORMAL_SERVER_IP)==null){
			ACache.get(this).put(MLContent.FORMAL_SERVER_URL,VspOperation.rsIp,24*60*60);
		}
		if (errorCode==1){
			mBaseHandler.removeMessages(100006);
			sharedPreferencesUtil.setLogin(true);
			sharedPreferencesUtil.setPu(data);
			return;
		}
		if (errorCode==-3){
			SmartService.errorMessage="妙联平台未认证此设备MAC";
		}
		new PuTask(platformBind,mBaseHandler).execute(100003+"");
		//登录失败，5s登录一次
		mBaseHandler.sendEmptyMessageDelayed(100006,5000);
		sharedPreferencesUtil.setLogin(false);
	}

	@Override
	public void timeOut() {
		mBaseHandler.sendEmptyMessage(100006);
	}

	@Override
	public void commonRes(int type, int error, String errorMessage, String data) throws Exception {

	}

	@Override
	public void onReceiverDeviceUart(int id, String uart) throws Exception {

	}

	@Override
	public void logout(int id) throws Exception {
		new PuTask(platformBind,mBaseHandler).execute(100003+"");
		mBaseHandler.sendEmptyMessageDelayed(100006,3000);
	}


}
