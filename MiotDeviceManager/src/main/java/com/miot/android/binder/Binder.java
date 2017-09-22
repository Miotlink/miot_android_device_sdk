package com.miot.android.binder;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public abstract class Binder extends android.os.Binder {

	public abstract boolean Login(String username,String password)throws Exception;

	public abstract String sendPuToPu(Integer puId,String uart)throws Exception;

	public abstract String sendPuToCu(Integer id,String userData)throws Exception;

	public abstract void Logout()throws Exception;
}
