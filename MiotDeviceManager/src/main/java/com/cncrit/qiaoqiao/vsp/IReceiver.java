package com.cncrit.qiaoqiao.vsp;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public interface IReceiver {

	public void onReceive(byte[] recvData, int recvLen);
}
