package com.xiaohan.ihappy.ui.fragments;

import java.io.File;
import java.io.IOException;
import android.app.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tarena.fashionmusic.lrc.Lyric;
import com.tarena.fashionmusic.lrc.LyricView;
import com.tarena.fashionmusic.lrc.PlayListItems;
import com.xiaohan.ihappy.R;
import com.xiaohan.ihappy.helpers.utils.MusicUtils;
import com.xiaohan.ihappy.lrc.utils.HttpTool;
import com.xiaohan.ihappy.lrc.utils.SavelrcTool;


import com.xiaohan.ihappy.baidu.music.BaiduLrc;
import com.xiaohan.ihappy.baidu.music.BaiduMusic;
import com.xiaohan.ihappy.lrc.utils.LRCXmlParser;
import com.xiaohan.ihappy.service.ApolloService;






public class LrcShowFragment extends Fragment{


	    public static final String TTpath = "mnt/sdcard/TMusic/";
	    View lrc_view= null;
        TextView tv_nolrc;
    	public LyricView lyricView;
    	public Lyric mLyric;
    	public boolean ishavelrc = false;
    	public static Intent intent;
    	public static Context Lrccontext;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	lrc_view= inflater.inflate(R.layout.layout_lrc, container, false);
	    	lyricView = (LyricView)lrc_view.findViewById(R.id.audio_lrc);
	    	tv_nolrc = (TextView)lrc_view.findViewById(R.id.tv_nolrc);
	    	
	     	intent = new Intent(ApolloService.UPDATE_LRC_ACTION);
	     	
	        return lrc_view;
	    }

	    /**
	     * Update everything as the meta or playstate changes
	     */
		private final BroadcastReceiver LrcmStatusListener = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	if (intent.getAction().equals(ApolloService.META_CHANGED))
	        	{
	        		ShowLyric(TTpath + MusicUtils.getTrackName() + "-"
							+ MusicUtils.getArtistName()
							+ ".lrc");	
	        	}
	          
	        	Lrccontext=context;
	             
	        }
	    };
	    Thread myThread;
	    @Override
	    public void onStart() {
	        super.onStart();
	        IntentFilter f = new IntentFilter();
	        f.addAction(ApolloService.PLAYSTATE_CHANGED);
	        f.addAction(ApolloService.META_CHANGED);
	        getActivity().registerReceiver(LrcmStatusListener, new IntentFilter(f));
	    }
	    
	 // ------------------------------歌词显示-------------------------------------------------
		/**
		 * 显示歌词信息
		 * 
		 * @param music
		 * @param musicname
		 * @param singername
		 */
		String netlrcpath = null;

		private void ShowLyric(String path) {
			//"mnt/sdcard/external_sd/MP3/"+MusicUtils.getTrackName()+"."+"mp3",
			if (new File(path).exists()) {

				doshowlrc(MusicUtils.songPathName, path);
				ishavelrc = true;

			} else {
				netlrcpath = BaiduLrc.getMusic(MusicUtils.getTrackName(),
						MusicUtils.getArtistName());
				tv_nolrc.setVisibility(View.VISIBLE);
				lyricView.setVisibility(View.GONE);
				new Thread() {
					@Override
					public void run() {
						try {
							new LRCXmlParser(nameshandler)
									.parse(HttpTool.getStream(netlrcpath, null,
											null, HttpTool.GET));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}.start();
			}
		}

		// 显示歌词
		public void doshowlrc(String musicpath, String lrcpath) {
		
			
			tv_nolrc.setVisibility(View.GONE);
			lyricView.setVisibility(View.VISIBLE);
			mLyric = new Lyric(new File(lrcpath), new PlayListItems("", musicpath,
					0l, true));
			lyricView.setmLyric(mLyric);
			lyricView.setSentencelist(mLyric.list);
			// 设置 歌词颜色
			lyricView.setNotCurrentPaintColor(Color.WHITE);
			// 设置当前显示的歌词 颜色
			lyricView.setCurrentPaintColor(Color.BLACK);
//			lyricView.setLrcTextSize(musicPreference.getLrcSize(context));
			lyricView.setTexttypeface(Typeface.SERIF);
			lyricView.setTextHeight(40);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == 5) {
				ShowLyric(TTpath + MusicUtils.getTrackName() + "-"
						+ MusicUtils.getArtistName()
						+ ".lrc");
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		// -------------------lrc--------------------------------------
		public static boolean isupdatalrc = true;
		class UIUpdateThread implements Runnable {
			long time = 1000;

			public void run() {
				while (isupdatalrc) {
					try {
						if (MusicUtils.mService.isPlaying()
								&& ishavelrc == true&&MusicUtils.songPathName!=null) {
							lyricView.updateIndex(MusicUtils.getDuration());
							mHandler.post(mUpdateResults);
                  
						}
						Thread.sleep(time);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// ---------------------lrc-----------------------------------------
		Handler mHandler = new Handler();
		Runnable mUpdateResults = new Runnable() {
			public void run() {
				lyricView.invalidate();
			}
		};

		Handler nameshandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					final BaiduMusic b = (BaiduMusic) msg.obj;
					System.out.println("解析完毕---------" + b.getLrcid());
					if (b.getLrcid() != null) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									SavelrcTool.save(HttpTool.getStream(
											BaiduLrc.getLrcPath(b.getLrcid()),
											null, null, HttpTool.GET), TTpath
											+ MusicUtils.getTrackName() + "-"
											+ MusicUtils.getArtistName()
											+ ".lrc", nameshandler);
		
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
					System.out.println(MusicUtils.getTrackName() + "-"+ MusicUtils.getArtistName());
					break;

				case 10:
					//Log.i("music", "下载保存完毕-开始更新--------");
					ShowLyric(TTpath + MusicUtils.getTrackName()+ "-"
							+ MusicUtils.getArtistName()+ ".lrc");
					break;
				
				}

			}
		};
		
		boolean isrunable = true;
		int curms;

		
		
		
	    @Override
	    public void onDestroy() {
	    	super.onDestroy();  
	    	isupdatalrc = false;
	    	 getActivity().unregisterReceiver(LrcmStatusListener);
	        }

	  
		@Override
		public void onStop() {
			isupdatalrc = false;
			super.onStop();
		}

		@Override
		public void onResume() {
			super.onResume();
			
			isupdatalrc = true;
			new Thread(new UIUpdateThread()).start();
			

			
		}
	
	}


