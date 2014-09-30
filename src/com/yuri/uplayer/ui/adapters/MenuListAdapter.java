package com.yuri.uplayer.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.uplayer.R;
import com.yuri.uplayer.menu.SlideMenuItem;


/**抽屉菜单的适配器**/
public class MenuListAdapter extends BaseAdapter {
	
	private List<SlideMenuItem> listItems; 
	private LayoutInflater mInflater = null;

	public final class ViewHolder{
		public ImageView menuIcon;
		public TextView menuText;
	}
	
	public MenuListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		listItems = new SlideMenuItem().init(context);
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int position) {
		return listItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = this.mInflater.inflate(R.layout.menu_list_item, null);
			holder.menuIcon = (ImageView)convertView.findViewById(R.id.menuIcon);
			holder.menuText = (TextView)convertView.findViewById(R.id.menuText);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.menuIcon.setBackgroundResource(listItems.get(position).getIconId());
		holder.menuText.setText(listItems.get(position).getTitle());
		
		return convertView;
	} 
	
//	private void setSleppTime() {
//		String[] menustring = new String[] { "5分钟后，暂停音乐播放并退出应用！","15分钟后，暂停音乐播放并退出应用！", "30分钟后，暂停音乐播放并退出应用！", 
//    "60分钟后，暂停音乐播放并退出应用！" , "90分钟后，暂停音乐播放并退出应用！"};
//		ListView menulist = new ListView(context);
//		menulist.setCacheColorHint(Color.TRANSPARENT);
//		menulist.setDividerHeight(1);
//		menulist.setAdapter(new ArrayAdapter<String>(context,R.layout.dialog_menu_item, R.id.text1, menustring));
//		menulist.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT));
//
//		final AlertDialog xfdialog = new AlertDialog.Builder(context).setTitle(R.string.set_time_tittle).setView(menulist).create();
//		xfdialog.show();
//		menulist.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				xfdialog.cancel();
//				xfdialog.dismiss();
//				if (position==0) { 
//					MusicLibrary.sleeptime=0;
//				}
//				else if (position==1) {
//					MusicLibrary.sleeptime=15;
//				}else if (position==2) {
//					MusicLibrary.sleeptime=30;
//				}
//				else if (position==3) {
//					MusicLibrary.sleeptime=60;
//				}
//				else if (position==4) {
//					MusicLibrary.sleeptime=90;
//				}
//				new Thread(new MyThread()).start();
//			}
//		});
//	}
	
	
	
}
