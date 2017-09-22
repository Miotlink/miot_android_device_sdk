package com.miot.android.binder;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class BinderManager {

	private PlatformBind platformBind=null;

	private static BinderManager instance=null;

	public PlatformBind getPlatformBind() {
		return platformBind;
	}

	public static synchronized BinderManager getInstance() {
		if (instance==null){
			synchronized (BinderManager.class){
				if (instance==null){
					instance=new BinderManager();
				}
			}
		}
		return instance;
	}

	public void setPlatformBind(PlatformBind platformBind) {
		this.platformBind = platformBind;
	}
}
