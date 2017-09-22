package com.miot.android.task;

import android.os.AsyncTask;

import com.miot.android.utils.MLContent;

import java.net.InetAddress;

public class HostAddressTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String ip_devdiv = null;
		try {
			InetAddress x = InetAddress.getByName(params[0]);
			ip_devdiv = x.getHostAddress();// 得到字符串形式的ip地址
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip_devdiv;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			MLContent.FORMAL_SERVER_IP=result;
		}
	}

}
