
package com.yuri.uplayer.ui.adapters;

import static com.yuri.uplayer.Constants.SIZE_THUMB;
import static com.yuri.uplayer.Constants.SRC_FIRST_AVAILABLE;
import static com.yuri.uplayer.Constants.TYPE_ARTIST;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.yuri.uplayer.R;
import com.yuri.uplayer.cache.ImageInfo;
import com.yuri.uplayer.cache.ImageProvider;
import com.yuri.uplayer.helpers.utils.MusicUtils;
import com.yuri.uplayer.ui.fragments.grid.ArtistsFragment;
import com.yuri.uplayer.views.ViewHolderGrid;
import com.yuri.uplayer.views.ViewHolderList;

import java.lang.ref.WeakReference;

/**
 * @author Andrew Neal
 */
public class ArtistAdapter extends SimpleCursorAdapter {

    private AnimationDrawable mPeakOneAnimation, mPeakTwoAnimation;

//    private WeakReference<ViewHolderGrid> holderReference;
    private WeakReference<ViewHolderList> holderReference;
    
    private Context mContext;
    
    private ImageProvider mImageProvider;

    public ArtistAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    	mContext = context;
    	mImageProvider = ImageProvider.getInstance( (Activity) mContext );
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
        // ViewHolderGrid
//        final ViewHolderGrid viewholder;
        final ViewHolderList viewholder;

        if (view != null) {

//          viewholder = new ViewHolderGrid(view);
      	viewholder = new ViewHolderList(view);
//          holderReference = new WeakReference<ViewHolderGrid>(viewholder);
      	holderReference = new WeakReference<ViewHolderList>(viewholder);
            view.setTag(holderReference.get());

        } else {
//            viewholder = (ViewHolderGrid)convertView.getTag();
        	viewholder = (ViewHolderList)convertView.getTag();
        }

        // Artist Name
        String artistName = mCursor.getString(ArtistsFragment.mArtistNameIndex);
        holderReference.get().mViewHolderLineOne.setText(artistName);

        // Number of albums
        int albums_plural = mCursor.getInt(ArtistsFragment.mArtistNumAlbumsIndex);
        boolean unknown = artistName == null || artistName.equals(MediaStore.UNKNOWN_STRING);
        String numAlbums = MusicUtils.makeAlbumsLabel(mContext, albums_plural, 0, unknown);
        holderReference.get().mViewHolderLineTwo.setText(numAlbums);
        
        holderReference.get().mQuickContext.setOnClickListener(showContextMenu);

        ImageInfo mInfo = new ImageInfo();
        mInfo.type = TYPE_ARTIST;
        mInfo.size = SIZE_THUMB;
        mInfo.source = SRC_FIRST_AVAILABLE;
        mInfo.data = new String[]{ artistName };
        
        mImageProvider.loadImage( viewholder.mViewHolderImage, mInfo );

        // Now playing indicator
        long currentartistid = MusicUtils.getCurrentArtistId();
        long artistid = mCursor.getLong(ArtistsFragment.mArtistIdIndex);
        if (currentartistid == artistid) {
            holderReference.get().mPeakOne.setImageResource(R.anim.peak_meter_1);
            holderReference.get().mPeakTwo.setImageResource(R.anim.peak_meter_2);
            mPeakOneAnimation = (AnimationDrawable)holderReference.get().mPeakOne.getDrawable();
            mPeakTwoAnimation = (AnimationDrawable)holderReference.get().mPeakTwo.getDrawable();
            try {
                if (MusicUtils.mService.isPlaying()) {
                    mPeakOneAnimation.start();
                    mPeakTwoAnimation.start();
                } else {
                    mPeakOneAnimation.stop();
                    mPeakTwoAnimation.stop();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            holderReference.get().mPeakOne.setImageResource(0);
            holderReference.get().mPeakTwo.setImageResource(0);
        }
        return view;
    }
}
