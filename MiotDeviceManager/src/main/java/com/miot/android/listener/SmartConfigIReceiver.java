package com.miot.android.listener;

/**
 * Created by Administrator on 2017/9/25 0025.
 */
public interface SmartConfigIReceiver {
	/**
	 * 判断设备是否绑定成功
	 * @param message
	 * @throws Exception
	 */
	public void onSmartConfigIReceiver(String message)throws Exception;


}
