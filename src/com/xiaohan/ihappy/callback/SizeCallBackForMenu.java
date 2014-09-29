package com.xiaohan.ihappy.callback;

import android.widget.Button;


public class SizeCallBackForMenu implements SizeCallBack {

	private Button menu;
	private int menuWidth;
	
	
	public SizeCallBackForMenu(Button menu){
		super();
		this.menu = menu;
	}
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		this.menuWidth = this.menu.getMeasuredWidth() + 80;
	}

	public void getViewSize(int idx, int width, int height, int[] dims) {
		// TODO Auto-generated method stub
		dims[0] = width;
		dims[1] = height;
		/*��ͼ�����м���ͼ*/
		if(idx != 1){
			dims[0] = width - this.menuWidth;
		}
	}

}
