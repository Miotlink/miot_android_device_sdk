package com.miot.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by admin on 2016/7/1
 */
public class SharedPreferencesUtil {
    /**
     * The settings.
     */
    private SharedPreferences settings;

    /**
     * The Constant sPrefsFileName.
     */
    public static final String sPrefsFileName = "MiotBox_Manager";

    /**
     * The editor.
     */
    private SharedPreferences.Editor editor;

    /**
     * The instance.
     */
    private static SharedPreferencesUtil instance;

    /**
     * The lock.
     */
    private final Lock lock = new ReentrantLock();

    /**
     * The current account.
     */
    private static String currentAccount;

    public final String GUIDE_MASK = "guide_mask";

    /**
     * Gets the single instance of SharedPreferencesUtil.
     *
     * @param context the context
     * @return single instance of SharedPreferencesUtil
     */
    public static SharedPreferencesUtil getInstance(Context context) {
        return getInstance(context, "com.box.share.preference.common");
    }

    /**
     * Instantiates a new shared preferences util.
     *
     * @param context the context
     * @param account the account
     */
    private SharedPreferencesUtil(Context context, String account) {
        String fileName = sPrefsFileName;
        currentAccount = account;
        if (!TextUtils.isEmpty(account)) { // 如果有用户名，则各自存储，否则存在公用的
            fileName = account + sPrefsFileName;
            // userAccount = account;
        } else {
            throw new IllegalArgumentException("account can not be empty.");
        }
        lock.lock();
        try {
            settings = context.getApplicationContext().getSharedPreferences(
                    fileName, Context.MODE_PRIVATE);
            editor = settings.edit();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
    }

    /**
     * 配置是不区分用户名存储的，即所有用户都共享这份存储.
     *
     * @param context the context
     * @param account the account
     * @return single instance of SharedPreferencesUtil
     */
    public static SharedPreferencesUtil getInstance(Context context,
                                                    String account) {
        if (instance == null) {
            instance = new SharedPreferencesUtil(context, account);
        } else {
            if (TextUtils.isEmpty(account)) {
                throw new IllegalArgumentException("account can not be empty.");
            } else if (!account.equals(currentAccount)) {
                instance = new SharedPreferencesUtil(context, account);
            }
        }
        return instance;
    }


    public SharedPreferences getSharedPreferences() {
        return settings;
    }

    /**
     * Gets the editor.
     *
     * @return the editor
     */
    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static  final String MAC_SN="MAC";
    public  static  final String MAC_LOGIN="login";
    public  static  final String DEVICE="device";

    public void setLogin(boolean isLogin) {
        editor.putBoolean(MAC_LOGIN,isLogin);
        editor.commit();
    }

    public boolean getLogin() {
        return settings.getBoolean(MAC_LOGIN,false);
    }

    public void setPu(String data){
        editor.putString(DEVICE,data);
        editor.commit();
    }


    public String getPu(){
       return settings.getString(DEVICE,"");
    }


    public  void setMac(String macSn) {
        editor.putString(MAC_SN,macSn);
        editor.commit();
    }

    public  String getMac() {
        return settings.getString(MAC_SN,"");
    }

    /**
     * 清空所有数据.
     */
    public void clearAllData() {
        editor.clear();
        editor.commit();
    }


}
