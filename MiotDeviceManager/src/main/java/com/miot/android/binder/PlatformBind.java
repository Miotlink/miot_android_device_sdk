package com.miot.android.binder;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.utils.JSONUitls;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class PlatformBind extends Binder  {
	@Override
	public boolean Login(String username, String password)throws Exception {
		if (VspOperation.LoginPu(username,password)){
			return true;
		}
		return false;
	}

	@Override
	public String sendPuToPu(Integer puId, String uart)throws Exception {
		if (VspOperation.puToPu(puId,uart,1,0)){
			return JSONUitls.getJSONResult(1,"success","");
		}
		return JSONUitls.getJSONResult(-1,"send fail","");
	}

	@Override
	public String sendPuToCu(Integer id, String userData)throws Exception {
		if (VspOperation.puToPu(id,userData,1,1)){
			return JSONUitls.getJSONResult(1,"success","");
		}
		return JSONUitls.getJSONResult(-1,"send fail","");
	}

	@Override
	public void Logout() throws Exception{
		VspOperation.LogoutPu();
	}
}
