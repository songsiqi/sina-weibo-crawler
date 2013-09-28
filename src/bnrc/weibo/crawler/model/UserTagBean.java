package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import weibo4j.model.Tag;

public class UserTagBean {
	
	private long userId;						// 用户ID
	private long tagId;							// 标签ID
	private int iteration;						// 爬虫访问次数
	private Timestamp updateTime;				// 该条记录的更新时间
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	// 将Tag对象和userId封装成为UserTagBean对象
	public static UserTagBean getUserTagBean(Tag tag, String userId) {
		UserTagBean userTagBean = new UserTagBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		userTagBean.setUserId(new BigInteger(userId).longValue());
		userTagBean.setTagId(new BigInteger(tag.getId()).longValue());
		
		time = simpleDateFormat.format(Calendar.getInstance().getTime());
		userTagBean.setUpdateTime(Timestamp.valueOf(time));
		
		userTagBean.setIteration(0);
		
		return userTagBean;
	}
	
}
