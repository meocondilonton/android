package vn.philong.game.pikachunew.dlg;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

public abstract class DialogLayout extends Dialog {
	protected Activity context;
	public DialogLayout(Activity context, int resID, int btCloseRes) {
		super(context);
		this.context = context;
		if (btCloseRes == 0) {
			setCancelable(true);
		} else
			setCancelable(false);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(resID);
		getWindow().setLayout(
				context.getResources().getDisplayMetrics().widthPixels * 4 / 5,
				context.getResources().getDisplayMetrics().heightPixels * 4/5);
		if(btCloseRes != 0){
			View v = findViewById(btCloseRes);
			if(v != null){
				v.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
			}
		}
		init();
	}

	protected abstract void init();
}
