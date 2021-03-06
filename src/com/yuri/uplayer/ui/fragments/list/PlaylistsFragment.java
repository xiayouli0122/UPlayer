/**
 * 
 */

package com.yuri.uplayer.ui.fragments.list;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.PlaylistsColumns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yuri.uplayer.Log;
import com.yuri.uplayer.R;
import com.yuri.uplayer.activities.TracksBrowser;
import com.yuri.uplayer.helpers.utils.MusicUtils;
import com.yuri.uplayer.ui.adapters.PlaylistAdapter;

import static com.yuri.uplayer.Constants.INTENT_KEY_RENAME;
import static com.yuri.uplayer.Constants.INTENT_RENAME_PLAYLIST;
import static com.yuri.uplayer.Constants.MIME_TYPE;
import static com.yuri.uplayer.Constants.PLAYLIST_NAME;

/**
 * @author Andrew Neal
 */
public class PlaylistsFragment extends Fragment implements LoaderCallbacks<Cursor>,
        OnItemClickListener {
	private static final String TAG = PlaylistsFragment.class.getSimpleName();
	
    private PlaylistAdapter mPlaylistAdapter;
    private ListView mListView;

    private Cursor mCursor;

    private String mCurrentPlaylistId;

    // Options
    private static final int PLAY_SELECTION = 11;
    private static final int DELETE_PLAYLIST = 12;
    private static final int RENAME_PLAYLIST = 13;

    // Aduio columns
    public static int mPlaylistNameIndex, mPlaylistIdIndex;

    // Bundle
    public PlaylistsFragment() {
    }

    public PlaylistsFragment(Bundle args) {
        setArguments(args);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Adapter
        mPlaylistAdapter = new PlaylistAdapter(getActivity(), R.layout.listview_items, null,
                new String[] {}, new int[] {}, 0);
        mListView.setOnCreateContextMenuListener(this);
        mListView.setAdapter(mPlaylistAdapter);
        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.listview, container, false);
        mListView = (ListView)root.findViewById(android.R.id.list);
        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	StringBuilder where = new StringBuilder();
		where.append(MediaStore.Audio.Playlists.NAME + " != ''");
		
		String selection = where.toString();
		
        String[] projection = new String[] {
                BaseColumns._ID, PlaylistsColumns.NAME
        };
        Uri uri = Audio.Playlists.EXTERNAL_CONTENT_URI;
        String sortOrder = Audio.Playlists.DEFAULT_SORT_ORDER;
        return new CursorLoader(getActivity(), uri, projection, selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Check for database errors
        if (data == null) {
            return;
        }
        Log.d(TAG, "onLoadFinished.count:" + data.getCount());
        
        mPlaylistIdIndex = data.getColumnIndexOrThrow(BaseColumns._ID);
        mPlaylistNameIndex = data.getColumnIndexOrThrow(PlaylistsColumns.NAME);
        mPlaylistAdapter.changeCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mPlaylistAdapter != null)
            mPlaylistAdapter.changeCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(getArguments() != null ? getArguments() : new Bundle());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        AdapterContextMenuInfo mi = (AdapterContextMenuInfo)menuInfo;

        menu.add(0, PLAY_SELECTION, 0, getResources().getString(R.string.play_all));

        if (mi.id >= 0) {
            menu.add(0, RENAME_PLAYLIST, 0, getResources().getString(R.string.rename_playlist));
            menu.add(0, DELETE_PLAYLIST, 0, getResources().getString(R.string.delete_playlist));
        }

        mCurrentPlaylistId = mCursor.getString(mCursor.getColumnIndexOrThrow(BaseColumns._ID));

        String title = mCursor.getString(mPlaylistNameIndex);
        menu.setHeaderTitle(title);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo mi = (AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case PLAY_SELECTION: {
                long[] list = MusicUtils.getSongListForPlaylist(getActivity(),
                        Long.parseLong(mCurrentPlaylistId));
                MusicUtils.playAll(getActivity(), list, 0);
                break;
            }
            case RENAME_PLAYLIST: {
                Intent intent = new Intent(INTENT_RENAME_PLAYLIST);
                intent.putExtra(INTENT_KEY_RENAME, mi.id);
                getActivity().startActivity(intent);
                break;
            }
            case DELETE_PLAYLIST: {
                Uri uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, mi.id);
                getActivity().getContentResolver().delete(uri, null, null);
                break;
            }
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        tracksBrowser(id);
    }

    /**
     * @param id
     */
    private void tracksBrowser(long id) {

        String playlistName = mCursor.getString(mPlaylistNameIndex);

        Bundle bundle = new Bundle();
        bundle.putString(MIME_TYPE, Audio.Playlists.CONTENT_TYPE);
        bundle.putString(PLAYLIST_NAME, playlistName);
        bundle.putLong(BaseColumns._ID, id);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getActivity(), TracksBrowser.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}
