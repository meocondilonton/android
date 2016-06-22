/**
 * Parser.java
 * 
 * Purpose              :
 * 
 * Optional info        : 
 *
 * @author              : Van Hoang Phuong
 * 
 * @date                : 10 Jun 2013
 * 
 * @lastChangedRevision : 
 *
 * @lastChangedDate     :
 *
 */
package vn.philong.game.pikachunew.dc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.philong.game.pikachunew.CrossSaleManager;
import vn.philong.game.pikachunew.dc.model.Payment;
import vn.philong.game.pikachunew.dc.model.Ranking;
import vn.philong.game.pikachunew.utility.LogUtils;
import vn.philong.game.pikachunew.utility.ResourceManager;
import vn.philong.game.pikachunew.utility.SMSUtils;
import vn.philong.game.pikachunew.view.BroadcaseMessage;

/**
 * @author Van Hoang Phuong
 * 
 */
public class Parser {
	public static Result parse(String json, Params params) throws Exception {
		switch (params.getMethod()) {
		case CHECK_UPDATE:
			return parseUpdate(json);
		case GET_NOTIFY:
			return parseNotify(json);
		case GET_RANKING:
			return parseRanking(json);
		case USING_CARD:
			return parseCard(json);
		case SYNC:
			return parseSync(json);
		default:
			return null;

		}
	}

	// ///////////////////// COMMON PARSE ////////////////////////
	private static Result parseSync(String reader) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		LogUtils.e(Parser.class, "reader:" + reader);
		JSONObject jObj = new JSONObject(reader);
		int code = jObj.getInt("code");
		if (code == 0) {
			result.setData(1);
		} else
			result.setError(Error.SERVER_ERROR);
		return result;
	}
	private static Result parseCard(String json) throws Exception{
		// TODO Auto-generated method stub
		LogUtils.e(Parser.class, json);
		Result result = new Result();
		JSONObject jObj = new JSONObject(json);
		int code = jObj.getInt("code");
		switch(code){
		case 0:
			JSONObject data = jObj.getJSONObject("obj");
			int coin = data.getInt("Coin");
			result.setData(coin);
			break;
		case 1://card khong hop le
			result.setError(Error.CARD_NOT_VALID);
			break;
		case 2://card da duoc su dung
			result.setError(Error.CARD_USED);
			break;
		default:
			result.setError(Error.UNKNOWN_ERROR);
			break;
		}
		return result;
	}

	// ///////////////////// RESULT PARSE ////////////////////////

	private static Result parseRanking(String reader) throws Exception {
		Result result = new Result();
		LogUtils.e(Parser.class, "string:" + reader);
		JSONObject jObj = new JSONObject(reader);
		int code = jObj.getInt("code");
		if (code == 0) {
			JSONObject data = jObj.getJSONObject("obj");
			JSONArray arrAll = data.getJSONArray("All");
			JSONArray arrFriend = data.getJSONArray("Friends");
			Ranking userRank = new Ranking();
			try {
				JSONObject userJson = data.getJSONObject("User");
				userRank.setUserName(userJson.getString("Name"));
				userRank.setUserID(userJson.getInt("UserId"));
				userRank.setLevel(userJson.getInt("LevelId"));
				userRank.setScore(userJson.getInt("Score"));
				userRank.setQuestion(userJson.getInt("Question"));
				userRank.setRank(userJson.getInt("RankFriends"),
						userJson.getInt("RankAll"));
			} catch (Exception e) {

			}
			List<Ranking> listAll = new ArrayList<Ranking>();
			List<Ranking> listFriends = new ArrayList<Ranking>();
			for (int i = 0; i < arrAll.length(); i++) {
				JSONObject o = arrAll.getJSONObject(i);
				String name = o.getString("Name");
				int userID = o.getInt("UserId");
				int score = o.getInt("Score");
				int level = o.getInt("LevelId");
				String fbID = o.getString("FbUserId");
				int question = o.getInt("Question");
				Ranking rank = new Ranking();
				rank.setLevel(level);
				rank.setFBID(fbID);
				rank.setUserID(userID);
				rank.setScore(score);
				rank.setUserName(name);
				rank.setQuestion(question);
				listAll.add(rank);
			}
			for (int i = 0; i < arrFriend.length(); i++) {
				JSONObject o = arrFriend.getJSONObject(i);
				String name = o.getString("Name");
				int userID = o.getInt("UserId");
				int score = o.getInt("Score");
				int level = o.getInt("LevelId");
				String fbID = o.getString("FbUserId");
				int question = o.getInt("Question");
				Ranking rank = new Ranking();
				rank.setLevel(level);
				rank.setUserID(userID);
				rank.setScore(score);
				rank.setUserName(name);
				rank.setFBID(fbID);
				rank.setQuestion(question);
				listFriends.add(rank);
			}
			Object[] datas = new Object[3];
			datas[0] = listAll;
			datas[1] = listFriends;
			datas[2] = userRank;
			DataHelper.saveUserID(userRank.getUserID());
			return result.setData(datas);
		} else
			result.setError(Error.SERVER_ERROR);
		return result;
	}

	private static Result parseNotify(String reader) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		JSONObject jObj = new JSONObject(reader);
		int code = jObj.getInt("code");
		if (code == 0) {
			JSONArray datas = jObj.getJSONArray("obj");
			Object[][] notifies = new Object[datas.length()][];
			for (int j = 0; j < datas.length(); j++) {
				JSONObject data = datas.getJSONObject(j);
				int type = data.getInt("Type");
				int id = data.getInt("Id");
				Object[] notify = null;
				switch (type) {
				case 0:
					// payment
					notify = new Object[3];
					notify[1] = type;
					JSONArray arr = data.getJSONArray("Content");
					Payment[] payment = new Payment[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						JSONObject idx = arr.getJSONObject(i);
						payment[i] = new Payment();
						payment[i].setCode(idx.getString("Code"));
						payment[i].setSyntax(idx.getString("Syntax"));
						if (i == 0) {
							payment[i].setType(SMSUtils.SMS_2K);
						} else if(i== 1){
							payment[i].setType(SMSUtils.SMS_5k);
						} else {
							payment[i].setType(SMSUtils.SMS_15k);
						}
					}
					notify[2] = payment;
					notify[0] = id;
					break;
				case 1:
					// message
					notify = new Object[3];
					notify[1] = type;
					notify[2] = data.getString("Content");
					notify[0] = id;
					break;
				case 2:
					// open web
					notify = new Object[4];
					notify[1] = type;
					notify[2] = data.getString("Link");
					notify[3] = data.getString("Content");
					notify[0] = id;
					break;
				}
				notifies[j] = notify;
			}
			result.setData(notifies);
		} else
			result.setError(Error.SERVER_ERROR);
		return result;
	}

	private static Result parseUpdate(String reader) throws Exception {
		Result result = new Result();
		JSONObject jObj = new JSONObject(reader);
		int code = jObj.getInt("code");
		LogUtils.e(Parser.class, "reader:" + reader);
		JSONObject config = jObj.getJSONObject("config");
		String phoneNumber = config.getString("PhoneCSKH");
		ResourceManager.saveString(ResourceManager.PHONE_CSKH, phoneNumber);
		if (code == 1) {
			JSONObject data = jObj.getJSONObject("obj");
			Object[] update = new Object[4];
			update[0] = data.getInt("VersionCode");
			update[1] = data.getString("VersionName");
			update[2] = data.getString("LinkDownload");
			update[3] = data.getString("Description");
			return result.setData(update);
		} else if (code == 2) {
			try {
				JSONArray data = jObj.getJSONObject("obj").getJSONArray("CrossSales");
				for (int i = 0; i < data.length(); i++) {
					JSONObject crossSale = data.getJSONObject(i);
					String title = crossSale.getString("Title");
					String coverImage = crossSale.getString("CoverImage");
					String linkDownload = crossSale.getString("LinkDownload");
					String description = crossSale.getString("Description");
					String packageName = crossSale.getString("PackageName");
					int coin = crossSale.getInt("Coin");
					CrossSaleManager.checkGameForPackageName(title, coverImage,
							description, packageName, linkDownload, coin);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			JSONArray data = jObj.getJSONObject("obj").getJSONArray("Broadcast");
			for (int i = 0; i < data.length(); i++) {
				JSONObject crossSale = data.getJSONObject(i);
				int s = crossSale.getInt("DelayTime");
				String text = crossSale.getString("Text");
				LogUtils.e(Parser.class, "text="+text+",delaytime="+s);
				BroadcaseMessage.addMessage(s, text);
			}
		} catch(Exception e){
			
		}
		return result.setData(0);
	}

}
