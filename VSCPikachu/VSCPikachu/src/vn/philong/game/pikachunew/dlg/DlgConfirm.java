package vn.philong.game.pikachunew.dlg;

import vn.philong.game.pikachunew.R;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DlgConfirm extends DialogLayout implements
		android.view.View.OnClickListener {
	private Button btOK, btCancel;
	private TextView tvText;
	public DlgConfirm(Activity context) {
		super(context, R.layout.layout_dlg_confirm, R.id.confirm_cancel);
		getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		btOK = (Button) findViewById(R.id.confirm_ok);
		btCancel = (Button) findViewById(R.id.confirm_cancel);
		tvText = (TextView) findViewById(R.id.confirm_text);
		btOK.setOnClickListener(this);
	}

	public void show(String text, String btOKText, String btCancelText,
			Runnable onOK, boolean isCancel,int resCharacter) {
		try {
			tvText.setText(text);
			if (!TextUtils.isEmpty(btOKText))
				btOK.setText(btOKText);
			if (onOK != null)
				btOK.setTag(onOK);
			if (!isCancel) {
				btCancel.setVisibility(View.GONE);
			} else {
				btCancel.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(btCancelText))
				btCancel.setText(btCancelText);
			if (!isShowing()) {
				show();
			}
		} catch (Throwable e) {

		}
	}
	public void showInfo(CharSequence text){
		btOK.setVisibility(View.GONE);
		btCancel.setVisibility(View.GONE);
		tvText.setText(text);
		setCancelable(true);
		show();
	}
	public void show(String text, Runnable onOK, boolean isCancel) {
		try {
			tvText.setText(text);
			if (onOK != null)
				btOK.setTag(onOK);
			if (!isCancel) {
				btCancel.setVisibility(View.GONE);
			} else {
				btCancel.setVisibility(View.VISIBLE);
			}
			if (!isShowing()) {
				show();
			}
		} catch (Throwable e) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			Runnable run = (Runnable) btOK.getTag();
			run.run();
			btOK.setTag(null);
		} catch (Exception e) {

		} finally {
			dismiss();
		}
	}
}
