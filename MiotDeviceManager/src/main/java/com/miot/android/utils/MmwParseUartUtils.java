package com.miot.android.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class MmwParseUartUtils {
	public static String doLinkBindMake(String mac,String UserBinaryData) {

		String CodeName="";
		String Chn="";
		int len=0;
		String sMlcc="";
		try {
			CodeName = "GetUartData";
			Chn = "0";
			byte[] by=hexString2Bytes(UserBinaryData);
			len = by.length;
			sMlcc = "CodeName=" + CodeName + "&Chn=" + Chn +"&Mac="+mac+"&port="+4888+ "&Len="
					+ len
					+ "&UserBinaryData=";
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[by.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(by, 0, bs, mlcc.length, by.length);
			sMlcc = new String(bs, "ISO-8859-1");
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;

	}

	public static String doLinkBindMake(String UserBinaryData) {

		String CodeName="";
		String Chn="";
		int len=0;
		String sMlcc="";
		try {
			CodeName = "GetUartData";
			Chn = "0";
			byte[] by=hexString2Bytes(UserBinaryData);
			len = by.length;
			sMlcc = "CodeName=" + CodeName + "&Chn=" + Chn + "&Len="
					+ len
					+ "&UserBinaryData=";
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[by.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(by, 0, bs, mlcc.length, by.length);
			sMlcc = new String(bs, "ISO-8859-1");
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;

	}

	public static String getMlccUart(String s){
		String result="";
		if(doLinkBindParse(s).isEmpty()){
			return "";
		}
		result=byteToStr(doLinkBindParse(s));
		return result;

	}

	public static boolean isMlccUartFormat(String s){
		if (s.isEmpty()){
			return false;
		}
		if (s.startsWith("f2f2")||s.startsWith("F2F2")&&s.endsWith("7e")||s.endsWith("7E")){
			return true;
		}
		return false;
	}

	public static String functionCode(String s){
		String result="";

		return result;
	}

	public static String doLinkBindParse(String string) {
		String smlcc = "";
		try {
			if (string.startsWith("CodeName=GetUartData")) {
				if (string.startsWith("UserBinaryData=",
						string.indexOf("UserBinaryData="))) {
					smlcc = string.substring(string.indexOf("UserBinaryData=")
							+ "UserBinaryData=".length(), string.length());
					int length = Integer.parseInt(string.substring(
							string.indexOf("Len=") + "Len=".length(),
							string.indexOf("&UserBinaryData=")));
					if (length > smlcc.length()) {
						return "";
					}
					smlcc = smlcc.substring(0, length);
				} else {
					smlcc = string;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smlcc;
	}

	private static String lwSmlcc = null;

	// TODO
	public static String doLinkBindByteToString(String string) {
		String mlccString = "";
		try {
			// byte [] bs=string.getBytes("ISO-8859-1");
			mlccString = string.substring(string.indexOf("UserBinaryData=")
					+ "UserBinaryData=".length(), string.length());
			lwSmlcc = byteToStr(mlccString);
			return lwSmlcc;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return lwSmlcc;

	}

	public static String byteToStr(String msgIn) {
		String msg = "";
		String byteStr = "";
		try {
			byte[] bytes;
			bytes = msgIn.getBytes("iso-8859-1");
			byteStr = Bytes2HexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byteStr = byteStr.toUpperCase();
		return byteStr;
	}

	// 从字节数组到十六进制字符串转换
	public static String Bytes2HexString(byte[] b) {
		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 二维码校验和
	 *
	 * @param preIds
	 * @return
	 */

	public static Character getLastIDNum(String preIds) {
		Character lastId = null; // 当传入的字符串没有17位的时候，则无法计算，直接返回
		if (preIds == null) { // && preIds.length()<17) {
			return null;
		}
		int[] weightArray = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
				2 };// 权数数组
		String vCode = "10X98765432";// 校验码字符串
		int sumNum = 0;// 前17为乘以权然后求和得到的数
		// 循环乘以权，再求和
		int maxI = preIds.length() > 17 ? 17 : preIds.length();
		for (int i = 0; i < maxI; i++) {
			int index = Integer.parseInt(preIds.charAt(i) + "");
			sumNum = sumNum + index * weightArray[i];// 乘以权数，再求和
		}
		int modNum = sumNum % 11;// 求模
		lastId = vCode.charAt(modNum);// 从验证码中找出对应的数
		if (lastId == 'X')
			return '0';
		else
			return lastId;
	}

	public static String doLinkBindMianBanMake(byte[] bt) {
		String CodeName="";
		String Chn="";
		String sMlcc="";
		CodeName = "GetUartData";
		Chn = "0";
		sMlcc = "CodeName=" + CodeName + "&Chn=" + Chn + "&Len=" + bt.length
				+ "&UserBinaryData=";
		try {
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[bt.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(bt, 0, bs, mlcc.length, bt.length);
			sMlcc = new String(bs, "ISO-8859-1");
			System.out.println(sMlcc);
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;
	}
	public static String bytesToHexString(byte[] src) {
		StringBuilder sb = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
			sb.append(" ");
		}
		return sb.toString();
	}


	public static String hexString2Byte(byte[] bs){
		String string=null;
		try {
			string=new String(bs,"ISO-8859-1");
			return string;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static byte[] hexString2Bytes(String src) {
		src = src.replace(" ", "");
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp;
		try {
			tmp = src.getBytes("ISO-8859-1");
			for (int i = 0; i < src.length() / 2; i++) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return ret;
	}



	public static byte[] stringTo16Byte(String temp) {

		int len = temp.length();
		for (int i = 0; i < 16 - len; i++) {
			if (temp.length() == 16) {
				break;
			}
			temp = temp + "0";
		}
		return temp.getBytes();
	}

	//将传入参数按2位分割字符串返回数组【传入参数:16进制字符串】
	public static String[] splitStrs(String str){
		int m = str.length()/2;
		if (m * 2 < str.length()) {
			m++;
		}
		String[] strs = new String[m];
		int j = 0;
		for (int i = 0; i < str.length(); i++) {if (i % 2 == 0) {
			strs[j] = "" + str.charAt(i);
		} else {
			strs[j] = strs[j] + "" + str.charAt(i);
			j++;
		}
		}
		return strs;
	}

	public static int getNameLen(String name){
		int len=0;
		String regex = "[\u4e00-\u9fff]";

		len = (" " + name + " ").split (regex).length - 1;
		return len;
	}
	/**
	 * 字符串补齐
	 * @param source 源字符串
	 * @param fillLength 补齐长度
	 * @param fillChar 补齐的字符
	 * @param isLeftFill true为左补齐，false为右补齐
	 * @return
	 */
	public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
		if (source == null || source.length() >= fillLength) return source;

		StringBuilder result = new StringBuilder(fillLength);
		int len = fillLength - source.length();
		if (isLeftFill) {
			for (; len > 0; len--) {
				result.append(fillChar);
			}
			result.append(source);
		} else {
			result.append(source);
			for (; len > 0; len--) {
				result.append(fillChar);
			}
		}
		return result.toString();
	}

	public static String stringFill2(String source, int fillLength, char fillChar, boolean isLeftFill) {
		if (source == null || source.length() >= fillLength) return source;

		char[] c = new char[fillLength];
		char[] s = source.toCharArray();
		int len = s.length;
		if(isLeftFill){
			int fl = fillLength - len;
			for(int i = 0; i<fl; i++){
				c[i] = fillChar;
			}
			System.arraycopy(s, 0, c, fl, len);
		}else{
			System.arraycopy(s, 0, c, 0, len);
			for(int i = len; i<fillLength; i++){
				c[i] = fillChar;
			}
		}
		return String.valueOf(c);
	}

}
