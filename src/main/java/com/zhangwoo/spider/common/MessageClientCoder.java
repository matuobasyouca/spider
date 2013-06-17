package com.zhangwoo.spider.common;

import java.io.UnsupportedEncodingException;

import com.meiya.common.encrypt.RSABuilder;

public class MessageClientCoder {
	public static String[] keys = new String[] {
			"65537",
			"119095462634741964324355910493568039420885276438910071756048819526760006432501232179657393516109953903723308234590275673898671491223659550041136368691686513371223760955488588079501257335745347521033202920071657686214660648901407766547905679030685564937077732539638036430290036113165791480029536921250935798327"};
	
	public static String decode(String msg){
		try {
			msg = new RSABuilder().Dec_RSA(msg, keys[0], keys[1]);
			if(!msg.substring(0,8).equals("zhangwoo")){
				return "";
			}
		} catch (UnsupportedEncodingException e) {}
		return msg.substring(8);
	}
	
	public static String encode(String msg){
		try {
			msg = new RSABuilder().Enc_RSA("zhangwoo"+msg, keys[0], keys[1]);
		} catch (UnsupportedEncodingException e) {}
		return msg;
	}
}
