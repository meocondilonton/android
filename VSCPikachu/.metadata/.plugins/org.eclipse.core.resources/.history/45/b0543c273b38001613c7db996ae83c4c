package vn.vsc.game.pikachu.utility;

import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;
import org.json.JSONStringer;

import vn.vsc.game.pikachu.KaApp;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.easyndk.classes.AndroidNDKHelper;

public class ResourceManager {
	private static SharedPreferences sharedPref = PreferenceManager
			.getDefaultSharedPreferences(KaApp.getAppContext());
	private static final String URL_SAVE_IMAGE = "img_";
	private static final String URL_SMS = "sms_";
	public static final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";
	public static final String FACEBOOK_AVATAR = "FACEBOOK_AVATAR";
	public static final String FACEBOOK_NAME = "FACEBOOK_NAME";
	public static final String FACEBOOK_ACCOUNT = "FACEBOOK_ACCOUNT";
	public static final String REG_ID = "REGISTER_ID";
	public static final String PHONE_CSKH = "PHONE_CSKH";

	public static final String CROSS_SALE = "CROSS_SALE";
	public static final String ALTP_XU = "ALTP_XU";
	public static final String SHARE_FB_LEVEL = "FB_LEVEL_";
	public static final String DATE_UPDATE_DB = "DATE_FB";
	public static final String PHONE_NUMBER = "PHONE_NUMBER";
	public static int loadAsInt(String key) {
		int value = sharedPref.getInt(key, -1);
		return value;
	}

	public static int loadAsInt(String key, int defaultValue) {
		int value = sharedPref.getInt(key, defaultValue);
		return value;
	}

	public static void saveInt(String key, int data) {
		sharedPref.edit().putInt(key, data).commit();
	}

	public static boolean loadAsBoolean(String key) {
		boolean is = sharedPref.getBoolean(key, false);
		// Log.e("load key = " + key, "value = " + is);
		return is;
	}

	public static void saveString(String key, String data) {
		sharedPref.edit().putString(key, data).commit();
	}

	public static void saveBoolean(String key, boolean data) {
		// Log.e("save key = " + key, "value = " + data);
		sharedPref.edit().putBoolean(key, data).commit();
	}

	public static String loadAsString(String key) {
		return sharedPref.getString(key, "");
	}

	public static String loadAsString(String key, String defaultValue) {
		return sharedPref.getString(key, defaultValue);
	}

	public static Bitmap loadImage(String name) {
		try {
			InputStream stream = KaApp.getAppContext().openFileInput(
					URL_SAVE_IMAGE + name);
			return BitmapFactory.decodeStream(stream);
		} catch (Exception e) {
			return null;
		}
	}

	public static void saveImage(String name, Bitmap bmp) {
		try {
			OutputStream stream = KaApp.getAppContext().openFileOutput(
					URL_SAVE_IMAGE + name, Context.MODE_PRIVATE);
			bmp.compress(CompressFormat.PNG, 100, stream);
			stream.flush();
		} catch (Exception e) {
		}
	}

	public static boolean isCrossSale(String packageName) {
		return !loadAsBoolean(CROSS_SALE + packageName);
	}

	public static void activeCrossSale(String pkgName, int addXu) {
		saveBoolean(CROSS_SALE + pkgName, true);
		addXu(addXu);
	}

	public static void addXu(int addXu) {
		// int xu = loadAsInt(ALTP_XU,0);
		// xu += addXu;
		// saveInt(ALTP_XU, xu);
		try {
			JSONStringer jStringer = new JSONStringer();
			jStringer.object().key("xu").value(addXu + "").endObject();
			AndroidNDKHelper.SendMessageWithParameters("addXu", new JSONObject(
					jStringer.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int gameXu = 0;

	public static int getXu() {
		return gameXu;
	}

	public static void updateGameXu(int gameXu) {
		ResourceManager.gameXu = gameXu;
	}

	public static void loadXu() {
		// return loadAsInt(ALTP_XU, 0);
		try {
			JSONStringer jStringer = new JSONStringer();
			jStringer.object().key("xu").value("0").endObject();
			AndroidNDKHelper.SendMessageWithParameters("addXu", new JSONObject(
					jStringer.toString()));
		} catch (Exception e) {

		}
	}

	public static boolean isSavedDate(String dateString) {
		return loadAsString(DATE_UPDATE_DB).equals(dateString);
	}

	public static void saveDate(String dateString) {
		saveString(DATE_UPDATE_DB, dateString);
	}

	private static int level, time;

	public static void requestLevel() {
		try {
			JSONStringer jStringer = new JSONStringer();
			AndroidNDKHelper.SendMessageWithParameters("getLevel", new JSONObject(
					jStringer.toString()));
		} catch (Exception e) {

		}
	}
	public static void setLevelAndTime(int level,int time){
		ResourceManager.level = level;
		ResourceManager.time = time;
	}
	public static int getLevel() {
		return level;
	}

	public static int getTime() {
		return time;
	}
}
