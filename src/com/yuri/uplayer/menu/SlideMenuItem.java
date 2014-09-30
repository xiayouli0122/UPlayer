package com.yuri.uplayer.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.yuri.uplayer.R;

public class SlideMenuItem {
	// 抽屉菜单列表的图标引入
	public static final int[] SLIDE_MENU_DEFAULT_ICON_IDS = {
			R.drawable.person_news_press, R.drawable.menu_icon_effect,
			R.drawable.menu_icon_set, R.drawable.menu_icon_find_music,
			R.drawable.menu_icon_playmode, R.drawable.menu_icon_refresh_lib,
			R.drawable.menu_icon_about, R.drawable.menu_icon_exit };

	private int iconId;
	private String title;

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 抽屉菜单的初始
	public List<SlideMenuItem> init(Context context) {
		List<SlideMenuItem> listItems = new ArrayList<SlideMenuItem>();
		String[] itemTitles = context.getResources().getStringArray(R.array.slide_menu_items);
		SlideMenuItem menuItem = null;
		for (int i = 0; i < SLIDE_MENU_DEFAULT_ICON_IDS.length; i++) {
			menuItem = new SlideMenuItem();
			menuItem.setIconId(SLIDE_MENU_DEFAULT_ICON_IDS[i]);
			menuItem.setTitle(itemTitles[i]);
			listItems.add(menuItem);
		}
		return listItems;
	}

}
