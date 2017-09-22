package com.miot.android.udp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.miot.android.listener.IReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class UDP_CustomSocket implements  Runnable{

	public static String TAG = UDP_CustomSocket.class.getName();


	private IReceiver receiver = null;

	private DatagramSocket socket = null;

	private Thread thread = null;
	int localPort = 0;
	private Context context=null;

	private  WifiManager.MulticastLock lock = null;

	public UDP_CustomSocket(Context context){
		this.context=context;
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		lock = manager.createMulticastLock("wifi");
	}

	public boolean startRecv(int port, IReceiver lrs) {
		try {
			Log.e("listener port","port="+port);
			this.localPort=port;
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(localPort));
			}
			this.receiver = lrs;
			thread = new Thread(this);
			thread.start();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}

	public boolean needStop = false;

	private InetAddress address = null;


	private DatagramPacket dPacket = null;

	public boolean send(String ip, int port, byte[] bs, int len) {
		try {
			address = InetAddress.getByName(ip);
			dPacket = new DatagramPacket(bs, len, address, port);
			dPacket.getPort();
			if (lock != null) {
				lock.acquire();
			}
			socket.send(dPacket);
			if (lock != null) {
				lock.release();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public void run() {
		try {
			if (socket == null || receiver == null) {
				Log.e(TAG, "run: socket and receiver should not be null!");
				return;
			}
			while (!needStop) {
				byte data[] = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				String msg = new String(packet.getData(), packet.getOffset(),
						packet.getLength());
				byte[] bs = new byte[packet.getLength()];
				System.arraycopy(packet.getData(), packet.getOffset(), bs, 0,
						packet.getLength());
				String host = packet.getAddress().getHostAddress();
				int port = packet.getPort();
				if (receiver != null) {
					try {
						receiver.onReceive(localPort, host, port, bs, bs.length);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
