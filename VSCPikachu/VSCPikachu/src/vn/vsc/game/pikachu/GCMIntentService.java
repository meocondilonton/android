package vn.vsc.game.pikachu;

import java.util.Observable;
import java.util.Observer;

import vn.vsc.game.pikachu.dc.DataHelper;
import vn.vsc.game.pikachu.dc.Result;
import vn.vsc.game.pikachu.dc.model.Payment;
import vn.vsc.game.pikachu.utility.LogUtils;
import vn.vsc.game.pikachu.utility.ResourceManager;
import vn.vsc.game.pikachu.utility.SMSUtils;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService implements Observer{
	public static final String SENDER_ID = "968541590240";
	public GCMIntentService(){
		super(SENDER_ID);
	}
	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		String objID = intent.getExtras().getString("Id");
		DataHelper.getInstance().getNotify(objID);
		DataHelper.getInstance().addObserver(this);
	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		LogUtils.e(getClass(), "registerid:"+registrationId);
		ResourceManager.saveString(ResourceManager.REG_ID, registrationId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

	}
	
	 private NotificationManager mNotificationManager;
	 public static final int NOTIFICATION_ID = 1;
	 private void sendNotification(String msg,int id) {
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			PendingIntent contentIntent = PendingIntent.getActivity(this, id,
					new Intent(this, VSCPikachu.class), 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(getString(R.string.app_name))
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
					.setAutoCancel(true).setContentText(msg);

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}

		private void sendNotificationByWeb(String msg, String link,int id) {
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			PendingIntent contentIntent = PendingIntent.getActivity(this, id,
					browserIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(getString(R.string.app_name))
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
					.setAutoCancel(true).setContentText(msg);

			mBuilder.setContentIntent(contentIntent);
			
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			DataHelper.getInstance().deleteObserver(this);
			Result result = (Result) data;
			switch (result.getParams().getMethod()) {
			case GET_NOTIFY:
				Object[][] notifies = (Object[][]) result.getData();
				for (int k = 0; k < notifies.length; k++) {
					int type = (Integer) notifies[k][1];
					int id = (Integer) notifies[k][0];
					switch (type) {
					case 0:
						// payment
						Payment[] payments = (Payment[]) notifies[k][2];
						for (int i = 0; i < payments.length; i++) {
							SMSUtils.savePayment(payments[i]);
						}
						break;
					case 1:
						// message
						sendNotification((String) notifies[k][2],id);
						break;
					case 2:
						// web
						String link = (String) notifies[k][3];
						sendNotificationByWeb((String) notifies[k][2], link,id);
						break;
					}
				}
				break;
			}
		}
}
