package vn.philong.game.pikachunew.utility;

import vn.philong.game.pikachunew.BuildConfig;
import android.util.Log;

public class LogUtils {

	public static void exception(Exception e) {
		if (BuildConfig.DEBUG)
			e.printStackTrace();
	}

	public static void e(Class target, String s) {
		if (BuildConfig.DEBUG)
			Log.e(target.getSimpleName(), s);
	}
}
