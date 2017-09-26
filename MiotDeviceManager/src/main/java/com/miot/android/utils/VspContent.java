package com.miot.android.utils;

import java.io.UnsupportedEncodingException;


public class VspContent {

	/**
	 * @param content
	 * @return
	 */
	 public static byte[] buildVspContent(String content) {
	        byte[] bs = null;
			try {
			bs = content.getBytes("ISO-8859-1");
	        int headLen = 20;
	        byte[] vc = new byte[headLen+bs.length];
	        for(int i=0;i<vc.length; i++) 
	        	vc[i]= 0;
	            vc[0] = 0x30;
	            vc[1] = 0x68;
	            vc[2] = (byte)(vc.length/256 & 0xff);
	            vc[3] = (byte)(vc.length%256 & 0xff);
	            vc[8] = 0x65;
	            vc[8+2] = (byte)((vc.length-8)/256 & 0xff);
	            vc[8+3] = (byte)((vc.length-8)%256 & 0xff);
	            vc[8+4+3] = (byte)1;
	            System.arraycopy(bs, 0, vc, headLen, bs.length);
	        return decodeMlccMsg(vc);
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
			return bs;
	    }

	public static byte[] formatLsscCmdBuffer(byte[] content) {
		byte[] bs = null;
		try {
			int packageLen = content.length + 20;
			int contentlen = content.length + 12;
			bs = new byte[packageLen];
			bs[0] = (byte) 0x30;
			bs[1] = (byte) 104;
			bs[2] = (byte) (packageLen >> 8 & 0xff);// (packLen/256); // //
			bs[3] = (byte) (packageLen >> 0 & 0xff);// (packLen%256); // //
			bs[4] = (byte) (0 >> 24 & 0xff);
			bs[5] = (byte) (0 >> 16 & 0xff);
			bs[6] = (byte) (0 >> 8 & 0xff);
			bs[7] = (byte) (0 >> 0 & 0xff);
			bs[8] = (byte) 0x65;
			bs[9] = (byte) 0;
			bs[10] = (byte) (contentlen / 256);
			bs[11] = (byte) (contentlen % 256);
			bs[12] = (byte) (1 >> 24 & 0xff);
			bs[13] = (byte) (1 >> 16 & 0xff);
			bs[14] = (byte) (1 >> 8 & 0xff);
			bs[15] = (byte) (1 >> 0 & 0xff);
			bs[16] = (byte) (0 >> 24 & 0xff);
			bs[17] = (byte) (0 >> 16 & 0xff);
			bs[18] = (byte) (0 >> 8 & 0xff);
			bs[19] = (byte) (0 >> 0 & 0xff);
			System.arraycopy(content, 0, bs, 20, content.length);
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[]{};
		}
		return encrypt(bs);
	}


	 
	 public static byte[] decodeMlccMsg(byte[] bs){
	            for(int i=8;i<bs.length;i++)
	                bs[i] = (byte) (bs[i] ^ 0x30);
	            return bs;
	    }
	 
	 public static String getMlccContent(byte[] bs, int len) {
			try {
				if (bs == null || len < 20) {
					return null;
				}
				return new String(bs, 20, len - 20, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}
	 
	 public static byte[] encrypt(byte[] src) {
			for (int i = 8; i < src.length; i++) {
				src[i] ^= src[0];
			}
			return src;
		}
}
