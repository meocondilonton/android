package vn.philong.game.pikachunew;


import com.easyndk.classes.AndroidNDKHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CrossSaleActivity extends Activity {
	private TextView mTitle,mContent;
	private ImageView mIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cross_sale);
		mTitle = (TextView) findViewById(R.id.cross_sale_title);
		mContent = (TextView) findViewById(R.id.cross_sale_content);
		mIcon = (ImageView) findViewById(R.id.cross_sale_icon);
		CrossSaleManager.initUI(mTitle, mContent, mIcon);
	}
	public void finish(){
//		Intent intent = new Intent(this,VSCPikachu.class);
//		startActivity(intent);
		super.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(this,VSCPikachu.class);
//		startActivity(intent);
		super.onDestroy();
		AndroidNDKHelper.SendMessageWithParameters(
				"callExit",
				null);
	}
	public void cancel(View target){
		finish();
	}
	public void submit(View target){
		CrossSaleManager.submit(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!isFinishing()){
			finish();
		}
	}
	
}
