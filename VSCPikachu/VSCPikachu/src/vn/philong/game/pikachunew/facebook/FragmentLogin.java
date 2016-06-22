package vn.philong.game.pikachunew.facebook;

import vn.philong.game.pikachunew.R;
import vn.philong.game.pikachunew.utility.LogUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookException;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class FragmentLogin extends Fragment {
//	private static final String[] PERMISSIONS = { "publish_actions"};
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.layout_ranking_fb, 
	            container, false);
	    init(view);
	    return view;
	}
	private void init(View view){
		LoginButton loginBtn = (LoginButton) view
				.findViewById(R.id.login_button);
		
		loginBtn.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				LogUtils.e(getClass(), "error:" + error.getMessage());
			}
		});
	}
}
