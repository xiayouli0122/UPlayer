package com.yuri.uplayer.helpers.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.yuri.uplayer.R;

/**歌曲扫描类**/
/*This work comes from Dreamer丶Team. The main programmer is LinShaoHan.
 * QQ:752280466   Welcome to join with us.
 */
public class ScanSdReceiver extends BroadcastReceiver {

	private int count1;
	private int count2;
	private int count;
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)){
			Cursor c1 = context.getContentResolver()
			.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME },
					null, null, null);
			count1 = c1.getCount();
			System.out.println("count:"+count);

			
		}else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			Cursor c2 = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Audio.Media.TITLE,
							MediaStore.Audio.Media.DURATION,
							MediaStore.Audio.Media.ARTIST,
							MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.DISPLAY_NAME }, null, null,
					null);
			count2 = c2.getCount();
			count = count2 - count1;
			
			if (count > 0) {
				Toast.makeText(context, "已刷新乐库，共增添" + count +"首歌曲！",
						Toast.LENGTH_SHORT).show();
			} if (count ==0){
				Toast.makeText(context, R.string.had_add,Toast.LENGTH_SHORT).show();
			}
		}	
	}
}
