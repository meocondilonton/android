package vn.vsc.game.pikachu.facebook;

import java.util.List;

import vn.vsc.game.pikachu.KaApp;
import vn.vsc.game.pikachu.R;
import vn.vsc.game.pikachu.dc.model.Ranking;
import vn.vsc.game.pikachu.utility.Util;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmanit.lib.imagemanager.ImageManager;

public class RankingAdapter extends BaseAdapter {
	public List<Ranking> mListRanking;
	public RankingAdapter(){
	}
	public void setListRanking(List<Ranking> rankings){
		mListRanking = rankings;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mListRanking != null)
			return mListRanking.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListRanking.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) KaApp.getAppContext().getSystemService(KaApp.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_item_ranking, null);
		}
		TextView tvLeft = (TextView) convertView.findViewById(R.id.item_ranking_left);
		TextView tvCenter = (TextView) convertView.findViewById(R.id.item_ranking_center);
		TextView tvRight = (TextView) convertView.findViewById(R.id.item_ranking_right);
		Ranking rank = mListRanking.get(position);
		tvLeft.setTextColor(Color.WHITE);
		tvLeft.setText(rank.getUserName());
		tvCenter.setTextColor(Color.WHITE);
		tvCenter.setText(Util.formatMoney(rank.getScore()));
		tvRight.setTextColor(Color.WHITE);
		tvRight.setText(Util.formatMoney(rank.getQuestion()));
		ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.ranking_avatar);
		String address = "http://graph.facebook.com/"
				+ rank.getFBID() + "/picture";
		imgAvatar.setVisibility(View.VISIBLE);
		ImageManager.getInstance().push(address, imgAvatar);
		return convertView;
	}

}
