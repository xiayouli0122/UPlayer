package com.yuri.uplayer.activities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yuri.uplayer.IApolloService;
import com.yuri.uplayer.R;
import com.yuri.uplayer.callback.SizeCallBackForMenu;
import com.yuri.uplayer.helpers.utils.ApolloUtils;
import com.yuri.uplayer.helpers.utils.MenuHorizontalScrollView;
import com.yuri.uplayer.helpers.utils.MusicUtils;
import com.yuri.uplayer.helpers.utils.ThemeUtils;
import com.yuri.uplayer.service.ApolloService;
import com.yuri.uplayer.service.ServiceToken;
import com.yuri.uplayer.ui.adapters.MenuListAdapter;
import com.yuri.uplayer.ui.adapters.PagerAdapter;
import com.yuri.uplayer.ui.adapters.ScrollingTabsAdapter;
import com.yuri.uplayer.ui.fragments.grid.AlbumsFragment;
import com.yuri.uplayer.ui.fragments.grid.ArtistsFragment;
import com.yuri.uplayer.ui.fragments.list.GenresFragment;
import com.yuri.uplayer.ui.fragments.list.PlaylistsFragment;
import com.yuri.uplayer.ui.fragments.list.TracksFragment;
import com.yuri.uplayer.ui.widgets.ScrollableTabView;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import com.xiaohan.ihappy.ui.fragments.list.RecentlyAddedFragment;
//import static com.xiaohan.ihappy.Constants.TABS_ENABLED;
import static com.yuri.uplayer.Constants.MIME_TYPE;
import static com.yuri.uplayer.Constants.PLAYLIST_RECENTLY_ADDED;
import static com.yuri.uplayer.Constants.THEME_ITEM_BACKGROUND;

/**
 * @author 萧丶翰
 * @Note This is the "holder" for all of the tabs
 */
public class MusicLibrary extends Activity implements ServiceConnection {

    private ServiceToken mToken;
    public static MenuHorizontalScrollView scrollView;
	private ListView menuList;
	private View carolPage;
	private Button menuBtn;
	private MenuListAdapter menuListAdapter;
	public static int sleeptime;
	 public static Set<String> defaults,tabs_set;

    @SuppressWarnings("static-access")
	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	
        LayoutInflater inflater = LayoutInflater.from(this);
		
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		this.scrollView = (MenuHorizontalScrollView)findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 0);//FIX ME can change the Pressed position
		this.menuList = (ListView)findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
		TextView mName = (TextView)findViewById(R.id.desktop_name);
		mName.setText(R.string.menu_title);
//		TextView mSig = (TextView)findViewById(R.id.desktop_sig);
//		mSig.setText(R.string.menu_sig);
		
		this.carolPage = inflater.inflate(R.layout.carol_page, null);
		this.menuBtn = (Button)this.carolPage.findViewById(R.id.carol_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
		
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[]{leftView, carolPage};
		this.scrollView.initViews(children, new SizeCallBackForMenu(this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		
		
        // Landscape mode on phone isn't ready
        if (!ApolloUtils.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Scan for music
       // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);       
        
        // Layout
        //setContentView(R.layout.library_browser);

        // Style the actionbar
        //initActionBar();

        // Control Media volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Important!
        initPager();  
  
    }

    private OnClickListener onClickListener = new OnClickListener(){

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			scrollView.clickMenuBtn();
			
			
		}
	};

	public MenuHorizontalScrollView getScrollView() {
		return scrollView;
		
	}



	@SuppressWarnings("static-access")
	public void setScrollView(MenuHorizontalScrollView scrollView) {
		this.scrollView = scrollView;
	}



	
	/*定义和实现设置睡眠时间的Handler*/
	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		//要做的事情
		super.handleMessage(msg);
			android.os.Process.killProcess(android.os.Process.myPid());
		   }
		};
		
	public static class MyThread implements Runnable{
		
		public void run() {
		// TODO Auto-generated method stub
		while (true) {
		try {
		Thread.sleep(1000);//线程暂停时间XX分钟
		Message message=new Message();
		message.what=1;
		handler.sendMessage(message);//发送消息
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		      }
		    }
		  }
		}

	
	//侧滑菜单的隐现状态控制
	@SuppressWarnings("static-access")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		/*侧滑菜单隐藏*/
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(MenuHorizontalScrollView.menuOut == true)
			{
				this.scrollView.clickMenuBtn();
			}
				
			else{
				this.finish();			
			}
			return true;	
			
		}
		/*侧滑菜单出现*/
		if(keyCode == KeyEvent.KEYCODE_MENU){
			if(MenuHorizontalScrollView.menuOut != true)
			{
				this.scrollView.clickMenuBtn();
			   }
			return true;
			}
		
		return super.onKeyDown(keyCode, event);
	}
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder obj) {
        MusicUtils.mService = IApolloService.Stub.asInterface(obj);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        MusicUtils.mService = null;
    }

    @Override
    protected void onStart() {

        // Bind to Service
        mToken = MusicUtils.bindToService(this, this);
        if(TracksFragment.mListView!=null){
        	IntentFilter filter = new IntentFilter();
        	filter.addAction(ApolloService.META_CHANGED);
            filter.addAction(ApolloService.QUEUE_CHANGED);
            filter.addAction(ApolloService.PLAYSTATE_CHANGED);
            ApolloService.IDLE_DELAY=90000;
        }
        
        super.onStart();
    }

    @Override
	public void onStop() {
        // Unbind
        if (MusicUtils.mService != null)
            MusicUtils.unbindFromService(mToken);

        //TODO: clear image cache

        super.onStop();
    }

    /**
     * Initiate ViewPager and PagerAdapter
     */
    @SuppressLint({ "NewApi", "NewApi" })
	public void initPager() {
        // Initiate PagerAdapter
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString(MIME_TYPE, Audio.Playlists.CONTENT_TYPE);
        bundle.putLong(BaseColumns._ID, PLAYLIST_RECENTLY_ADDED);
        
        //Get tab visibility preferences
       // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        tabs_set = new HashSet<String>(Arrays.asList(
        		getResources().getStringArray(R.array.tab_titles)
        	));//此处的defaults已被替换为tabs_set
       // tabs_set = sp.getStringSet(TABS_ENABLED,defaults);
        //if its empty fill reset it to full defaults
        	//stops app from crashing when no tabs are shown
        	//TODO:rewrite activity to not crash when no tabs are chosen to show
//        if(tabs_set.size()==0){
//        	tabs_set = defaults;
//        }
       
        //Only show tabs that were set in preferences
        
        // Tracks
        if(tabs_set.contains(getResources().getString(R.string.tab_songs)))
        	mPagerAdapter.addFragment(new TracksFragment());
       // Albums
        if(tabs_set.contains(getResources().getString(R.string.tab_albums)))
        	mPagerAdapter.addFragment(new AlbumsFragment());
        
        // Recently added tracks
        //if(tabs_set.contains(getResources().getString(R.string.tab_recent)))
        //	mPagerAdapter.addFragment(new RecentlyAddedFragment(bundle));
        // Artists
        if(tabs_set.contains(getResources().getString(R.string.tab_artists)))
        	mPagerAdapter.addFragment(new ArtistsFragment());
        
      
        // // Playlists
        if(tabs_set.contains(getResources().getString(R.string.tab_playlists)))
        	mPagerAdapter.addFragment(new PlaylistsFragment());
        // // Genres
        if(tabs_set.contains(getResources().getString(R.string.tab_genres)))
        	mPagerAdapter.addFragment(new GenresFragment());

        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(mViewPager);

        // Theme chooser
        ThemeUtils.initThemeChooser(this, mViewPager, "viewpager", THEME_ITEM_BACKGROUND);
        ThemeUtils.setMarginDrawable(this, mViewPager, "viewpager_margin");
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView)findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);

        // Theme chooser
        ThemeUtils.initThemeChooser(this, mScrollingTabs, "scrollable_tab_background",
                THEME_ITEM_BACKGROUND);
    }
    
    /**
     * For the theme chooser
     */
//    private void initActionBar() {
//
//    	ActionBar actBar = getActionBar();
//        
//        // The ActionBar Title and UP ids are hidden.
//        int upId = Resources.getSystem().getIdentifier("up", "id", "android");
//        
//        ImageView actionBarUp = (ImageView)findViewById(upId);
//
//        // Theme chooser
//        ThemeUtils.setActionBarBackground(this, actBar, "action_bar_background");
//        ThemeUtils.initThemeChooser(this, actionBarUp, "action_bar_up", THEME_ITEM_BACKGROUND);
//
//    	actBar.setDisplayUseLogoEnabled(true);
//        actBar.setDisplayShowTitleEnabled(false);
//    }
    
    /**
     * Respond to clicks on actionbar options
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//	        case R.id.action_search:
//	            onSearchRequested();
//	            break;
//
//	        case R.id.action_settings:
//	        	startActivityForResult(new Intent(this, SettingsHolder.class),0);
//	            break;
//
//	        case R.id.action_eqalizer:
//	        	Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
//                i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicUtils.getCurrentAudioId());
//                startActivityForResult(i, 0);
//	            break;
//
//	        case R.id.action_shuffle_all:
//	        	shuffleAll();
//	            break;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	Intent i = getBaseContext().getPackageManager()
	    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
    }   
    
    
 
	@Override
	protected void onResume() {
		super.onResume();

    }
    
    
    /**
     * Initiate the Top Actionbar
     */
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.actionbar_top, menu);
//	    return true;
//	}
//
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem search = menu.findItem(R.id.action_search);
//        MenuItem overflow = menu.findItem(R.id.action_overflow);
//        // Theme chooser
//        ThemeUtils.setActionBarItem(this, search, "apollo_search");
//        ThemeUtils.setActionBarItem(this, overflow, "apollo_overflow");
//        
//        return super.onPrepareOptionsMenu(menu);
//    }
    
	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
//	private void exit() {
//		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
//			Toast.makeText(this, R.string.exit_talking, Toast.LENGTH_SHORT).show();
//			mExitTime = System.currentTimeMillis();
//		} else {
//			
//			ApolloService.mManager.cancelAll();
//			this.onStop();
//			this.finish();
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(0);
//		}
//	}

	/**
     * Shuffle all the tracks
     */
    public void shuffleAll() {
        Uri uri = Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
            BaseColumns._ID
        };
        String selection = AudioColumns.IS_MUSIC + "=1";
        String sortOrder = Audio.Media.DEFAULT_SORT_ORDER;
        Cursor cursor = MusicUtils.query(this, uri, projection, selection, null, sortOrder);
        if (cursor != null) {
            MusicUtils.shuffleAll(this, cursor);
            cursor.close();
            cursor = null;
        }
    }    

}
