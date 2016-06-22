package vn.philong.game.pikachunew.dlg;

import vn.philong.game.pikachunew.R;
import android.app.Activity;
import android.widget.ListView;
import android.widget.RadioButton;

public class DialogRanking extends DialogLayout {
	private ListView mLvRanking;
	private RadioButton btFriend,btWorld;
	public DialogRanking(Activity context) {
		super(context, R.layout.layout_ranking, R.id.ranking_close);
	}

	@Override
	protected void init() {
		btFriend = (RadioButton) findViewById(R.id.ranking_bt_friend);
		btWorld = (RadioButton) findViewById(R.id.ranking_bt_world);
		mLvRanking = (ListView) findViewById(R.id.ranking_list);
	}

}
