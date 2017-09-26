package com.miot.android.udp;

import android.content.Context;

import com.miot.android.listener.IReceiver;
import com.miot.android.listener.SmartConfigIReceiver;
import com.miot.android.sdk.MiotSDKInitializer;
import com.miot.android.utils.VspContent;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class UDP_SmartIReceiver implements IReceiver {

	private static UDP_SmartIReceiver instance = null;

	private int port = 0;

	private Context context;

	private UDPSocket udpSocket = null;

	public static UDP_SmartIReceiver getInstance() {
		if (instance == null) {
			synchronized (UDP_SmartIReceiver.class) {
				if (instance == null) {
					instance = new UDP_SmartIReceiver(MiotSDKInitializer.getInstance().context);
				}
			}
		}
		return instance;
	}

	private SmartConfigIReceiver smartConfigIReceiver=null;

	public void setSmartConfigIReceiver(SmartConfigIReceiver smartConfigIReceiver) {
		this.smartConfigIReceiver = smartConfigIReceiver;
	}

	private UDP_SmartIReceiver(Context context) {
		this.context = context;
	}

	/**
	 * 监听端口初始化
	 *
	 * @param port
	 */
	public void init(int port) throws Exception{
		this.port = port;
		udpSocket = new UDPSocket(context);
		udpSocket.startRecv(port, this);
	}

	public boolean send(String ip, int port, byte[] content) {
		try {
			if (udpSocket!=null) {
				udpSocket.send(ip, port, content, content.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}



	@Override
	public void onReceive(int localPort, String host, int port, byte[] bs, int len) {
		try {
			byte [] bytes=VspContent.decodeMlccMsg(bs);
			String msg = VspContent.getMlccContent(bytes,bytes.length);
			if (smartConfigIReceiver!=null){
				smartConfigIReceiver.onSmartConfigIReceiver(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
