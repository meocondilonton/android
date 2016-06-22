package vn.philong.game.pikachunew.utility;

import vn.philong.game.pikachunew.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Util {
	public static String formatMoney(long money) {
		boolean isAm = false;
		if (money < 0) {
			isAm = true;
			money = Math.abs(money);
		}
		StringBuilder sBuilder = new StringBuilder();
		while (money >= 1000) {
			if (money % 1000 != 0) {
				int m = (int) (money % 1000);
				sBuilder.insert(0, money % 1000);
				if (m < 10)
					sBuilder.insert(0, "00");
				else if (m < 100)
					sBuilder.insert(0, "0");
			} else
				sBuilder.insert(0, "000");
			sBuilder.insert(0, ",");
			money = money / 1000;
		}
		sBuilder.insert(0, money % 1000);
		if (isAm)
			sBuilder.insert(0, "-");
		return sBuilder.toString();
	}
	private static NotificationManager mNotificationManager;
	private static final int NOTIFICATION_ALERT_ID = 500;
	public static void showAlertNotification(Context context) {
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notifyDetails = new Notification(R.drawable.ic_launcher,
				context.getString(R.string.alert_content),
				System.currentTimeMillis());
		Intent notifyIntent = null;
		notifyIntent = new Intent(context, context.getClass());
		notifyDetails.flags = Notification.FLAG_AUTO_CANCEL;
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		CharSequence contentTitle = context.getString(R.string.app_name);
		CharSequence contentText = context.getString(R.string.alert_content);
		notifyDetails.setLatestEventInfo(context, contentTitle, contentText,
				intent);
		mNotificationManager.notify(NOTIFICATION_ALERT_ID, notifyDetails);
	}

	public static void clearAlertNotification() {
		try {
			mNotificationManager.cancel(NOTIFICATION_ALERT_ID);
		} catch (Exception e) {
		}
	}
	
	
}
