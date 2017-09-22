package com.miot.android.sdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.miot.android.listener.DeviceStateOnReceiver;
import com.miot.android.utils.MacUtils;
import com.miot.android.utils.NetUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,DeviceStateOnReceiver{

	private Button pulist_btn;

	private Button send_pu_to_pu;
	private Button send_pu_scene;
	private EditText uart_data;

	private void initView(){
		pulist_btn=(Button)findViewById(R.id.getPuList);
		send_pu_to_pu=(Button)findViewById(R.id.send_pu_to_pu);
		send_pu_scene=(Button)findViewById(R.id.send_pu_scene);
		uart_data=(EditText)findViewById(R.id.uart_data);
		pulist_btn.setOnClickListener(this);
		send_pu_to_pu.setOnClickListener(this);
		send_pu_scene.setOnClickListener(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.miot_activity_main);
		initView();
		MiotSDKInitializer.getInstance().setOnReceiver(this);
		Log.e("WIFI MAC",NetUtil.getWifiMacAddress()) ;
		Log.e("IP",NetUtil.getHostAddress(this));
		String s=MacUtils.mac2serial("00:0E:A3:BD:E8:BF");
		Log.e("---",s);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.getPuList:
				new Thread(){
					@Override
					public void run() {
						super.run();

						try {
							String s=MiotSDKInitializer.getInstance().miotlinkPlatform_getDeviceList();
							Log.e("--",s);
						} catch (Exception e) {
							e.printStackTrace();
						}



					}
				}.start();
				break;
			case R.id.send_pu_to_pu:
				new Thread(){
					@Override
					public void run() {
						super.run();

						try {
							MiotSDKInitializer.getInstance().miotlinkPlatform_sendToPu(165382,"AC:CF:23:E4:5D:76","F1F1010201FF037E");
						} catch (Exception e) {
							e.printStackTrace();
						}



					}
				}.start();


				break;
			case R.id.send_pu_scene:
				break;
		}

	}

	@Override
	public void onReceiverDeviceRes(String message) throws Exception {

		Log.e("message",message);
	}
}
