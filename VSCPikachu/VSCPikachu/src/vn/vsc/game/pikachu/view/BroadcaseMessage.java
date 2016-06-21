package vn.vsc.game.pikachu.view;

import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;
import org.json.JSONStringer;




import vn.vsc.game.pikachu.KaApp;
import vn.vsc.game.pikachu.utility.LogUtils;

import com.easyndk.classes.AndroidNDKHelper;

public class BroadcaseMessage {
	private static String currentText = "tesstttttttttttttttt";
	private static long currentDelay = 30000;

	public static String getCurrentText() {
		return currentText;
	}

	public static void setCurrentText(String currentText) {
		BroadcaseMessage.currentText = currentText;
	}

	public static long getCurrentDelay() {
		return currentDelay;
	}

	public static void setCurrentDelay(long currentDelay) {
		BroadcaseMessage.currentDelay = currentDelay;
	}

	public int getMilisecond() {
		return milisecond;
	}

	public void setMilisecond(int milisecond) {
		this.milisecond = milisecond;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private BroadcaseMessage mCurrentMessage;
	private static Queue<BroadcaseMessage> listMessage = new LinkedList<BroadcaseMessage>();
	private int milisecond;
	private String text;

	public static void addMessage(int s, String text) {
		BroadcaseMessage mess = new BroadcaseMessage();
		mess.setMilisecond(s * 1000);
		mess.setText(text);
		listMessage.add(mess);
	}
	
	public static void startMessage() {
		BroadcaseMessage mess = listMessage.poll();
		if (mess != null) {
			currentDelay = mess.getMilisecond();
			currentText = mess.getText();
			listMessage.add(mess);
			try {
				JSONStringer jStringer = new JSONStringer();
				jStringer.object().key("text").value(currentText)
						.key("delay").value(currentDelay / 1000)
						.endObject();
				AndroidNDKHelper.SendMessageWithParameters(
						"startMessage",
						new JSONObject(jStringer.toString()));
			} catch (Exception e) {
				
			}
		}
	}
	public static void callMessage(){
		LogUtils.e(BroadcaseMessage.class, "call message:"+currentText);
		BroadcaseMessage mess = listMessage.poll();
//		mess = new BroadcaseMessage();
//		mess.text = "ssdssdf asdlasj laskl askja djaskj asash asdsas  adsads ";
//		mess.milisecond = 10000;
		if (mess != null) {
			currentDelay = mess.getMilisecond();
			currentText = mess.getText();
			listMessage.add(mess);
			KaApp.getHandler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					LogUtils.e(BroadcaseMessage.class, "runMessage:"+currentText);
					try {
						JSONStringer jStringer = new JSONStringer();
						jStringer.object().key("text").value(currentText)
								.key("delay").value(currentDelay / 1000)
								.endObject();
						AndroidNDKHelper.SendMessageWithParameters(
								"startMessage",
								new JSONObject(jStringer.toString()));
					} catch (Exception e) {
						
					}
				}
			}, currentDelay);
			
		}
	}
}
