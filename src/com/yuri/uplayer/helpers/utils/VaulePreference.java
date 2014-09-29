package com.yuri.uplayer.helpers.utils;


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
	 * 保存歌词大小
	 * 
	 * @param context
	 * @param lrc_size
	 *            歌词大小
	 */
	public void savaLrcSize(Context context, int lrc_size) {
		sharedPreferences.edit().putInt("lrc_size", lrc_size).commit();
	}

	/**
	 * 获取歌词大小
	 * 
	 * @param context
	 * @return int lrc_size 歌词大小
	 */
	public int getLrcSize(Context context) {
		return sharedPreferences.getInt("lrc_size", 22);
	}

	/**
	 * 保存歌词颜色
	 * 
	 * @param context
	 * @param lrc_color歌词颜色
	 */
	public void savaLrcColor(Context context, int lrc_color) {
		sharedPreferences.edit().putInt("lrc_color", lrc_color).commit();
	}

	/**
	 * 获取歌词颜色小
	 * 
	 * @param context
	 * @return int lrc_color 歌词颜色
	 */
	public int getLrcColor(Context context) {
		return sharedPreferences.getInt("lrc_color", Color.rgb(51, 181, 229));
	}
	
	/**
	 * 保存应用退出时的引导界面的状态控制
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
