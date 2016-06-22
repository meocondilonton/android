package vn.philong.game.pikachunew.dlg;
//package vn.vsc.ailatrieuphu.dlg;
//
//import com.facebook.Session;
//import com.facebook.android.Facebook;
//import com.facebook.widget.LoginButton;
//import com.redmanit.lib.imagemanager.ImageManager;
//
//import vn.vsc.ailatrieuphu.KaApp;
//import vn.vsc.altp.R;
//import vn.vsc.ailatrieuphu.utility.ResourceManager;
//import vn.vsc.ailatrieuphu.utility.Util;
//import android.app.Activity;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class DialogProfile extends DialogLayout {
//
//	public DialogProfile(Activity context) {
//		super(context, R.layout.layout_persional, R.id.persional_close);
//	}
//
//	@Override
//	protected void init() {
//		ImageView imgAvatar = (ImageView) findViewById(R.id.persional_avatar);
//		TextView tvUserName = (TextView) findViewById(R.id.persional_username);
//		TextView tvXu = (TextView) findViewById(R.id.persional_xu);
//		TextView tvLevel = (TextView) findViewById(R.id.persional_level);
//		TextView tvPoint = (TextView) findViewById(R.id.persional_point);
//		String userName = ResourceManager.loadAsString(ResourceManager.FACEBOOK_NAME);
//		if(!TextUtils.isEmpty(userName)){
//			String address = "http://graph.facebook.com/"
//					+ ResourceManager.loadAsString(ResourceManager.FACEBOOK_ACCOUNT) + "/picture?width=120&height=120";
//			ImageManager.getInstance().push(address, imgAvatar);
//			tvUserName.setText(userName);
//		} else {
//			tvUserName.setText("Guest");
//		}
//		tvXu.setText(ResourceManager.loadXu()+"");
//		
//		tvPoint.setText(context.getString(R.string.info_point,
//				Util.formatMoney(KaApp.getDBLevel().getAllPoint()) + ""));
//		tvLevel.setText(context.getString(R.string.info_level, KaApp.getDBLevel()
//				.getMaxLevel() + ""));
//		LoginButton loginBtn = (LoginButton) findViewById(R.id.login_button);
//		if(Session.getActiveSession() != null){
//			loginBtn.setVisibility(View.GONE);
//		}else {
//			loginBtn.setVisibility(View.VISIBLE);
//		}
//	}
//	public void show(){
//		ImageView imgAvatar = (ImageView) findViewById(R.id.persional_avatar);
//		String address = "http://graph.facebook.com/"
//				+ ResourceManager.loadAsString(ResourceManager.FACEBOOK_ACCOUNT,"me") + "/picture?width=120&height=120";
//		ImageManager.getInstance().push(address, imgAvatar);
//		TextView tvXu = (TextView) findViewById(R.id.persional_xu);
//		tvXu.setText(ResourceManager.loadXu()+"");
//		
//		LoginButton loginBtn = (LoginButton) findViewById(R.id.login_button);
//		if(Session.getActiveSession() != null){
//			loginBtn.setVisibility(View.GONE);
//		}else {
//			
//		}
//		super.show();
//	}
//}
