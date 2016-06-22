package vn.philong.game.pikachunew;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.redmanit.lib.imagemanager.CacheParams;
import com.redmanit.lib.imagemanager.ImageManager;

public class KaApp extends Application {
	private static Context mAppContext;
	private static Handler mHandler;

	public static Context getAppContext() {
		return mAppContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mAppContext = getApplicationContext();
		mHandler = new Handler();
		 Log.d("long", "KaApp");
		CacheParams cacheParams = new CacheParams(getApplicationContext(),
				"images");
		final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;
		cacheParams.memCacheSize = cacheSize;
		cacheParams.diskCacheSize = 1024 * 1024 * 50;
		cacheParams.clearDiskCacheOnStart = false;
		cacheParams.clearDiskCacheOnStartIfExceedMaxSize = false;
		cacheParams.diskCacheEnabled = true;
		ImageManager im = ImageManager.getInstance(this, cacheParams, 5);
		im.setFIFS(true);
		
	}


	public static Handler getHandler() {
		return mHandler;
	}


	public static void showToast(final int resID) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(KaApp.mAppContext, resID, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	public static void showToast(final String resID) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(KaApp.mAppContext, resID, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
