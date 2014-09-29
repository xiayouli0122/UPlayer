package com.yuri.uplayer.activities;



import com.yuri.uplayer.R;
import com.yuri.uplayer.helpers.utils.MusicUtils;




import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
/**搜索歌曲的活动单元组件**/
public class SearchActivity extends Activity {
	private AutoCompleteTextView actv;
	
	private Button search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  	// 设置全屏显示
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search);
		search = (Button) findViewById(R.id.search);
		actv = (AutoCompleteTextView) findViewById(R.id.actv);
		search.setOnClickListener(new OnClickListener() {

			public  boolean isselect;
			public Cursor content;
			public String[] _titles;
			public String[] _artists;
			public int[] _ids;
			public void onClick(View v) {
				String select = actv.getText().toString();
		
				isselect = getIntent().getBooleanExtra("isselect", false);
				if (isselect) {
					ContentResolver cr = SearchActivity.this.getContentResolver();
					content = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
							null, " title like ?", new String[] { "%" + select + "%" },
							null);

					if (content != null) {
						_ids = new int[content.getCount()];
						_artists = new String[content.getCount()];
						_titles = new String[content.getCount()];
						int i = 0;
						while (content.moveToNext()) {
							_ids[i] = content.getInt(content
									.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
							_artists[i] = content
									.getString(content
											.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
							_titles[i] = content
									.getString(content
											.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
							i++;
 
						 }
					  }
					}
				MusicUtils.findSearch(SearchActivity.this,actv.getText().toString());
				SearchActivity.this.finish();

			}
		});
	}

}
