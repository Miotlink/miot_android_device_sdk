package com.miot.android.listener;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public interface DeviceStateOnReceiver {
	/**
	 * 接收设备端回来的数据
	 * @param message
	 * @throws Exception
	 */
	public void onReceiverDeviceRes(String message)throws Exception;
}
