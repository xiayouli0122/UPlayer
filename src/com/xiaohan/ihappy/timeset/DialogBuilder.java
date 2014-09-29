package com.xiaohan.ihappy.timeset;






import com.xiaohan.ihappy.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.ContextThemeWrapper;

/**���õ���ʽ�Ի������ʽ�����ɼ̳еĶԻ�������**/
/*This work comes from DreamerؼTeam. The main programmer is LinShaoHan.
 * QQ:752280466   Welcome to join with us.
 */

public class DialogBuilder {
	private static Builder instance;
	private static ContextThemeWrapper localContextThemeWrapper;

	public static Builder getInstance(Context context) {
		localContextThemeWrapper = new ContextThemeWrapper(context,
				R.style.Theme_Dialog);
		instance = new AlertDialog.Builder(localContextThemeWrapper);
		return instance;
	}
}
