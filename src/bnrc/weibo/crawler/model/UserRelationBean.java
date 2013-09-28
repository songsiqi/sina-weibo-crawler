package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserRelationBean {
	
	private long sourceUserId;					// 粉丝ID
	private long targetUserId;					// 关注人ID
	private int relationState;					// 好感度级别
	private int iteration;						// 爬虫访问次数
	private Timestamp updateTime;				// 该条记录的更新时间
	
	public long getSourceUserId() {
		return sourceUserId;
	}
	
	public void setSourceUserId(long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}
	
	public long getTargetUserId() {
		return targetUserId;
	}
	
	public void setTargetUserId(long targetUserId) {
		this.targetUserId = targetUserId;
	}
	
	public int getRelationState() {
		return relationState;
	}
	
	public void setRelationState(int relationState) {
		this.relationState = relationState;
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
	
	// 封装成UserRelationBean对象
	public static UserRelationBean getUserRelationBean(String sourceUserId, String targetUserId) {
		UserRelationBean userRelationBean = new UserRelationBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		userRelationBean.setSourceUserId(new BigInteger(sourceUserId).longValue());
		userRelationBean.setTargetUserId(new BigInteger(targetUserId).longValue());
		time = simpleDateFormat.format(Calendar.getInstance().getTime());
		userRelationBean.setUpdateTime(Timestamp.valueOf(time));
		userRelationBean.setIteration(0);
		
		// TODO: 判断关系是否存在
		userRelationBean.setRelationState(1);
		
		return userRelationBean;
	}
}
