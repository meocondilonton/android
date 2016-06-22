package vn.vsc.game.pikachu.utility;

import vn.vsc.game.pikachu.KaApp;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

public class DeviceInfo {
	private String deviceName;
	private String os, osVersion;
	private String hardware;
	private String manufacture;
	private String is3G, isWifi;
	private Connections connections;
	private String googleAccount;
	private String deviceID;
	private String IMEI;
	private static DeviceInfo instance;
	private String versionName, versionCode;

	public static final int CHANNEL_VSC = 0;
	public static final int CHANNEL_DIENMAY = 1;
	public static final int CHANNEL_MOBIISTAR = 3;
	public static final int CHANNEL_GIONEE = 7;
	public static final int CHANNEL_GOOGLE_PLAY = 6;
	public static final int CHANNEL_EWAY = 8;
	public static final int CHANNEL_9GAME = 9;
	public static final int CHANNEL_APPOTA = 10;
	public static final int CHANNEL_COFFEE_CARE= 12;
	public static final int CHANNEL_BETHONGMINH = 13;
	private static final int CHANNEL = CHANNEL_GOOGLE_PLAY;

	public static final int REAL_CHANEL = loadChanel();
	public String getChanelName() {
		switch(REAL_CHANEL){
		case CHANNEL_VSC:
			return "VSC";
		case CHANNEL_DIENMAY:
			return "DIENMAY";
		case CHANNEL_MOBIISTAR:
			return "MBS";
		case CHANNEL_GIONEE:
			return "GIONEE";
		case CHANNEL_GOOGLE_PLAY:
			return "GGP";
		case CHANNEL_EWAY:
			return "EWAY";
		case CHANNEL_9GAME:
			return "9GAME";
		case CHANNEL_APPOTA:
			return "APPOTA";
		case CHANNEL_BETHONGMINH:
			return "BETHONGMINH";
		}
		return "VSC";
		
	}
	private static int loadChanel() {
//		try {
//			Context myContext = KaApp.getAppContext().createPackageContext(
//					"vn.thanhbai", Context.CONTEXT_IGNORE_SECURITY);
//			SharedPreferences preferences = myContext.getSharedPreferences(
//					"9Play_data", Context.MODE_WORLD_READABLE);
//			int i = preferences.getInt("partner", CHANNEL);
//			LogUtils.e(DeviceInfo.class, "parner:" + i);
//			return i * (i != CHANNEL ? 10 : 1);
//		} catch (Exception e) {
//		}
		return CHANNEL;
	}

	public static DeviceInfo getInstance() {
		if (instance == null)
			instance = new DeviceInfo();
		return instance;
	}

	public DeviceInfo() {
		deviceName = android.os.Build.MODEL;
		manufacture = android.os.Build.MANUFACTURER;
		os = android.os.Build.VERSION.RELEASE;
		osVersion = android.os.Build.VERSION.SDK;
		StatFs statFs = new StatFs(Environment.getRootDirectory()
				.getAbsolutePath());
		long Total = ((long) statFs.getBlockCount() * (long) statFs
				.getBlockSize()) / 1048576;
		hardware = Total + "";
		PackageManager pm = KaApp.getAppContext().getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(KaApp.getAppContext()
					.getPackageName(), 0);
			versionCode = pi.versionCode + "";
			versionName = pi.versionName;
		} catch (Exception e) {
			versionCode = "";
			versionName = "";
		}
		AccountManager manager = (AccountManager) KaApp.getAppContext()
				.getSystemService(Context.ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();
		if (list != null && list.length > 0) {
			googleAccount = list[0].name;
		} else
			googleAccount = "";

		connections = new Connections();
		try {
			ConnectivityManager conMan = (ConnectivityManager) KaApp
					.getAppContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			// mobile
			NetworkInfo mobile = conMan.getNetworkInfo(0);
			if (mobile == null) {
				is3G = "0";
			} else {
				is3G = "1";
			}
			// wifi
			NetworkInfo wifi = conMan.getNetworkInfo(1);
			if (wifi == null) {
				isWifi = "0";
			} else {
				isWifi = "1";
			}
		} catch (Exception e) {
			isWifi = "0";
			is3G = "0";
		}
		connections.s3G = is3G;
		connections.sWifi = isWifi;

		deviceID = getDeviceId();
		IMEI = getPhoneIMEI();
		
		
	}

	public static String getPhoneIMEI() {
		final TelephonyManager tm = (TelephonyManager) KaApp.getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return "" + tm.getDeviceId();

	}

	public static String getDeviceId() {

		String deviceID, androidID, simSerial;

		/** Get IMEI Device */
		final TelephonyManager tm = (TelephonyManager) KaApp.getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = "" + (tm.getDeviceId() == null ? "" : tm.getDeviceId());

		/** Get Android ID */
		androidID = android.provider.Settings.Secure.getString(KaApp
				.getAppContext().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		androidID = (androidID == null ? "" : androidID);
		/** Get Sim Serial */
		simSerial = tm.getSimSerialNumber();
		simSerial = (simSerial == null ? "" : simSerial);
		String macAddr = "";
		try {
			WifiManager wifiMan = (WifiManager) KaApp.getAppContext()
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			macAddr = wifiInf.getMacAddress();
			macAddr = (macAddr == null ? "" : macAddr);
			macAddr = macAddr.replaceAll(":", "");
		} catch (Exception e) {
			macAddr = "";
		}
		if (deviceID.length() == 0) {
			if (macAddr.length() > 0) {
				deviceID = macAddr;
			} else if (simSerial.length() > 0) {
				deviceID = simSerial;
			} else if (androidID.length() > 0) {
				deviceID = androidID;
			}
		}
		return deviceID;
	}

	public String getIMEI() {
		return IMEI;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public String getEmail() {
		return googleAccount;
	}

	public String getOSVersion() {
		return osVersion;
	}

	public static class Connections {
		public String s3G, sWifi;
	}

	public Connections getConnections() {
		return connections;
	}

	// public String getMemory() {
	// return memory;
	// }

	public String getHardware() {
		return hardware;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getOS() {
		return "Android-" + os;
	}

	public String getManufacture() {
		return manufacture;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	
}
