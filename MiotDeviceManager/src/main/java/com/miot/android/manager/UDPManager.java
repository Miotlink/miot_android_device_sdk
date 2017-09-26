package com.miot.android.manager;

import com.miot.android.listener.SmartConfigIReceiver;
import com.miot.android.sdk.MiotSDKInitializer;
import com.miot.android.udp.UDP_SmartIReceiver;
import com.miot.android.utils.JSONUitls;
import com.miot.android.utils.VspContent;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25 0025.
 */
public class UDPManager implements SmartConfigIReceiver{

	public static UDPManager instance=null;

	public static synchronized UDPManager getInstance() {
		if (instance==null){
			synchronized (UDPManager.class){
				if (instance==null){
					instance=new UDPManager();
				}
			}
		}
		return instance;
	}

	private MyThread myThread=null;

	private UDP_SmartIReceiver smartIReceiver=UDP_SmartIReceiver.getInstance();;

	public void init()throws Exception{
		smartIReceiver.init(64540);

	}

	private String userId="";



	public void start(String userId){
		this.userId=userId;
		smartIReceiver.setSmartConfigIReceiver(this);
		myThread=new MyThread();
		myThread.start();
	}



	public boolean send()throws Exception{
		byte[] bytes= JSONUitls.getSmartConfigMessageRes(JSONUitls.getSmartConfigMessage(userId, MiotSDKInitializer.MAC)).getBytes("ISO-8859-1");
		return smartIReceiver.send("255.255.255.255",63541,VspContent.formatLsscCmdBuffer(bytes));
	}

	private boolean isRun=true;

	@Override
	public void onSmartConfigIReceiver(String message) throws Exception {
		if (message.equals("")){
			return;
		}
		JSONObject jsonObject=new JSONObject(message);
		if (jsonObject==null){
			return;
		}
		if (jsonObject.getString("resultCode").equals("1")){
			JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));
			if (jsonObject1==null){
				return;
			}
			if (jsonObject1.getString("codeName").equals("smartConfigFin")){
				if(jsonObject1.getString("mac").equals(MiotSDKInitializer.MAC)){
					onDestory();
				}
			}
		}
	}


	class  MyThread extends Thread{

		private int count=0;
		@Override
		public void run() {
			super.run();
			while (isRun){
				try {
					if (count>10){
						onDestory();
						return;
					}
					send();
					sleep(2000);
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public  void onDestory(){
		if (isRun){
			isRun=false;
		}
		if (myThread!=null){
			myThread.interrupt();
			myThread=null;
		}
	}
}
