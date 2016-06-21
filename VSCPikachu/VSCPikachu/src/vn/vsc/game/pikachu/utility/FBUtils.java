package vn.vsc.game.pikachu.utility;

import java.io.ByteArrayOutputStream;

import vn.vsc.game.pikachu.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

public class FBUtils {
	public static boolean isLoginFB(){
		return ResourceManager.loadAsString(ResourceManager.FACEBOOK_TOKEN).length() > 0;
	}
	public static void shareFB(Activity a,String message,Bitmap bmp,final Runnable callback){
		final Bundle params = new Bundle();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		params.putString("message", message);
		params.putByteArray("picture", byteArray);
		bmp.recycle();
		Session session = Session.getActiveSession();
		if(session == null){
			Session.openActiveSession(a, true, new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					Request request = new Request(session, "me/photos", params, HttpMethod.POST);
					request.setCallback(new Request.Callback() {
					    @Override
					    public void onCompleted(Response response) {
					        if (response.getError() == null) {
					            // Tell the user success!
					        	callback.run();
					        }
					    }
					});
					request.executeAsync();
				}
			});
		} else {
			Request request = new Request(Session.getActiveSession(), "me/photos", params, HttpMethod.POST);
			request.setCallback(new Request.Callback() {
			    @Override
			    public void onCompleted(Response response) {
			        if (response.getError() == null) {
			            // Tell the user success!
			        	callback.run();
			        }
			    }
			});
			request.executeAsync();
		}
	}
	public static void shareFB(Activity a,String message,final Runnable callback){
		final Bundle params = new Bundle();
		params.putString("caption", "VSC");
		params.putString("message", message);
		params.putString("link", a.getString(R.string.fb_url));
		params.putString("picture", a.getString(R.string.fb_link_img));
		params.putString("title", a.getString(R.string.app_name));
		
		Session session = Session.getActiveSession();
		if(session == null){
			Session.openActiveSession(a, true, new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					Request request = new Request(session, "me/feed", params, HttpMethod.POST);
					request.setCallback(new Request.Callback() {
					    @Override
					    public void onCompleted(Response response) {
					        if (response.getError() == null) {
					            // Tell the user success!
					        	callback.run();
					        }
					    }
					});
					request.executeAsync();
				}
			});
		} else {
			Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST);
			request.setCallback(new Request.Callback() {
			    @Override
			    public void onCompleted(Response response) {
			        if (response.getError() == null) {
			            // Tell the user success!
			        	callback.run();
			        }
			    }
			});
			request.executeAsync();
		}
	}
}
