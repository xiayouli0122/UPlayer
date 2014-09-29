package com.xiaohan.ihappy.baidu.music;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

public class BaiduLrc {

	// public static String BaiDupath =
	// "http://box.zhangmen.baidu.com/x?op=12&count=1&title=��Լ�ڶ���$$����$$$$ ";
	public static String BaiDupath = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=";
	// public static String
	// BaiDulrcpath="http://box.zhangmen.baidu.com/bdlrc/496/49684.lrc";
	public static String BaiDulrcpath = "http://box.zhangmen.baidu.com/bdlrc/";

	public static String getMusic(String musicname, String singername) {
		Log.i("lrc", "--" + BaiDupath + EncodeName(musicname) + "$$" + EncodeName(singername) + "$$$$");
		return BaiDupath + EncodeName(musicname) + "$$" + EncodeName(singername) + "$$$$";
	}

	public static String getLrcPath(String lrcid) {
		int id = Integer.parseInt(lrcid);
		Log.i("lrc", lrcid + "---/100");
		return BaiDulrcpath + id / 100 + "/" + id + ".lrc";
	}

	/**
	 * �������߸������� �м��пո��ʱ�� ����Ҫ����ת��
	 * @param name
	 * @return
	 */
	public static String EncodeName(String name) {
	   String [] names=name.split(" ");
		if (names.length > 1) {// ˵���������м��пո�
			try {
				return URLEncoder.encode(name.trim(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return name;
			}
		} else {
			return name;
		}
	}

}
