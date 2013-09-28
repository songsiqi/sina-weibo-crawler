package bnrc.weibo.crawler.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UserIdBean {

	private long userId;					// 用户UID
	private int iteration;					// 爬虫访问次数
	private Timestamp updateTime;			// 该条记录的更新时间

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	public static UserIdBean getUserIdBean(String userId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		UserIdBean userIdBean = new UserIdBean();
		userIdBean.setUserId(Long.valueOf(userId));
		userIdBean.setIteration(0);
		Date currentTime = Calendar.getInstance().getTime();
    	time = simpleDateFormat.format(currentTime);
    	userIdBean.setUpdateTime(Timestamp.valueOf(time));
    	
		return userIdBean;
	}

}
