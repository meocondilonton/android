package vn.philong.game.pikachunew.facebook;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import vn.philong.game.pikachunew.KaApp;
import vn.philong.game.pikachunew.R;
import vn.philong.game.pikachunew.dc.DataHelper;
import vn.philong.game.pikachunew.dc.Result;
import vn.philong.game.pikachunew.dc.model.Ranking;
import vn.philong.game.pikachunew.utility.LogUtils;
import vn.philong.game.pikachunew.utility.ResourceManager;
import vn.philong.game.pikachunew.utility.Util;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class FragmentRanking extends Fragment implements OnClickListener,
		Observer, OnCheckedChangeListener {
	private View vProgressBar, vContent;
	private RadioGroup groupButtons;
	private List<Ranking> listFriends, listAll;
	private ListView mListView;
	private RankingAdapter mAdapter;

	private TextView rankingAll, rankingFriend, rankingLevel, rankingPoint,
			rankingQuestion, rankUsername;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.layout_ranking, container, false);
		vContent = view.findViewById(R.id.ranking_content);
		vProgressBar = view.findViewById(R.id.ranking_progress);
		mListView = (ListView) view.findViewById(R.id.ranking_list);
		groupButtons = (RadioGroup) view.findViewById(R.id.ranking_group);
		mAdapter = new RankingAdapter();
		mListView.setAdapter(mAdapter);
		View vClose = view.findViewById(R.id.ranking_close);
		vClose.setOnClickListener(this);
		rankingAll = (TextView) view.findViewById(R.id.ranking_rank_all);
		rankingFriend = (TextView) view.findViewById(R.id.ranking_rank_friend);
		rankingLevel = (TextView) view.findViewById(R.id.ranking_level);
		rankingPoint = (TextView) view.findViewById(R.id.ranking_point);
		rankingQuestion = (TextView) view.findViewById(R.id.ranking_question);
		rankUsername = (TextView) view.findViewById(R.id.ranking_rank_username);
		groupButtons.setOnCheckedChangeListener(this);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.ranking_bt_friend) {
			if (listFriends != null) {
				mAdapter.setListRanking(listFriends);
				mAdapter.notifyDataSetChanged();
			}
		} else if (checkedId == R.id.ranking_bt_world) {
			if (listAll != null) {
				mAdapter.setListRanking(listAll);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void request() {
		DataHelper.getInstance().addObserver(this);
		// native load values
		DataHelper.getInstance().sync(ResourceManager.getLevel(), ResourceManager.getTime(),
				ResourceManager.loadAsString(ResourceManager.REG_ID));
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		LogUtils.e(getClass(), "updating");
		Result result = (Result) data;
		if (result.getError() != null) {
			LogUtils.e(getClass(), "error:" + result.getError().toString());
			try {
				KaApp.showToast(R.string.connection_problem);
				DataHelper.getInstance().deleteObserver(this);
				getActivity().finish();
			} catch (Exception e) {

			}
		} else {
			LogUtils.e(getClass(), "updating:" + result.getParams().getMethod());
			switch (result.getParams().getMethod()) {
			case SYNC:
				DataHelper.getInstance().getRanking(
						ResourceManager.loadAsString(ResourceManager.REG_ID));
				break;
			case GET_RANKING:
				LogUtils.e(getClass(), "updating:get ranking");
				Object[] datas = (Object[]) result.getData();
				listAll = (List<Ranking>) datas[0];
				listFriends = (List<Ranking>) datas[1];
				Ranking myUser = (Ranking) datas[2];
				vContent.setVisibility(View.VISIBLE);
				vProgressBar.setVisibility(View.GONE);
				switch (groupButtons.getCheckedRadioButtonId()) {
				case R.id.ranking_bt_friend:
					mAdapter.setListRanking(listFriends);
					mAdapter.notifyDataSetChanged();
					break;
				case R.id.ranking_bt_world:
					mAdapter.setListRanking(listAll);
					mAdapter.notifyDataSetChanged();
					break;
				}
				try {
					rankUsername.setTextColor(Color.YELLOW);
					rankUsername.setText(myUser.getUserName());
					rankingAll.setText(getString(R.string.ranking_all,
							myUser.getRankingAll() + ""));
					rankingFriend.setText(getString(R.string.ranking_friend,
							myUser.getRankingFriend() + ""));
					rankingLevel.setText(getString(R.string.info_level,
							+myUser.getLevel() + ""));
					rankingPoint.setText(getString(R.string.info_point,
							Util.formatMoney(myUser.getScore())));
					rankingQuestion.setText(getString(R.string.info_question,
							myUser.getQuestion() + ""));
					DataHelper.getInstance().deleteObserver(this);
				} catch (Exception e) {
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		getActivity().finish();
	}

}
