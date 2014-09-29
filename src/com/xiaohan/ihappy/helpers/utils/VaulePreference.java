package com.xiaohan.ihappy.helpers.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class VaulePreference {

	SharedPreferences sharedPreferences;

	public VaulePreference(Context context) {
		sharedPreferences = context.getSharedPreferences("value_preference",
				Context.MODE_PRIVATE);
	}

	
	/**
	 * �����ʴ�С
	 * 
	 * @param context
	 * @param lrc_size
	 *            ��ʴ�С
	 */
	public void savaLrcSize(Context context, int lrc_size) {
		sharedPreferences.edit().putInt("lrc_size", lrc_size).commit();
	}

	/**
	 * ��ȡ��ʴ�С
	 * 
	 * @param context
	 * @return int lrc_size ��ʴ�С
	 */
	public int getLrcSize(Context context) {
		return sharedPreferences.getInt("lrc_size", 22);
	}

	/**
	 * ��������ɫ
	 * 
	 * @param context
	 * @param lrc_color�����ɫ
	 */
	public void savaLrcColor(Context context, int lrc_color) {
		sharedPreferences.edit().putInt("lrc_color", lrc_color).commit();
	}

	/**
	 * ��ȡ�����ɫС
	 * 
	 * @param context
	 * @return int lrc_color �����ɫ
	 */
	public int getLrcColor(Context context) {
		return sharedPreferences.getInt("lrc_color", Color.rgb(51, 181, 229));
	}
	
	/**
	 * ����Ӧ���˳�ʱ�����������״̬����
	 * 
	 * @param context
	 * @param pos
	 */
	public void savaGuidePosition(Context context, boolean pos) {
		sharedPreferences.edit().putBoolean("isStart", pos).commit();
	}
	public boolean getGuidePosition(Context context) {
		return sharedPreferences.getBoolean("isStart",true);
	}
	
	
	public void savaPlayState(Context context, boolean yes) {
		sharedPreferences.edit().putBoolean("is_play", yes).commit();
	}
	public boolean getPlayState(Context context) {
		return sharedPreferences.getBoolean("is_play",true);
	}
}
