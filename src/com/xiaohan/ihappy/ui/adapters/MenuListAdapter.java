package com.xiaohan.ihappy.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.xiaohan.ihappy.R;

import com.xiaohan.ihappy.activities.MusicLibrary;
import com.xiaohan.ihappy.activities.SearchActivity;
//import com.xiaohan.ihappy.activities.MusicLibrary.MyThread;
import com.xiaohan.ihappy.helpers.utils.MusicUtils;
import com.xiaohan.ihappy.helpers.utils.ScanSdReceiver;
import com.xiaohan.ihappy.preferences.SettingsHolder;
import com.xiaohan.ihappy.service.ApolloService;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**����˵���������**/
public class MenuListAdapter extends BaseAdapter {
	
	private Activity context;
	private List<Map<String, Object>> listItems; 
	private int itemCount;
	private LayoutInflater listInflater;
	private boolean isPressed[];
	//*****************************************************/
	//����˵��б��ͼ������
	private int[] menu_image_array = { 
			 /* R.drawable.head,*/
			  R.drawable.person_news_press,
			  R.drawable.menu_icon_effect,R.drawable.menu_icon_set,
			  R.drawable.menu_icon_find_music,R.drawable.menu_icon_playmode,
			  R.drawable.menu_icon_refresh_lib,R.drawable.menu_icon_about,R.drawable.menu_icon_exit};

   //***************************************/
	private final int COUNT =8; 
	private int pressedId;

	public final class ListItemsView{
		public ImageView menuIcon;
		public TextView menuText;
	}
	
	
	public MenuListAdapter(Activity context, int pressedId) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.pressedId = pressedId;
		this.init();
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return this.itemCount;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public ListItemsView listItemsView;
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int po = position;
		
		if(convertView == null){
			listItemsView = new ListItemsView();
			convertView = this.listInflater.inflate(R.layout.menu_list_item, null);
			listItemsView.menuIcon = (ImageView)convertView.findViewById(R.id.menuIcon);
			listItemsView.menuText = (TextView)convertView.findViewById(R.id.menuText);
			convertView.setTag(listItemsView);
		}
		else{
			listItemsView = (ListItemsView)convertView.getTag();
		}
		
		listItemsView.menuIcon.setBackgroundResource((Integer)listItems.get(position).get("menuIcon"));
		listItemsView.menuText.setText((String)listItems.get(position).get("menuText"));
		//��ĳ������Ĳ˵���Ŀ���������Ŀ��ʾ��ɫ�Ҷ�Ӧ��ͼ����ʾ����
		if(this.isPressed[position] == true){
			convertView.setBackgroundResource(R.drawable.menu_item_bg_sel);
		   // listItemsView.menuIcon.setImageResource(mIconPressed[position]);
		}
		//��ľ������ʾ��ʼ״̬	
		else{
			convertView.setBackgroundColor(Color.TRANSPARENT);
			//listItemsView.menuIcon.setImageResource(menu_image_array[position]);
		}
		
		
		convertView.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				changeState(po);
				gotoActivity(po);
				notifyDataSetInvalidated();
				new Handler().post(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		return convertView;
	} 
	
	
	public void shuffleAll() {
        Uri uri = Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
            BaseColumns._ID
        };
        String selection = AudioColumns.IS_MUSIC + "=1";
        String sortOrder = Audio.Media.DEFAULT_SORT_ORDER;
        Cursor cursor = MusicUtils.query(context, uri, projection, selection, null, sortOrder);
        if (cursor != null) {
            MusicUtils.shuffleAll(context, cursor);
            cursor.close();
            cursor = null;
        }
    }    
	/**
	 * ɨ��SD��
	 */
	private void ScanSDCard() {
		IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		ScanSdReceiver receiver = new ScanSdReceiver();
		context.registerReceiver(receiver, intentfilter);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		Uri.parse("file://"+ Environment.getExternalStorageDirectory().getAbsolutePath())));
		
	}
	
//	private void setSleppTime() {
//		String[] menustring = new String[] { "5���Ӻ���ͣ���ֲ��Ų��˳�Ӧ�ã�","15���Ӻ���ͣ���ֲ��Ų��˳�Ӧ�ã�", "30���Ӻ���ͣ���ֲ��Ų��˳�Ӧ�ã�", 
//				                             "60���Ӻ���ͣ���ֲ��Ų��˳�Ӧ�ã�" , "90���Ӻ���ͣ���ֲ��Ų��˳�Ӧ�ã�"};
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
	
	private  void AboutDialog(){
		LayoutInflater in = context.getLayoutInflater();
		final View about_view = in.inflate(R.layout.about_dialog, null);
		new AlertDialog.Builder(context).setIcon(R.drawable.app_icon).setTitle(R.string.about)
		.setView(about_view).setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {	
		
			public void onClick(DialogInterface dialog,	int which) {	

    	Toast.makeText(context,	R.string.thanks_word, Toast.LENGTH_LONG).show();} 
	
		}).show(); }

	                            
	//���ĳ������˵���Ŀ��ת����Ӧ��activity
	private void gotoActivity(int position){
		Intent intent = new Intent();
		switch(position){
		 //˯�߶�ʱ
		//case 0:
			
			//setSleppTime();
			//SetTimeDialog.getCreatePlaylistDialog(context).create().show();
	
			//break;
		/*----------------------------------------------------*/
		 //���־�����
		case 1:
			
//			Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
//            i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicUtils.getCurrentAudioId());
//            context.startActivityForResult(i, 0);
			break;
		/*----------------------------------------------------*/	
		//����������
		case 2:
			if(this.pressedId == 3){
				intent.setClass(context, SettingsHolder.class);
				context.startActivity(intent);
				
			}
			else{
				intent.setClass(context, SettingsHolder.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,R.anim.push_out);
				//context.finish();
			}
			break;
		/*----------------------------------------------------*/
			//��������
			case 3:
				intent.setClass(context, SearchActivity.class);
				context.startActivity(intent);
				MusicLibrary.scrollView.clickMenuBtn();
				break;
			/*----------------------------------------------------*/
		//ȫ���������
		case 4:
			((MusicLibrary) context).shuffleAll();
			break;
		/*----------------------------------------------------*/
       //ˢ���ֿ�
		case 5:
			
        	ScanSDCard();
 	
			break;	
			/*----------------------------------------------------*/
			 //����iHappy���ֲ�����
		   case 6:
              AboutDialog();
			break;	
			/*----------------------------------------------------*/
		   //�˳�Ӧ��
			case 7:
				ApolloService.valuesave.savaGuidePosition(context, false);
				ApolloService.valuesave.savaPlayState(context,false);
				try {
					MusicUtils.mService.stop();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    context.finish();
				 ApolloService.IDLE_DELAY=100;
				 
			   break;	
		}
	}
	
	//�ı䴥���Ĳ˵���Ŀλ�ã���ͣ��״̬��
	private void changeState(int position){
		
		for(int i = 0; i < this.itemCount; i++){
			isPressed[i] = false;
		}
		isPressed[position] = true;
	}
	
	
	//����˵��ĳ�ʼ
	private void init(){
		
		this.itemCount = this.COUNT;
		this.listItems =  new ArrayList<Map<String, Object>>();
		this.isPressed = new boolean[this.itemCount];
		for(int i = 0; i < this.itemCount; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuIcon", menu_image_array[i]);
			if(i == 0){
				map.put("menuText", "      �������У���������! ");
			} 
			else if(i == 1){
				map.put("menuText", "  ���־�����");
			}
			else if(i == 2){
				map.put("menuText","  ��������");
			}
			else if(i == 3){
				map.put("menuText", "  ��������");
			}
			else if(i == 4){
				map.put("menuText","  �����һ��");
			}
			else if(i == 5){
				map.put("menuText", "  ˢ���ֿ�");
			}
			else if(i == 6){
				map.put("menuText", "  ����iHappyMusic");
			}
			else if(i == 7){
				map.put("menuText", "  ��    ��");
			}
			
			this.listItems.add(map);
			this.isPressed[i] = false;
		}
		this.isPressed[this.pressedId] = true;
		this.listInflater = LayoutInflater.from(context); 
	}
}
