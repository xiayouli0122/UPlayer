package com.yuri.uplayer.activities;

import static com.yuri.uplayer.Constants.MIME_TYPE;
import static com.yuri.uplayer.Constants.PLAYLIST_RECENTLY_ADDED;
import static com.yuri.uplayer.Constants.THEME_ITEM_BACKGROUND;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yuri.uplayer.IApolloService;
import com.yuri.uplayer.Log;
import com.yuri.uplayer.R;
import com.yuri.uplayer.helpers.SingleMediaScanner;
import com.yuri.uplayer.helpers.utils.ApolloUtils;
import com.yuri.uplayer.helpers.utils.MusicUtils;
import com.yuri.uplayer.helpers.utils.ThemeUtils;
import com.yuri.uplayer.preferences.SettingsHolder;
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

/**
 * @Note This is the "holder" for all of the tabs
 */
public class MusicLibrary extends Activity implements ServiceConnection,
		OnItemClickListener, OnQueryTextListener {
	private static final String TAG = MusicLibrary.class.getSimpleName();
	
	private ServiceToken mToken;
	private ListView mMenuListView;
	private MenuListAdapter mMenuAdapter;
	
	public static int sleeptime;
	public static Set<String> defaults, tabs_set;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		// Landscape mode on phone isn't ready
		if (!ApolloUtils.isTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Scan for music
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.library_browser);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.about, R.string.about) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Log.d(TAG, "onDrawerClosed");
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Log.d(TAG, "onDrawerOpened");
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		this.mMenuAdapter = new MenuListAdapter(this);
		this.mMenuListView = (ListView) findViewById(R.id.menuList);
		this.mMenuListView.setOnItemClickListener(this);
		this.mMenuListView.setAdapter(mMenuAdapter);

		TextView mName = (TextView) findViewById(R.id.desktop_name);
		mName.setText(R.string.menu_title);

		// Style the actionbar
		initActionBar();

		// Control Media volume
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Important!
		initPager();
	}

	/* 定义和实现设置睡眠时间的Handler */
	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情
			super.handleMessage(msg);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	};

	public static class MyThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(1000);// 线程暂停时间XX分钟
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);// 发送消息
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			} else {
				mDrawerLayout.openDrawer(Gravity.LEFT);
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
		if (TracksFragment.mListView != null) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(ApolloService.META_CHANGED);
			filter.addAction(ApolloService.QUEUE_CHANGED);
			filter.addAction(ApolloService.PLAYSTATE_CHANGED);
			ApolloService.IDLE_DELAY = 90000;
		}

		super.onStart();
	}

	@Override
	public void onStop() {
		// Unbind
		if (MusicUtils.mService != null)
			MusicUtils.unbindFromService(mToken);

		// TODO: clear image cache
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

		// Get tab visibility preferences
		// SharedPreferences sp =
		// PreferenceManager.getDefaultSharedPreferences(this);
		tabs_set = new HashSet<String>(Arrays.asList(getResources()
				.getStringArray(R.array.tab_titles)));// 此处的defaults已被替换为tabs_set
		// tabs_set = sp.getStringSet(TABS_ENABLED,defaults);
		// if its empty fill reset it to full defaults
		// stops app from crashing when no tabs are shown
		// TODO:rewrite activity to not crash when no tabs are chosen to show
		// if(tabs_set.size()==0){
		// tabs_set = defaults;
		// }

		// Only show tabs that were set in preferences

		// Tracks
		if (tabs_set.contains(getResources().getString(R.string.tab_songs)))
			mPagerAdapter.addFragment(new TracksFragment());
		// Albums
		if (tabs_set.contains(getResources().getString(R.string.tab_albums)))
			mPagerAdapter.addFragment(new AlbumsFragment());

		// Recently added tracks
		// if(tabs_set.contains(getResources().getString(R.string.tab_recent)))
		// mPagerAdapter.addFragment(new RecentlyAddedFragment(bundle));
		// Artists
		if (tabs_set.contains(getResources().getString(R.string.tab_artists)))
			mPagerAdapter.addFragment(new ArtistsFragment());

		// // Playlists
		if (tabs_set.contains(getResources().getString(R.string.tab_playlists)))
			mPagerAdapter.addFragment(new PlaylistsFragment());
		// // Genres
		if (tabs_set.contains(getResources().getString(R.string.tab_genres)))
			mPagerAdapter.addFragment(new GenresFragment());

		// Initiate ViewPager
		ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setPageMargin(getResources().getInteger(
				R.integer.viewpager_margin_width));
		mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
		mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);

		// Tabs
		initScrollableTabs(mViewPager);

		// Theme chooser
		ThemeUtils.initThemeChooser(this, mViewPager, "viewpager",
				THEME_ITEM_BACKGROUND);
		ThemeUtils.setMarginDrawable(this, mViewPager, "viewpager_margin");
	}

	/**
	 * Initiate the tabs
	 */
	public void initScrollableTabs(ViewPager mViewPager) {
		ScrollableTabView mScrollingTabs = (ScrollableTabView) findViewById(R.id.scrollingTabs);
		ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(
				this);
		mScrollingTabs.setAdapter(mScrollingTabsAdapter);
		mScrollingTabs.setViewPager(mViewPager);

		// Theme chooser
		ThemeUtils.initThemeChooser(this, mScrollingTabs,
				"scrollable_tab_background", THEME_ITEM_BACKGROUND);
	}

	/**
	 * For the theme chooser
	 */
	private void initActionBar() {

		ActionBar actBar = getActionBar();

		// The ActionBar Title and UP ids are hidden.
		// int upId = Resources.getSystem().getIdentifier("up", "id",
		// "android");
		//
		// ImageView actionBarUp = (ImageView)findViewById(upId);
		//
		// // Theme chooser
		// ThemeUtils.setActionBarBackground(this, actBar,
		// "action_bar_background");
		// ThemeUtils.initThemeChooser(this, actionBarUp, "action_bar_up",
		// THEME_ITEM_BACKGROUND);

		// actBar.setDisplayUseLogoEnabled(true);
		// actBar.setDisplayShowTitleEnabled(false);
		actBar.setDisplayHomeAsUpEnabled(true);
		actBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		mSearchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		mSearchView.setOnQueryTextListener(this);
		return true;
	}
	
	
	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
		 boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
		 menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
		 return super.onPrepareOptionsMenu(menu);
	 }
	
	/**
	 * Respond to clicks on actionbar options
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// switch (item.getItemId()) {
		// case R.id.action_search:
		// onSearchRequested();
		// break;
		//
		// case R.id.action_settings:
		// startActivityForResult(new Intent(this, SettingsHolder.class),0);
		// break;
		//
		// case R.id.action_eqalizer:
		// Intent i = new
		// Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
		// i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,
		// MusicUtils.getCurrentAudioId());
		// startActivityForResult(i, 0);
		// break;
		//
		// case R.id.action_shuffle_all:
		// shuffleAll();
		// break;
		//
		// default:
		// return super.onOptionsItemSelected(item);
		// }
		return true;
	}

	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
	// private void exit() {
	// if (System.currentTimeMillis() - mExitTime > INTERVAL) {
	// Toast.makeText(this, R.string.exit_talking, Toast.LENGTH_SHORT).show();
	// mExitTime = System.currentTimeMillis();
	// } else {
	//
	// ApolloService.mManager.cancelAll();
	// this.onStop();
	// this.finish();
	// android.os.Process.killProcess(android.os.Process.myPid());
	// System.exit(0);
	// }
	// }

	/**
	 * Shuffle all the tracks
	 */
	public void shuffleAll() {
		Uri uri = Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection = new String[] { BaseColumns._ID };
		String selection = AudioColumns.IS_MUSIC + "=1";
		String sortOrder = Audio.Media.DEFAULT_SORT_ORDER;
		Cursor cursor = MusicUtils.query(this, uri, projection, selection,
				null, sortOrder);
		if (cursor != null) {
			MusicUtils.shuffleAll(this, cursor);
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		switch (position) {
		case 1:
			//音乐均衡器
			// Intent i = new
			// Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
			// i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,
			// MusicUtils.getCurrentAudioId());
			// context.startActivityForResult(i, 0);
			break;
		// Other setting
		case 2:
			intent.setClass(this, SettingsHolder.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_in, R.anim.push_out);
			break;
		// local search
		case 3:
//			intent.setClass(this, SearchActivity.class);
//			startActivity(intent);
//			break;
//		case 4:
			new Thread(new Runnable() {
				@Override
				public void run() {
					shuffleAll();
				}
			}).start();
			
			break;
		case 4:
			//ScanSDCard();
			File file = Environment.getExternalStorageDirectory();
			new SingleMediaScanner(getApplicationContext(), file);
			break;
		case 5:
			AboutDialog();
			break;
		case 6:
			//exit
			ApolloService.valuesave.savaGuidePosition(getApplicationContext(), false);
			ApolloService.valuesave.savaPlayState(getApplicationContext(), false);
			try {
				MusicUtils.mService.stop();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finish();
			ApolloService.IDLE_DELAY = 100;
			break;
		}
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
	
	private void AboutDialog() {
		LayoutInflater inflater = getLayoutInflater();
		final View about_view = inflater.inflate(R.layout.about_dialog, null);
		new AlertDialog.Builder(this)
				.setIcon(R.drawable.app_icon)
				.setTitle(R.string.about)
				.setView(about_view)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(getApplicationContext(),
										R.string.thanks_word, Toast.LENGTH_SHORT)
										.show();
							}
						}).show();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		MusicUtils.findSearch(MusicLibrary.this, query);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

}
