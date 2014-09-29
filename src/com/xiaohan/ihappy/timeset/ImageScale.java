package com.xiaohan.ihappy.timeset;




import com.xiaohan.ihappy.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

/**ͼƬ��Դ����**/
/*This work comes from DreamerؼTeam. The main programmer is LinShaoHan.
 * QQ:752280466   Welcome to join with us.
 */


public class ImageScale {

	@SuppressWarnings("deprecation")
	public static BitmapDrawable getImage(Context context) {
		// ������Ҫ������ͼƬ��������eoeAndroid��logoͼƬ
		Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon_menu_sleepmode);

		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// ����Ԥת���ɵ�ͼƬ�Ŀ�Ⱥ͸߶�
		int newWidth = 48;
		int newHeight = 48;

		// ���������ʣ��³ߴ��ԭʼ�ߴ�
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();

		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);

		// ��תͼƬ ����
		// matrix.postRotate(45);

		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
				height, matrix, true);

		// �����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton��
		return new BitmapDrawable(resizedBitmap);

	}

}
