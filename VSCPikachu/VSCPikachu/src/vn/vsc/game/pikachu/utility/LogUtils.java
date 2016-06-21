package vn.vsc.game.pikachu.utility;

import vn.vsc.game.pikachu.BuildConfig;
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
