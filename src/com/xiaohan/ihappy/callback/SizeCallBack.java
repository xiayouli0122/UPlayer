package com.xiaohan.ihappy.callback;

public interface SizeCallBack {

	public void onGlobalLayout();

	public void getViewSize(int idx, int width, int height, int[] dims);
}
