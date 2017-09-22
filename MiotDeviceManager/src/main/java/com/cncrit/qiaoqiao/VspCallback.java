package com.cncrit.qiaoqiao;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public interface VspCallback {

	/**
	 *
	 * 设备登录返回数据包
	 */
	public void loginRes(int errorCode,String errorMessage,String data) throws Exception;

	/**
	 * 设备登录平台心跳超时
	 */
	public void timeOut();

	/**
	 * 设备发送
	 */
	public void commonRes(int type,int error,String errorMessage,String data)throws Exception;

	/**
	 *监听vsp 返回数据包
	 * @param id
	 * @param uart
	 */
	public void onReceiverDeviceUart(int id, String uart)throws Exception;


	/**
	 * 设备登出
	 * @param id
	 */
	public void logout(int id)throws Exception;

}
