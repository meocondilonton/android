package vn.vsc.game.pikachu.dlg;
//package vn.vsc.ailatrieuphu.dlg;
//
//import java.util.Arrays;
//import java.util.List;
//
//import vn.vsc.altp.R;
//import vn.vsc.ailatrieuphu.utility.LogUtils;
//import android.app.Activity;
//import android.view.View;
//
//import com.facebook.FacebookException;
//import com.facebook.Session;
//import com.facebook.Session.OpenRequest;
//import com.facebook.Session.StatusCallback;
//import com.facebook.SessionLoginBehavior;
//import com.facebook.SessionState;
//import com.facebook.widget.LoginButton;
//import com.facebook.widget.LoginButton.OnErrorListener;
//
//public class DialogLoginFB extends DialogLayout {
//	private static final String[] PERMISSIONS = { "publish_actions",
//			"user_games_activity", "friends_games_activity" };
//
//	public DialogLoginFB(Activity context) {
//		super(context, R.layout.layout_login_fb, 0);
//	}
//
//	@Override
//	protected void init() {
//		LoginButton loginBtn = (LoginButton) findViewById(R.id.fb_login);
//		loginBtn.setReadPermissions(PERMISSIONS);
//		loginBtn.setOnErrorListener(new OnErrorListener() {
//
//			@Override
//			public void onError(FacebookException error) {
//				// TODO Auto-generated method stub
//				LogUtils.e(getClass(), "error:" + error.getMessage());
//			}
//		});
//
//		loginBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Session session = Session.getActiveSession();
//				LogUtils.e(getClass(), "sessions:" + session);
//				if (session != null && session.isOpened()) {
//					LogUtils.e(getClass(),
//							"acess token:" + session.getAccessToken());
//					openRanking();
//				} else {
//
//					OpenRequest op = new Session.OpenRequest(context);
//					op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
//					op.setCallback(null);
//					op.setPermissions(PERMISSIONS);
//					session = new Session.Builder(context).build();
//					Session.setActiveSession(session);
//					session.openForPublish(op);
//					StatusCallback callback = new StatusCallback() {
//
//						@Override
//						public void call(Session session, SessionState state,
//								Exception exception) {
//							// TODO Auto-generated method stub
//							Session s = Session.getActiveSession();
//
//							if (session.isOpened()) {
//								LogUtils.e(getClass(),
//										"acess token:" + s.getAccessToken());
//								openRanking();
//							} else {
//								if (exception != null)
//									LogUtils.e(getClass(),
//											exception.getMessage());
//								dismiss();
//							}
//						}
//					};
//					Session.openActiveSession(context, true, callback);
//				}
//			}
//		});
//	}
//
//	private Runnable callbackFB;
//
//	public void setCallback(Runnable callback) {
//		this.callbackFB = callback;
//	}
//
//	private static boolean checkPermissions(List<String> needed,
//			List<String> available) {
//		boolean ret = true;
//		for (String s : needed) {
//			if (!available.contains(s)) {
//				ret = false;
//				break;
//			}
//		}
//		return ret;
//	}
//
//	private void openRanking() {
//		dismiss();
//		if (callbackFB != null)
//			callbackFB.run();
//	}
//
//}
