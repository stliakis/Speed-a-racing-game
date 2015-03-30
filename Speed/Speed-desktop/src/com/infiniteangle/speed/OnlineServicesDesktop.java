package com.infiniteangle.speed;

import java.util.ArrayList;
import java.util.List;

import tools.OnlineServices;

public class OnlineServicesDesktop implements OnlineServices{
	public OnlineServicesDesktop() {
		
	}
	@Override
	public boolean postScore(String leaderBoard, long score) {
		// TODO Auto-generated method stub
		System.out.println("leaderboar:"+leaderBoard+"   "+"score:"+score);
		return false;
	}

	@Override
	public void getScores(String leaderBoard, int count,
			ScoresReceivedCallBack callback) {
		// TODO Auto-generated method stub
		List<Score> scores=new ArrayList<Score>();
		callback.received(scores);
	}

	@Override
	public void getScore(String leaderBoard, ScoreReceivedCallBack callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean openLeaderBoard(String leaderBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openAllLederBoards() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getPercent(String achievement, AchievementPercentCallBack callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean unlockAchivement(String achievement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void isAchivementUnlocked(String achivement,
			IsAchievementUnlockedCallBack callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean openAllAchivements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getUserScores(String leaderBoard, int count,
			ScoresReceivedCallBack callback) {
	}
	@Override
	public boolean LogIn() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean LogOff() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void getAllAchievements(AchievementListCallBack callback) {
		// TODO Auto-generated method stub
		List<Achievement> achi=new ArrayList<OnlineServices.Achievement>();
		achi.add(new Achievement("dsgdsg1 14 4 ds ggds sdg","asdsD",false));
		achi.add(new Achievement("sfdsgsd fsd sd ffds fsdg","asdsD",false));
		achi.add(new Achievement("erfsdsdsd fsd sd fs dsdf","asdsD",false));
		achi.add(new Achievement("sdfdsf sdf sdfs fsdffsdsdf","asdsD",false));
		achi.add(new Achievement("asdasdf sdf sf sdf sd ssdd","asdsD",false));
		achi.add(new Achievement("asdssdfsdfsdf sfd sfds ffsdaa","asdsD",false));
		achi.add(new Achievement("sdfsdf sd fds sdfsd fdssdfsdf","asdsD",false));
		achi.add(new Achievement("assd fsd fs sd  fsdf  dasd","asdsD",false));
		achi.add(new Achievement("a sdf sdf sf  sdsadasdasd","asdsD",false));
		achi.add(new Achievement("assd f sfsdfsdfs sfsd fsf d","asdsD",false));
		callback.received(achi);
	}
	@Override
	public void getData(String domain, DataReceivedCallBack drcb) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveData(String domain, Object data) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void incrementAchievement(String ach, int step) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPercent(String ach, int per) {
		// TODO Auto-generated method stub
		
	}
}
