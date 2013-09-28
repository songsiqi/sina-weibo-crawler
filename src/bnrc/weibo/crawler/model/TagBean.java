package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import weibo4j.model.Tag;

public class TagBean {
	
	private long tagId;							// 标签ID
	private String tagName;						// 标签具体名字
	private int weight;							// 标签权重
	private int iteration;						// 爬虫访问次数
	private Timestamp updateTime;				// 该条记录的更新时间
	
	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
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

	// 将Tag对象封装成为TagBean
	public static TagBean getTagBean(Tag tag) {
		TagBean tagBean = new TagBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		tagBean.setTagId(new BigInteger(tag.getId()).longValue());
		tagBean.setTagName(tag.getValue());
		tagBean.setWeight(new Integer(tag.getWeight()));
		
		time = simpleDateFormat.format(Calendar.getInstance().getTime());
		tagBean.setUpdateTime(Timestamp.valueOf(time));
		tagBean.setIteration(0);
		
		return tagBean;
	}
	
}
