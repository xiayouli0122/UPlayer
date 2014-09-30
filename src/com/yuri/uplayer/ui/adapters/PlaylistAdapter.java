/**
 * 
 */

package com.yuri.uplayer.ui.adapters;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.yuri.uplayer.Log;
import com.yuri.uplayer.R;
import com.yuri.uplayer.ui.fragments.list.PlaylistsFragment;
import com.yuri.uplayer.views.ViewHolderList;

/**
 * @author Andrew Neal
 */
public class PlaylistAdapter extends SimpleCursorAdapter {
	private static final String TAG = PlaylistAdapter.class.getSimpleName();
    private WeakReference<ViewHolderList> holderReference;
    
    private Context mContext;

    public PlaylistAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
    }

    /**
     * Used to quickly our the ContextMenu
     */
    private final View.OnClickListener showContextMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }
    };

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        Cursor mCursor = (Cursor) getItem(position);
        // ViewHolderList
        final ViewHolderList viewholder;

        if (view != null) {
            viewholder = new ViewHolderList(view);
            holderReference = new WeakReference<ViewHolderList>(viewholder);
            view.setTag(holderReference.get());

        } else {
            viewholder = (ViewHolderList)convertView.getTag();
        }

        String playlist_name = mCursor.getString(PlaylistsFragment.mPlaylistNameIndex);
        Log.d(TAG, "getView.playlist_name:" + playlist_name);
		holderReference.get().mViewHolderLineOne.setText(playlist_name);

		// Helps center the text in the Playlist tab
		int left = mContext.getResources().getDimensionPixelSize(
				R.dimen.listview_items_padding_left_top);
		holderReference.get().mViewHolderLineOne.setPadding(left, 40, 0, 0);
		holderReference.get().mViewHolderImage.setVisibility(View.GONE);
		holderReference.get().mQuickContext.setOnClickListener(showContextMenu);
        return view;
    }

}
