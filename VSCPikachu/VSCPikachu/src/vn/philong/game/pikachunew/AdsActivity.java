package vn.philong.game.pikachunew;

import vn.clevernet.android.sdk.ClevernetView;
import vn.clevernet.android.sdk.ClevernetView.ClevernetViewCallbackListener;
import android.app.Activity;
import android.os.Bundle;

import com.easyndk.classes.AndroidNDKHelper;

public class AdsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clever_net_view);
		initViews();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AndroidNDKHelper.SendMessageWithParameters(
				"callExit",
				null);
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(!isFinishing()){
			finish();
		}
	}
	private void initViews() {
		ClevernetView adView = (ClevernetView) findViewById(R.id.cadad);

		adView.setCleverNetViewCallbackListener(new ClevernetViewCallbackListener() {
			
			@Override
			public void onLoaded(boolean arg0, ClevernetView arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIllegalHttpStatusCode(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public void onApplicationResume() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onApplicationPause() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAdClicked() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}