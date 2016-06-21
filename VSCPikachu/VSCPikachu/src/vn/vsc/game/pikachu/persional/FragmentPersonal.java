package vn.vsc.game.pikachu.persional;

import vn.vsc.game.pikachu.KaApp;
import vn.vsc.game.pikachu.R;
import vn.vsc.game.pikachu.utility.LogUtils;
import vn.vsc.game.pikachu.utility.ResourceManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.LoginButton;
import com.redmanit.lib.imagemanager.ImageManager;

public class FragmentPersonal extends Fragment implements View.OnClickListener {
	private View layout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		layout = inflater.inflate(R.layout.layout_persional, container, false);
		View btClose = layout.findViewById(R.id.persional_close);
		btClose.setOnClickListener(this);
		inits();
		return layout;
	}
	public void inits(){
		ImageView imgAvatar = (ImageView) layout.findViewById(R.id.persional_avatar);
		TextView tvUserName = (TextView) layout.findViewById(R.id.persional_username);
		final TextView tvXu = (TextView) layout.findViewById(R.id.persional_xu);
		TextView tvLevel = (TextView) layout.findViewById(R.id.persional_level);
		TextView tvPoint = (TextView) layout.findViewById(R.id.persional_point);
		String userName = ResourceManager.loadAsString(ResourceManager.FACEBOOK_NAME);
		if(!TextUtils.isEmpty(userName)){
			String address = "http://graph.facebook.com/"
					+ ResourceManager.loadAsString(ResourceManager.FACEBOOK_ACCOUNT) + "/picture?width=120&height=120";
			ImageManager.getInstance().push(address, imgAvatar);
			tvUserName.setText(userName);
		} else {
			tvUserName.setText("Guest");
		}
		ResourceManager.loadXu();
		KaApp.getHandler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tvXu.setText(ResourceManager.getXu()+"");
			}
		}, 100);
		
		LoginButton loginBtn = (LoginButton) layout.findViewById(R.id.login_button);
		Session session = Session.getActiveSession();
		if(session != null){
			LogUtils.e(getClass(), "active session not null:"+session.isOpened());
			if(session.isOpened()){
				loginBtn.setVisibility(View.GONE);
			} else {
				loginBtn.setVisibility(View.VISIBLE);
			}
		}else {
			LogUtils.e(getClass(), "active session equals null");
			loginBtn.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		getActivity().finish();
	}
}
