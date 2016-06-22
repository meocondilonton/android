/****************************************************************************
Copyright (c) 2010-2011 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 ****************************************************************************/
package vn.philong.game.pikachunew;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.json.JSONObject;
import org.json.JSONStringer;

import vn.philong.game.pikachunew.dc.DataHelper;
import vn.philong.game.pikachunew.dc.Result;
import vn.philong.game.pikachunew.dlg.DialogInput;
import vn.philong.game.pikachunew.dlg.DlgConfirm;
import vn.philong.game.pikachunew.facebook.RankingActivity;
import vn.philong.game.pikachunew.persional.PersonalActivity;
import vn.philong.game.pikachunew.utility.FBUtils;
import vn.philong.game.pikachunew.utility.LogUtils;
import vn.philong.game.pikachunew.utility.ResourceManager;
import vn.philong.game.pikachunew.utility.SMSUtils;
import vn.philong.game.pikachunew.utility.Util;
import vn.philong.game.pikachunew.view.BroadcaseMessage;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.easyndk.classes.AndroidNDKHelper;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class VSCPikachu extends Cocos2dxActivity implements Observer {
	private DlgConfirm dlgConfirm;
	private DialogInput dlgInput;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidNDKHelper.SetNDKReciever(this);
		dlgConfirm = new DlgConfirm(this);
		dlgInput = new DialogInput(this);
		checkUpdate();
		initNotification();
		initViews();
		
	
		interstitial = new InterstitialAd(this);
		
		interstitial.setAdUnitId("ca-app-pub-5869820531988531/7179288209");
//		AdRequest adRequest = new AdRequest.Builder().build();
		AdRequest adRequest = new AdRequest.Builder()
//        .addTestDevice("866636024233433")
	    .build();
		interstitial.loadAd(adRequest);
		
		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub
				super.onAdClosed();
				AndroidNDKHelper.SendMessageWithParameters("callExit", null);
			}
			
		});
	}
	private InterstitialAd interstitial;
	 
	private void initViews() {
		FrameLayout.LayoutParams adViewLp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		adViewLp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
	}
	@Override
	public void onResume() {
		super.onResume();
		Util.clearAlertNotification();
	
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		 
//		 AndroidNDKHelper.SendMessageWithParameters(
//		 "callExit",
//		 null);
//		 finish();
	}

	private void initNotification() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		String regID = GCMRegistrar.getRegistrationId(this);
		if (regID.length() == 0) {
			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		} else {
			ResourceManager.saveString(ResourceManager.REG_ID, regID);
		}
	}

	public Cocos2dxGLSurfaceView onCreateView() {
		Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);
		// VSCPikachu should create stencil buffer
		glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);

		return glSurfaceView;
	}

	static {
		System.loadLibrary("cocos2dcpp");
	}
	private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy");;

	private static String formatDate(Date date) {
		String reportDate = df.format(date);
		return reportDate;
	}

	public void getXu(JSONObject prms) {
		try {
			String method = prms.getString("xu");
			Log.e("VSCPIKACHU", "handle add xu:"+method);
			int i = Integer.parseInt(method);
			ResourceManager.updateGameXu(i);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onStop() {
		super.onStop();
		if (!isFinishing()) {
			Util.showAlertNotification(this);
		} else {
			Util.clearAlertNotification();
		}
	}

	public void getLevel(JSONObject prms) {
		try {
			String level = prms.getString("level");
			String time = prms.getString("time");
			ResourceManager.setLevelAndTime(Integer.parseInt(level),
					Integer.parseInt(time));
		} catch (Exception e) {

		}
	}

	private Runnable onSendFail = new Runnable() {

		@Override
		public void run() {
			KaApp.showToast(R.string.send_sms_failed);
		}
	};

	public void sms15(JSONObject prms) {
		try {
			final String callback = prms.getString("callback");
			SMSUtils.sendSMS(DataHelper.getUserID(), SMSUtils.SMS_15k,
					new Runnable() {

						@Override
						public void run() {
							try {
								KaApp.showToast(R.string.send_sms_success);
								JSONStringer jStringer = new JSONStringer();
								jStringer.object().key("value").value("2")
										.endObject();
								AndroidNDKHelper.SendMessageWithParameters(
										callback,
										new JSONObject(jStringer.toString()));
							} catch (Exception e) {

							}
						}
					}, onSendFail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sms5(JSONObject prms) {
		try {
			final String callback = prms.getString("callback");
			SMSUtils.sendSMS(DataHelper.getUserID(), SMSUtils.SMS_5k,
					new Runnable() {

						@Override
						public void run() {
							try {
								KaApp.showToast(R.string.send_sms_success);
								JSONStringer jStringer = new JSONStringer();
								jStringer.object().key("value").value("1")
										.endObject();
								AndroidNDKHelper.SendMessageWithParameters(
										callback,
										new JSONObject(jStringer.toString()));
							} catch (Exception e) {

							}
						}
					}, onSendFail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sms2(JSONObject prms) {
		try {
			final String callback = prms.getString("callback");
			SMSUtils.sendSMS(DataHelper.getUserID(), SMSUtils.SMS_2K,
					new Runnable() {

						@Override
						public void run() {
							try {
								KaApp.showToast(R.string.send_sms_success);
								JSONStringer jStringer = new JSONStringer();
								jStringer.object().key("value").value("0")
										.endObject();
								AndroidNDKHelper.SendMessageWithParameters(
										callback,
										new JSONObject(jStringer.toString()));
							} catch (Exception e) {

							}
						}
					}, onSendFail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void timeLeft(JSONObject prms) {
		// KaApp.showToast(R.string.time_left);
		for(int i = 0 ; i < 2 ; i++){
		Toast  toast = Toast.makeText(this, R.string.time_left, Toast.LENGTH_LONG);
		toast.show();
		}
	}

	public static int randomRes[] = { R.string.sharing_get_xu_1,
			R.string.sharing_get_xu_2, R.string.sharing_get_xu_3 };
	public static int randomRes2[] = { R.string.sharing_win_1,
			R.string.sharing_win_2, R.string.sharing_win_3 };

	public void shareFB(JSONObject prms) {
		if (dlgConfirm.isShowing())
			dlgConfirm.dismiss();
		String level = "1";
		try {
			level = prms.getString("level");
		} catch (Exception e) {

		}
		dlgConfirm.show(getString(R.string.confirm_share_fb_win, level),
				new Runnable() {

					@Override
					public void run() {
						Toast.makeText(VSCPikachu.this,
								getString(R.string.sharing_fb),
								Toast.LENGTH_SHORT).show();

						FBUtils.shareFB(
								VSCPikachu.this,
								getString(randomRes2[(int) (System
										.currentTimeMillis() % randomRes.length)]),
								new Runnable() {

									@Override
									public void run() {
										Date date = Calendar.getInstance()
												.getTime();
										String dateString = formatDate(date);
										if (!ResourceManager
												.isSavedDate(dateString)) {
											ResourceManager.addXu(1);
											ResourceManager
													.saveDate(dateString);
											ResourceManager.loadXu();
											KaApp.getHandler().postDelayed(
													new Runnable() {

														@Override
														public void run() {
															// TODO
															// Auto-generated
															// method
															// stub
															KaApp.showToast(getString(
																	R.string.get_xu_success,
																	"1",
																	ResourceManager
																			.getXu()
																			+ ""));
														}
													}, 100);
										} else {
											KaApp.showToast(R.string.getted_xu_from_fb);
										}
									}
								});

					}
				}, true);
	}

	public void onBack(JSONObject prms) {
		onBackPressed();
	}
	public void callbackBroadcast(JSONObject prms){
		BroadcaseMessage.startMessage();
	}
	public void nativeCall(JSONObject prms) {
		try {
			String method = prms.getString("method");
			if (method.equals("o")) {
				// open persional
				Intent intent = new Intent(this, PersonalActivity.class);
				startActivity(intent);
			} else if (method.equals("p")) {
				// purcharse
				Intent intent = new Intent(this, CardActivity.class);
				startActivity(intent);
			} else if (method.equals("a")) {
				// arrange
				Intent intent = new Intent(this, RankingActivity.class);
				startActivity(intent);
			} else if (method.equals("f")) {
				// free xu
				ResourceManager.requestLevel();
				if (!FBUtils.isLoginFB()) {
					dlgConfirm.show(getString(R.string.remind_login_fb),
							new Runnable() {

								@Override
								public void run() {
									Intent intent = new Intent(VSCPikachu.this,
											RankingActivity.class);
									startActivity(intent);
								}
							}, true);
					return;
				}
				dlgConfirm.show(getString(R.string.confirm_share_fb),
						new Runnable() {

							@Override
							public void run() {
								Toast.makeText(VSCPikachu.this,
										getString(R.string.sharing_fb),
										Toast.LENGTH_SHORT).show();

								FBUtils.shareFB(
										VSCPikachu.this,
										getString(randomRes[(int) (System
												.currentTimeMillis() % randomRes.length)]),
										new Runnable() {

											@Override
											public void run() {
												Date date = Calendar
														.getInstance()
														.getTime();
												String dateString = formatDate(date);
												if (!ResourceManager
														.isSavedDate(dateString)) {
													ResourceManager.addXu(1);
													ResourceManager
															.saveDate(dateString);
													ResourceManager.loadXu();
													KaApp.getHandler()
															.postDelayed(
																	new Runnable() {

																		@Override
																		public void run() {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			KaApp.showToast(getString(
																					R.string.get_xu_success,
																					"1",
																					ResourceManager
																							.getXu()
																							+ ""));
																		}
																	}, 100);
												} else {
													KaApp.showToast(R.string.getted_xu_from_fb);
												}
											}
										});

							}
						}, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		LogUtils.e(getClass(), "update data");
		Result result = (Result) data;
		try {
			switch (result.getParams().getMethod()) {
			case CHECK_UPDATE:
//				Object[] updates = (Object[]) result.getData();
//				int versionCode = (Integer) updates[0];
//				String versionName = (String) updates[1];
//				String link = (String) updates[2];
//				String desc = (String) updates[3];
//				broadcastView.runNextText();
				BroadcaseMessage.callMessage();
				break;
			default:
				return;
			}
		} catch (Exception e) {
			 LogUtils.exception(e);
			
		}
		DataHelper.getInstance().deleteObserver(this);
	}

	private void checkUpdate() {
		String phone = ResourceManager
				.loadAsString(ResourceManager.PHONE_NUMBER);
		if (TextUtils.isEmpty(phone)) {
			dlgInput.show(R.string.hint_input_phone,R.string.input_phone_number , new DialogInput.IInput() {
				
				@Override
				public void onInput(String text1, String text2, String codeTelco) {
					if(text1.length() < 10){
						KaApp.showToast(R.string.input_phone_number_valid);
						return;
					}
					DataHelper.getInstance();
					DataHelper.getInstance().addObserver(VSCPikachu.this);
					DataHelper
							.getInstance()
							.checkUpdate(
									ResourceManager
											.loadAsString(ResourceManager.REG_ID),
											text1);
					ResourceManager.saveString(ResourceManager.PHONE_NUMBER, text1);
					ResourceManager.addXu(5);
					dlgInput.dismiss();
				}

				@Override
				public void onCancel() {
					DataHelper.getInstance();
					DataHelper.getInstance().addObserver(VSCPikachu.this);
					DataHelper
							.getInstance()
							.checkUpdate(
									ResourceManager
											.loadAsString(ResourceManager.REG_ID),
									"");
				}
			});
		} else {
			DataHelper.getInstance().addObserver(this);
			DataHelper
					.getInstance()
					.checkUpdate(
							ResourceManager
									.loadAsString(ResourceManager.REG_ID),
							phone);
		}
	}

	private static final int DELAY_TIME_EXIT = 3000;
	private DlgConfirm dlgGoodbye;

	private long lastPressExit = 0;

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastPressExit < DELAY_TIME_EXIT) {
			lastPressExit = 0;
		
				if (interstitial.isLoaded() ) {
					interstitial.show();
					 
				} else finish();

		} else {
			lastPressExit = currentTime;
			Toast.makeText(this, R.string.confirm_exit, DELAY_TIME_EXIT).show();
		}
	}
}
