package vn.philong.game.pikachunew.dc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONStringer;

import vn.philong.game.pikachunew.BuildConfig;
import vn.philong.game.pikachunew.KaApp;
import vn.philong.game.pikachunew.utility.DeviceInfo;
import vn.philong.game.pikachunew.utility.LogUtils;
import vn.philong.game.pikachunew.utility.ResourceManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataHelper extends Observable {
	private static final String TAG = "DataHelper";
	private ConnectivityManager mCm;
	private final int MAX_SIZE_POOL = 3;

	private static DataHelper mInstance;

	private final int TIME_OUT_CONNECTION = 15000;

	private LinkedHashMap<String, HttpTask> mPoolExcutingTask;
	private Queue<HttpTask> mWaitingTask;

	private HashMap<String, String> HEADER_MAP;

	public void loadHeader() {
		DeviceInfo device = DeviceInfo.getInstance();
		HEADER_MAP = new HashMap<String, String>();
		HEADER_MAP.put("ig", device.getConnections().s3G);
		HEADER_MAP.put("iw", device.getConnections().sWifi);
		HEADER_MAP.put("im", device.getHardware());
		HEADER_MAP.put("pn", device.getManufacture());
		HEADER_MAP.put("osn", device.getOS());
		HEADER_MAP.put("osv", device.getOSVersion());
		HEADER_MAP.put("dn", device.getDeviceName());
		HEADER_MAP.put("did", device.getDeviceID());
		HEADER_MAP.put("imei", device.getIMEI());
		HEADER_MAP.put("vn", device.getVersionName());
		HEADER_MAP.put("vc", device.getVersionCode());
		HEADER_MAP.put("chn", DeviceInfo.REAL_CHANEL + "");
	}

	public static DataHelper getInstance() {
		if (mInstance == null) {
			synchronized (DataHelper.class) {
				if (mInstance == null) {
					mInstance = new DataHelper();
				}
			}
		}
		return mInstance;
	}

	private DataHelper() {
		super();
		loadHeader();
		mWaitingTask = new ConcurrentLinkedQueue<DataHelper.HttpTask>();
		mPoolExcutingTask = new LinkedHashMap<String, DataHelper.HttpTask>(
				MAX_SIZE_POOL) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					java.util.Map.Entry<String, HttpTask> eldest) {
				if (size() > MAX_SIZE_POOL) {
					eldest.getValue().cancelTask();
					return true;
				} else {
					return false;
				}
			}
		};

		mCm = (ConnectivityManager) KaApp.getAppContext().getSystemService(
				Context.CONNECTIVITY_SERVICE);
	}

	private class HttpTask extends AsyncTask<Void, Void, Result> {
		Params mParams;
		String mUrl;
		Map<String, String> mPostData;
		HttpURLConnection mHttpUrlConnection;

		public HttpTask(String url, Params params) {
			mUrl = url;
			mParams = params;
		}

		public HttpTask(String url, Params params, Map<String, String> postdata) {
			mUrl = url;
			mPostData = postdata;
			mParams = params;
		}

		public void cancelTask() {
			if (mHttpUrlConnection != null)
				mHttpUrlConnection.disconnect();
			cancel(true);
			mWaitingTask.add(new HttpTask(mUrl, mParams, mPostData));

		}

		@Override
		protected Result doInBackground(Void... params) {
			Result result = null;
			result = getFromCache();
			if (result != null)
				return result;

			if (mCm != null
					&& (mCm.getActiveNetworkInfo() == null || !mCm
							.getActiveNetworkInfo().isConnectedOrConnecting())) {
				result = setError(Error.NETWORK_NO_CONNECTION);
			} else {
				try {
					URL u = new URL(mUrl);

					mHttpUrlConnection = (HttpURLConnection) u.openConnection();
					mHttpUrlConnection.setConnectTimeout(TIME_OUT_CONNECTION);
					mHttpUrlConnection.setReadTimeout(TIME_OUT_CONNECTION);
					mHttpUrlConnection.setDoInput(true);

					result = request(mHttpUrlConnection,
							getParamsString(mPostData), mParams);

					addToCache(result);

					return result;
				} catch (FileNotFoundException e) {
					Log.e(TAG, "method " + mParams.getMethod().name()
							+ " FileNotFoundException = " + e.toString());
					result = setError(Error.SERVER_ERROR);
				} catch (SocketTimeoutException e) {
					result = setError(Error.NETWORK_TIMED_OUT);
				} catch (ConnectTimeoutException e) {
					result = setError(Error.NETWORK_TIMED_OUT);
				} catch (IOException e) {
					Log.e(TAG, "method " + mParams.getMethod().name()
							+ " IOException = " + e.toString());
					result = setError(Error.SERVER_ERROR);
				} catch (IllegalStateException e) {
					Log.e(TAG, "method " + mParams.getMethod().name()
							+ " IllegalStateException = " + e.toString());
					result = setError(Error.SERVER_ERROR);
					e.printStackTrace();
				} catch (Exception e) {
					Log.e(TAG, "method " + mParams.getMethod().name()
							+ " IllegalStateException = " + e.toString());
					result = setError(Error.UNKNOWN_ERROR);
					e.printStackTrace();
				} finally {
					if (mHttpUrlConnection != null) {
						mHttpUrlConnection.disconnect();
					}
				}
			}
			return result;
		}

		private Result getFromCache() {
			Result result = null;
			switch (mParams.getMethod()) {
			case CHECK_UPDATE:
				return null;
			case GET_NOTIFY:
				return null;
			case GET_RANKING:
				return null;
			case SYNC:
				return null;
			default:
				break;
			}
			return result;
		}

		private void addToCache(Result result) {
			if (result == null || result.getError() != null)
				return;

			switch (mParams.getMethod()) {
			case CHECK_UPDATE:
				break;
			case GET_NOTIFY:
				break;
			case GET_RANKING:
				break;
			case SYNC:
				break;
			default:
				break;
			}
		}

		private Result setError(Error error) {
			Result result = new Result();
			result.setError(error);
			result.setParams(mParams);

			return result;
		}

		@SuppressLint({ "NewApi", "NewApi" })
		@Override
		protected void onPostExecute(Result result) {
			synchronized (mPoolExcutingTask) {
				mPoolExcutingTask.remove(mUrl);
				if (mPoolExcutingTask.size() < MAX_SIZE_POOL) {
					HttpTask task = mWaitingTask.poll();
					if (task != null) {
						mPoolExcutingTask.put(task.mUrl, task);
						// if (android.os.Build.VERSION.SDK_INT >= 11) {
						// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						// } else {
						// task.execute();
						// }
						task.execute();
					}
				}
			}

			setChanged();
			notifyObservers(result);
		}
	}

	@SuppressLint("NewApi")
	private void excute(String url, Params param) {
		synchronized (mPoolExcutingTask) {
			if (mPoolExcutingTask.get(url) == null) {
				HttpTask task = new HttpTask(url, param);
				mPoolExcutingTask.put(url, task);
				// if (android.os.Build.VERSION.SDK_INT >= 11)
				// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				// else
				// task.execute();
				task.execute();
				if (BuildConfig.DEBUG)
					Log.e(TAG, url);
			}
		}
	}

	@SuppressLint("NewApi")
	private void excute(String url, Map<String, String> data, Params param) {
		synchronized (mPoolExcutingTask) {
			HttpTask task;
			if ((task = mPoolExcutingTask.get(url)) == null
					|| !data.equals(task.mPostData)) {
				task = new HttpTask(url, param, data);
				mPoolExcutingTask.put(url, task);
				// if (android.os.Build.VERSION.SDK_INT >= 11)
				// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				// else
				// task.execute();
				task.execute();
				if (BuildConfig.DEBUG)
					Log.e(TAG, url + "_" + String.valueOf(data));
			}
		}
	}

	private static Result request(HttpURLConnection conn, String postData,
			Params param) throws Exception {

		Result result;
		if (postData != null) {
			conn.setRequestMethod("POST");
			LogUtils.e(DataHelper.class, "method: post");
			if (!postData.equals("")) {
				conn.setDoOutput(true);
				OutputStream out = new BufferedOutputStream(
						conn.getOutputStream());
				out.write(postData.getBytes());
				out.flush();
				out.close();
			}
		} else {
			LogUtils.e(DataHelper.class, "method: get");
			conn.setRequestMethod("GET");
		}

		int httpCode = conn.getResponseCode();
		if (httpCode == HttpURLConnection.HTTP_OK
				|| httpCode == HttpURLConnection.HTTP_CREATED) {
			InputStream in = new BufferedInputStream(conn.getInputStream());
			// LogUtils.e(DataHelper.class, "home:"+convertStreamToString(in));
			String s = convertStreamToString(in);
			result = Parser.parse(s, param);
			if (result == null)
				return null;
			result.setParams(param);
			return result;
		} else {
			Log.e(TAG, "Error, response code = " + httpCode);
			result = new Result();
			result.setParams(param);
			result.setError(Error.SERVER_ERROR);
		}
		return result;
	}

	public static String convertStreamToString(InputStream is) {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				return "";
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	private String getParamsString(Map<String, String> params) {
		if (params == null)
			return null;
		String ret = "";
		for (String key : params.keySet()) {

			String value = params.get(key);
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			ret += key + "=" + value + "&";
		}
		if (!ret.equals(""))
			return ret.substring(0, ret.length() - 1);
		return ret;
	}

	private HashMap<String, String> getHeaderMap(String regID) {
		HashMap<String, String> header = new HashMap<String, String>(HEADER_MAP);
		header.put("t",
				ResourceManager.loadAsString(ResourceManager.FACEBOOK_TOKEN));
		header.put("regid", regID);
		header.put("uid", getUserID() + "");
		return header;
	}

	// ///////////////////////////////////////
	public void checkUpdate(String regID,String phone) {
		HashMap<String, String> map = getHeaderMap(regID);
		map.put("phone", phone);
		excute(API.CHECK_UPDATE, map, new Params(Method.CHECK_UPDATE));
	}

	public void getNotify(String id) {
		HashMap<String, String> map = getHeaderMap(ResourceManager
				.loadAsString(ResourceManager.REG_ID));
		map.put("nid", id);
		excute(API.GET_NOTIFY, map, new Params(Method.GET_NOTIFY));
	}

	public void getRanking(String regID) {
		HashMap<String, String> map = getHeaderMap(regID);
//		map.put("sid", season + "");
		excute(API.GET_RANKING, map, new Params(Method.GET_RANKING));

	}

	public void purchaseCard(String text1, String text2, String code) {
		HashMap<String, String> map = getHeaderMap(ResourceManager
				.loadAsString(ResourceManager.REG_ID));
		map.put("tel", code + "");
		map.put("pin", text2 + "");
		map.put("seri", text1 + "");
		excute(API.USING_CARD, map, new Params(Method.USING_CARD));
	}

	// utils
	public static final String MY_USER_ID = "myUserID";

	public static void saveUserID(int userID) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(KaApp.getAppContext());
		pref.edit().putInt(MY_USER_ID, userID).commit();
	}

	public static int getUserID() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(KaApp.getAppContext());
		return pref.getInt(MY_USER_ID, 0);
	}

	public void sync(int levelID, int time, String regID) {
		HashMap<String, String> map = getHeaderMap(regID);
		JSONStringer jStringer = new JSONStringer();
		try {
			jStringer.array();
			jStringer.object();
			jStringer.key("LevelId").value(levelID);
			jStringer.key("Score").value(time);
			jStringer.key("LastUpdate").value(0);
			jStringer.endObject();
			jStringer.endArray();
		} catch (Exception e) {
		}
		map.put("data", jStringer.toString());
		excute(API.SYNC, map, new Params(Method.SYNC));
	}
}
