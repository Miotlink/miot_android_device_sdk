package com.miot.android.base;

import android.app.Service;
import android.os.Handler;
import android.os.Message;

import com.cncrit.qiaoqiao.VspCallback;
import com.miot.android.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public  abstract class BaseService extends Service implements VspCallback{

	protected SharedPreferencesUtil sharedPreferencesUtil=null;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferencesUtil=SharedPreferencesUtil.getInstance(this);
	}

	public Handler mBaseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				updateUI(msg.obj, msg.what, msg.arg1 == -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	protected abstract void updateUI(Object object, int cmd, boolean isFailed) throws Exception;

}
