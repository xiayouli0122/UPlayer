package com.xiaohan.ihappy.timeset;



import com.xiaohan.ihappy.R;
import com.xiaohan.ihappy.activities.MusicLibrary;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.AutoCompleteTextView;

import android.widget.Toast;
import com.xiaohan.ihappy.activities.MusicLibrary.MyThread;;

/**˯�߶�ʱ��**/
/*This work comes from DreamerؼTeam. The main programmer is LinShaoHan.
 * QQ:752280466   Welcome to join with us.
 */

public class SetTimeDialog extends DialogBuilder {

	public final static int RUNNING_BG = 0;
	public final static int EXIT_APP = 1;
	public final static int DIALOG_CANCEL = 2;
    public static AutoCompleteTextView edittext1; 
    public static int first=0;
	public static Builder getCreatePlaylistDialog(final Context context) {

		AlertDialog.Builder builder = getInstance(context);
		edittext1 = new AutoCompleteTextView(context);
		edittext1.setHint(R.string.set_time);
		edittext1.setSelectAllOnFocus(true);
		builder.setView(edittext1);
		builder.setPositiveButton(context.getString(R.string.Yes),
				new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				 first++;
				/*���ı��������String���͵ķ�����ת��Ϊint���Ͳ���ֵ��time*/
				if(SetTimeDialog.edittext1.getText().toString()!=null)
				{
				MusicLibrary.sleeptime= Integer.valueOf(SetTimeDialog.edittext1.getText().toString()).intValue();
				dialog.cancel();
				}
				else {
				Toast.makeText(context, R.string.sorry_tell, Toast.LENGTH_LONG).show();
				}
				new Thread(new MyThread()).start();
				if(MusicLibrary.sleeptime>=120){
					Toast.makeText(context, R.string.should_time, Toast.LENGTH_LONG)
							.show();
				}
				
				if(first>=2) {
				
					Toast.makeText(context, R.string.cannot_time, Toast.LENGTH_LONG).show();}
				else {
					Toast.makeText(context, "���óɹ���"+edittext1.getText().toString()
						+"���Ӻ��˳�Ӧ�ã�", Toast.LENGTH_LONG)
						.show();}
			    }
			
		});			 
				
		builder.setNeutralButton(context.getString(R.string.cancel),
				null);
		builder.setIcon(ImageScale.getImage(context));
		builder.setTitle(R.string.set_time_tittle);
		return builder;
		  
	}
			
}
