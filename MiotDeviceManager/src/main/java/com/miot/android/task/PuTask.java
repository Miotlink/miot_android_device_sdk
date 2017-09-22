package com.miot.android.task;

import android.os.AsyncTask;
import android.os.Handler;

import com.miot.android.binder.PlatformBind;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class PuTask extends AsyncTask<String, Void, String> {

	private Handler handler=null;

	private PlatformBind platformBind=null;

	private int what=0;

	public PuTask(PlatformBind platformBind, Handler handler){
		this.handler=handler;
		this.platformBind=platformBind;
	}

	@Override
	protected String doInBackground(String... strings) {
		what=Integer.parseInt(strings[0]);
		switch (what){
			case 100001:
				try {
					platformBind.Login(strings[1],strings[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 100002:
				try {
					platformBind.sendPuToPu(Integer.parseInt(strings[1]),strings[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 100003:
				try {
					platformBind.Logout();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

		}
		return "";
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);

	}
}
