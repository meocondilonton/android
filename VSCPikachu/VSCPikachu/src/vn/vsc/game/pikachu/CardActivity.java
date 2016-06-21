package vn.vsc.game.pikachu;

import java.util.Observable;
import java.util.Observer;

import vn.vsc.game.pikachu.dc.DataHelper;
import vn.vsc.game.pikachu.dc.Method;
import vn.vsc.game.pikachu.dc.Result;
import vn.vsc.game.pikachu.dlg.DialogInput;
import vn.vsc.game.pikachu.dlg.DialogInput.IInput;
import vn.vsc.game.pikachu.utility.LogUtils;
import vn.vsc.game.pikachu.utility.ResourceManager;
import vn.vsc.game.pikachu.utility.SMSUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CardActivity extends Activity implements IInput,Observer {

	private static class CardItem {
		public String code;
		public int imgId;
		public int text;

		public CardItem(String code, int imgId, int text) {
			this.code = code;
			this.imgId = imgId;
			this.text = text;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(this,VSCPikachu.class);
//		startActivity(intent);
		super.onDestroy();
		
	}
	public void finish(){
//		Intent intent = new Intent(this,VSCPikachu.class);
//		startActivity(intent);
		super.finish();
	}
	private class CardAdapter extends BaseAdapter {

		public CardAdapter(CardItem[] items) {
			this.items = items;
		}

		private CardItem[] items;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (items == null)
				return 0;
			return items.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return items[arg0];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(CardActivity.this);
			}
			TextView tv = (TextView) convertView;
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			tv.setPadding(5, 5, 5, 5);
			tv.setTextColor(Color.WHITE);
			tv.setText(items[position].text);
			tv.setGravity(Gravity.CENTER);
			tv.setCompoundDrawablesWithIntrinsicBounds(items[position].imgId,
					0, 0, 0);

			return convertView;
		}

	}

	private Runnable onSendFail = new Runnable() {

		@Override
		public void run() {
			KaApp.showToast(R.string.send_sms_failed);
		}
	};
	private Runnable onReceiveSMSBuyXu = new Runnable() {

		@Override
		public void run() {
			if (Looper.myLooper() == Looper.getMainLooper()) {
				ResourceManager.addXu(10);
				KaApp.getHandler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String s = getString(R.string.buy_xu_success,"10");
						Toast.makeText(CardActivity.this, s, Toast.LENGTH_SHORT).show();
					}
				}, 100);
				
			} else
				runOnUiThread(onReceiveSMSBuyXu);
		}
	};
	private DialogInput dlgInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_card);
		ListView lvCards = (ListView) findViewById(R.id.layout_card_list);
		CardItem[] items = new CardItem[] {
				new CardItem("", R.drawable.icon_sms, R.string.text_sms),// sms15
				new CardItem("VIETTEL", R.drawable.iconviettel,
						R.string.text_viettel),// card viettel
				new CardItem("VNP", R.drawable.icon_vinaphone,
						R.string.text_vinaphone),// card vinaphone
				new CardItem("VMS", R.drawable.icon_mobifone,
						R.string.text_mobiphone),// card mobifone
		};
		dlgInput = new DialogInput(this);
		lvCards.setAdapter(new CardAdapter(items));
		lvCards.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CardItem item = (CardItem) arg0.getItemAtPosition(arg2);
				String code = item.code;
				if (TextUtils.isEmpty(code)) {
					SMSUtils.sendSMS(DataHelper.getUserID(), SMSUtils.SMS_15k,
							onReceiveSMSBuyXu, onSendFail);
				} else {
					dlgInput.show(R.string.hint_input_serial,
							R.string.hint_input_code,
							R.string.text_title_input, code, CardActivity.this,item.text);
				}
			}
		});
	}
	private ProgressDialog progressingDlg;
	@Override
	public void onInput(String text1, String text2, String codeTelco) {
		if(TextUtils.isEmpty(text1)){
			return;
		}
		if(TextUtils.isEmpty(text2)){
			return;
		}
		//send to server
		DataHelper.getInstance().addObserver(this);
		DataHelper.getInstance().purchaseCard(text1,text2,codeTelco);
		progressingDlg = ProgressDialog.show(this, "", getString(R.string.purchasing));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(progressingDlg != null && progressingDlg.isShowing())
			progressingDlg.dismiss();
	}

	@Override
	public void update(Observable observable, Object data) {
		Result result = (Result) data;
		if(result.getParams().getMethod() == Method.USING_CARD){
			DataHelper.getInstance().deleteObserver(this);
			if(progressingDlg != null && progressingDlg.isShowing())
				progressingDlg.dismiss();
			if(result.getError() == null){
				final int xu = (Integer) result.getData();
				LogUtils.e(getClass(), "nap card thanh cong:"+xu);
				ResourceManager.addXu(xu);
				
				KaApp.getHandler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e("CARDACTIVITY", "show xu info");
						String s = getString(R.string.buy_xu_success,
								xu + "");
						Toast.makeText(CardActivity.this, s, Toast.LENGTH_SHORT).show();
						if(dlgInput != null && dlgInput.isShowing()){
							dlgInput.dismiss();
						}
					}
				}, 100);
				
				
			} else{
				Toast.makeText(this, result.getError().toString(), Toast.LENGTH_SHORT).show();
			}
			return;
		}
	}
	public void close(View target){
		finish();
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
}
