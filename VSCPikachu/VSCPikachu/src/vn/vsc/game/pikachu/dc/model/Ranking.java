package vn.vsc.game.pikachu.dc.model;

public class Ranking {
	private String userName;
	private int userID;
	private int score;
	private int level;
	private int[] ranks;
	
	private int question;
	public int getQuestion(){
		return question;
	}
	public void setQuestion(int question){
		this.question = question;
	}
	public void setRank(int... ranks){
		this.ranks = ranks;
	}
	public int getRankingAll(){
		try{
			return ranks[1];
		} catch(Exception e){
			return 1;
		}
	}
	public int getRankingFriend(){
		try{
			return ranks[0];
		} catch(Exception e){
			return 1;
		}
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level+1;
	}
	private String userFBID;
	public void setFBID(String id){
		this.userFBID = id;
	}
	public String getFBID() {
		// TODO Auto-generated method stub
		return userFBID;
	}
	
}
