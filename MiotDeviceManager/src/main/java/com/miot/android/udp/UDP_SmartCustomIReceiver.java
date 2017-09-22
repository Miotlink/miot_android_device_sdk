package com.miot.android.udp;

import android.content.Context;

import com.miot.android.listener.IReceiver;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class UDP_SmartCustomIReceiver implements IReceiver {

	private static UDP_SmartCustomIReceiver instance = null;

	private int port = 0;


	private UDP_CustomSocket udpSocket = null;

	public static UDP_SmartCustomIReceiver getInstance(Context context) {
		if (instance == null) {
			synchronized (UDP_SmartIReceiver.class) {
				if (instance == null) {
					instance = new UDP_SmartCustomIReceiver(context);
				}
			}
		}
		return instance;
	}

	private UDP_SmartCustomIReceiver(Context context) {

		if (udpSocket==null) {
			udpSocket = new UDP_CustomSocket(context);
		}
	}


	/**
	 * 监听端口初始化
	 *
	 * @param port
	 */
	public void init(int port) {

		this.port = port;

		udpSocket.startRecv(port, this);
	}

	public static String Charset = "ISO-8859-1";
	public static String getMlccContent(byte[] bs, int len) {
		try {
			if (bs == null || len < 20) {
				return null;
			}
			return new String(bs, 20, len - 20,Charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public void onReceive(int localPort, String host, int port, byte[] bs, int len) {
		try {
			String msg=new String(bs,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}



	}
}
