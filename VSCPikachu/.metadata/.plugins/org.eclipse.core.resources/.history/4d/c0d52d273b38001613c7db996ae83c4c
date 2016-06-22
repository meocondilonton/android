package vn.vsc.game.pikachu.dc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.vsc.game.pikachu.utility.LogUtils;
import android.os.AsyncTask;
import android.os.Environment;

public class DownloadTask extends AsyncTask<String, Integer, File[]> {
	public String mDownloadDir;
	private File mDirect;
	private final int TIME_OUT_CONNECTION = 15000;
	private static final int SIZE_BUFFER = 2048;// 2 kb

	public static interface IProgressHandler {
		// change in uithread
		public void onPreExcute();

		public void onChange(int paramsIndex, int percent);

		public void onFinishAll(File[] files);

		// finish not in uithread
		@Deprecated
		public void onFinish(int paramsIndex, File f);

		// error not in uithread
		@Deprecated
		public void onError(int paramsIndex, String message);

	}

	public DownloadTask() {
		mProgressChange = new ArrayList<DownloadTask.IProgressHandler>();
		mDownloadDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/LGKK/";
	}


	private List<IProgressHandler> mProgressChange;

	public void registerHandler(IProgressHandler handler) {
		synchronized (mProgressChange) {
			if (!mProgressChange.contains(handler))
				mProgressChange.add(handler);
		}

	}

	public void unRegisterHandler(IProgressHandler handler) {
		synchronized (mProgressChange) {
			mProgressChange.remove(mProgressChange);
		}
	}

	private void processFailed(int i, String message) {
		synchronized (mProgressChange) {
			for (IProgressHandler mChange : mProgressChange) {
				mChange.onError(i, message);
			}
		}
	}

	private void processSuccess(int i, File f) {
		synchronized (mProgressChange) {
			for (IProgressHandler mChange : mProgressChange) {
				mChange.onFinish(i, f);
			}
		}
	}

	private void processChange(int i, int percent) {
		synchronized (mProgressChange) {
			for (IProgressHandler mChange : mProgressChange) {
				mChange.onChange(i, percent);
			}
		}
	}

	private void preExcute() {
		synchronized (mProgressChange) {
			for (IProgressHandler mChange : mProgressChange) {
				mChange.onPreExcute();
			}
		}
	}

	private void processFinish(File[] files) {
		synchronized (mProgressChange) {
			for (IProgressHandler mChange : mProgressChange) {
				mChange.onFinishAll(files);
			}
		}
	}

	@Override
	protected void onPostExecute(File[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		processFinish(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mDirect = new File(mDownloadDir);
		if (!mDirect.exists()) {
			mDirect.mkdirs();
		}
		preExcute();
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
//		for (int i = 0; i < values.length; i++) {
//			LogUtils.e(DownloadTask.class, "Value " + i + " : " + values[i]);
//		}
		processChange(values[0], values[1]);
		super.onProgressUpdate(values);
	}

	private String getFilesName(String url) {
		int index = url.lastIndexOf("/");
		if (index >= 0) {
			return url.substring(index);
		}
		return url;
	}

	@Override
	protected File[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		File[] mResult = new File[params.length];
		HttpURLConnection urlConnection;
		URL url;
		byte[] buffer = new byte[SIZE_BUFFER];
		FileOutputStream fos = null;
		InputStream is = null;
		for (int i = 0; i < params.length; i++) {
			try {
				
				url = new URL(params[i]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(TIME_OUT_CONNECTION);
				urlConnection.setReadTimeout(TIME_OUT_CONNECTION);
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();
				int fileSize = urlConnection.getContentLength();
				File f = new File(mDirect + "/lgkk.apk");
				fos = new FileOutputStream(f);
				LogUtils.e(getClass(), "code:"+urlConnection.getResponseCode()+"filesize:"+fileSize);
				is = urlConnection.getInputStream();
				int size = buffer.length;
				int totalByte = 0;
				boolean isEmpty = false;
				while (!isEmpty) {
					size = is.read(buffer);
					fos.write(buffer, 0, size);
					totalByte += size;
					publishProgress(i, totalByte * 100 / fileSize);
					if (totalByte >= fileSize)
						isEmpty = true;
				}
				mResult[i] = f;
				processSuccess(i, mResult[i]);
			} catch (MalformedURLException e) {
				processFailed(i, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				processFailed(i, e.getMessage());
				e.printStackTrace();
			} catch (ClassCastException e) {
				processFailed(i, e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					fos.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		// processFinish();
		return mResult;
	}

}
