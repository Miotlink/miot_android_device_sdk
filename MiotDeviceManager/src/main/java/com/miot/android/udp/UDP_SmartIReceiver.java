package com.miot.android.udp;

import android.content.Context;

import com.miot.android.listener.IReceiver;
import com.miot.android.sdk.MiotSDKInitializer;

import java.io.UnsupportedEncodingException;

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

	private UDP_SmartIReceiver(Context context) {
		this.context = context;
	}



	/**
	 * 监听端口初始化
	 *
	 * @param port
	 */
	public void init(int port) {
		this.port = port;
		udpSocket = new UDPSocket(context);
		udpSocket.startRecv(port, this);
	}
	public void sendRobotInfo(String ip,int port,String content) {
		byte[] bs = null;
		try {
			if (content.isEmpty()) {
				return;
			}
			bs = content.getBytes("UTF-8");
			sendUdp(ip, port, bs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private boolean sendUdp(String ip, int port, byte[] content) {
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

	public boolean sendUdpVsp(byte[] content) {
		try {
			if (udpSocket!=null) {
				udpSocket.send("255.255.255.255", 65536, content, content.length);
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
			String msg = new String(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
