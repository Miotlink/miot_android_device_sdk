package com.miot.android.listener;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public interface IReceiver {
	public void onReceive(int localPort, String host, int port, byte[] bs, int len)throws Exception;
}
