package tools;

import java.util.List;

public interface OnlineServices {
	public boolean LogIn();
	public boolean LogOff();
		
	public ListenerManager lm=new ListenerManager();
	
	//LeaderBoards
	public boolean postScore(String leaderBoard,long score);
	public void getScores(String leaderBoard,int count,ScoresReceivedCallBack callback);
	public void getUserScores(String leaderBoard,int count,ScoresReceivedCallBack callback);
	public void getScore(String leaderBoard,ScoreReceivedCallBack callback);
	public boolean openLeaderBoard(String leaderBoard);
	public boolean openAllLederBoards();
	
	//Achivements
	public void getPercent(String achievement,AchievementPercentCallBack callback);
	public boolean unlockAchivement(String achievement);
	public void isAchivementUnlocked(String achivement,IsAchievementUnlockedCallBack callback);
	public boolean openAllAchivements();
	public void getAllAchievements(AchievementListCallBack callback);
	public void incrementAchievement(String ach,int step);
	public void setPercent(String ach, int per);
	
	//Data
	public void getData(String domain,DataReceivedCallBack drcb);
	public void saveData(String domain,Object data);
	
	public static interface DataReceivedCallBack{
		public void received(Object data);
		public void failed(String error);
	}
	public static interface AchievementPercentCallBack{
		public void received(float percent);
	}
	public static interface AchievementListCallBack{
		public void received(List<Achievement> achievements);
	}
	public static interface IsAchievementUnlockedCallBack{
		public void received(boolean unlocked);
	}
	public static interface ScoresReceivedCallBack{
		public void received(List<Score> scores);
	}
	public static interface ScoreReceivedCallBack{
		public void received(Score scores);
	}
	public static class Achievement{
		private String title,description;
		private boolean isUnlocked;
		public Achievement(String title,String description, boolean isUnlocked) {
			super();
			this.description=description;
			this.title = title;
			this.isUnlocked = isUnlocked;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public boolean isUnlocked() {
			return isUnlocked;
		}
		public void setUnlocked(boolean isUnlocked) {
			this.isUnlocked = isUnlocked;
		}
	}
	public static class Score {
		private String user;
		private long scoreLong;
		private float scoreFloat;
		private String leaderBoard;
		private String rank;
		public Score(String user, long scoreLong,String leaderBoard,String rank) {
			super();
			this.user = user;
			this.scoreLong = scoreLong;
			this.leaderBoard = leaderBoard;
			this.rank=rank;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public long getScoreLong() {
			return scoreLong;
		}
		public void setScoreLong(long scoreLong) {
			this.scoreLong = scoreLong;
		}
		public float getScoreFloat() {
			return scoreFloat;
		}
		public void setScoreFloat(float scoreFloat) {
			this.scoreFloat = scoreFloat;
		}
		public String getLeaderBoard() {
			return leaderBoard;
		}
		public void setLeaderBoard(String leaderBoard) {
			this.leaderBoard = leaderBoard;
		}
	}

}
