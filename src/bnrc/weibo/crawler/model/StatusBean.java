package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weibo4j.model.Status;

public class StatusBean {
	
	private long statusId;						// 微博ID
	private long userId;						// 用户ID
	private Timestamp createdAt;				// status创建时间
	private long mid;							// 微博MID
	private String content;						// 微博内容
	private String sourceUrl;					// 来源链接
	private String sourceName;					// 来源文案名字
	private boolean favorited;					// 是否已收藏
	private boolean truncated;					// 是否被截断
	private long inReplyToStatusId;				// 回复的微博ID
	private long inReplyToUserId;				// 回复的用户ID
	private String inReplyToScreenName;			// 回复的用户昵称
	private String thumbnailPic;				// 微博内容中的图片的缩略地址
	private String bmiddlePic;					// 中型图片
	private String originalPic;					// 原始图片
	private long retweetedStatusId;				// 转发的博文，内容为微博ID，如果不是转发，则此字段为0
	private String geoType;						// 地理位置类型
	private double geoCoordinatesX;				// 纬度
	private double geoCoordinatesY;				// 经度
	private int repostsCount;					// 转发数
	private int commentsCount;					// 评论数
	private int mlevel;							// （暂未支持）
	private int iteration;						// 爬虫访问次数
	private Timestamp updateTime;				// 该条记录的更新时间
	
	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public long getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public String getThumbnailPic() {
		return thumbnailPic;
	}

	public void setThumbnailPic(String thumbnailPic) {
		this.thumbnailPic = thumbnailPic;
	}

	public String getBmiddlePic() {
		return bmiddlePic;
	}

	public void setBmiddlePic(String bmiddlePic) {
		this.bmiddlePic = bmiddlePic;
	}

	public String getOriginalPic() {
		return originalPic;
	}

	public void setOriginalPic(String originalPic) {
		this.originalPic = originalPic;
	}

	public long getRetweetedStatusId() {
		return retweetedStatusId;
	}

	public void setRetweetedStatusId(long retweetedStatusId) {
		this.retweetedStatusId = retweetedStatusId;
	}

	public String getGeoType() {
		return geoType;
	}

	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}

	public double getGeoCoordinatesX() {
		return geoCoordinatesX;
	}

	public void setGeoCoordinatesX(double geoCoordinatesX) {
		this.geoCoordinatesX = geoCoordinatesX;
	}

	public double getGeoCoordinatesY() {
		return geoCoordinatesY;
	}

	public void setGeoCoordinatesY(double geoCoordinatesY) {
		this.geoCoordinatesY = geoCoordinatesY;
	}

	public int getRepostsCount() {
		return repostsCount;
	}

	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
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

	// 将Status对象转换成为StatusBean对象
	public static StatusBean getStatusBean(Status status) {
		StatusBean statusBean = new StatusBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		statusBean.setStatusId(new BigInteger(status.getId()).longValue());
		time = simpleDateFormat.format(status.getCreatedAt());
		statusBean.setCreatedAt(Timestamp.valueOf(time));
		String content = status.getText();
		if (content.length() > 200) {	// 若微博过长，则截断
			content = content.substring(0, 200);
		}
		statusBean.setContent(content);
		if (status.getSource() != null)	{
			statusBean.setSourceUrl(status.getSource().getUrl());
			statusBean.setSourceName(status.getSource().getName());
		} else {
			statusBean.setSourceUrl("");
			statusBean.setSourceName("");
		}			
		statusBean.setFavorited(status.isFavorited());
		statusBean.setTruncated(status.isTruncated());
		if (status.getGeo() != null) {	// 抽取地理位置类型
			String geo = status.getGeo();
			String strTypePattern = "\\{\"type\":\"(.+?)\"";
			Pattern typePattern = Pattern.compile(strTypePattern, Pattern.CASE_INSENSITIVE);
			Matcher typeMatcher = typePattern.matcher(geo);
			if (typeMatcher.find()) {
				statusBean.setGeoType(typeMatcher.group(1));
			} else {
				statusBean.setGeoType("");
			}
			
		} else {
			statusBean.setGeoType("");
		}
		statusBean.setGeoCoordinatesX(status.getLatitude());
		statusBean.setGeoCoordinatesY(status.getLongitude());
		statusBean.setInReplyToStatusId(status.getInReplyToStatusId());
		statusBean.setInReplyToUserId(status.getInReplyToUserId());
		statusBean.setInReplyToScreenName(status.getInReplyToScreenName());
		statusBean.setThumbnailPic(status.getThumbnailPic());
		statusBean.setBmiddlePic(status.getBmiddlePic());
		statusBean.setOriginalPic(status.getOriginalPic());
		statusBean.setMid(new BigInteger(status.getMid()).longValue());
		statusBean.setRepostsCount(status.getRepostsCount());
		statusBean.setCommentsCount(status.getCommentsCount());
		statusBean.setMlevel(status.getMlevel());
		if (status.getUser() != null) {
			statusBean.setUserId(new BigInteger(status.getUser().getId()).longValue());
		} else {
			statusBean.setUserId(0);
		}		
		if(status.getRetweetedStatus() != null) {
			statusBean.setRetweetedStatusId(new BigInteger(status.getRetweetedStatus().getId()).longValue());
		} else {
			statusBean.setRetweetedStatusId(0);
		}
		time = simpleDateFormat.format(Calendar.getInstance().getTime());
		statusBean.setUpdateTime(Timestamp.valueOf(time));
		statusBean.setIteration(0);
		
		return statusBean;
	}
	
//	public static void main(String [] args) {
//		String geo = "{\"type\":\"Point\",\"coordinates\":[28.20753,113.009582]}";
//		String strTypePattern = "\\{\"type\":\"(.+?)\"";
//		Pattern typePattern = Pattern.compile(strTypePattern, Pattern.CASE_INSENSITIVE);
//		Matcher typeMatcher = typePattern.matcher(geo);
//		if (typeMatcher.find()) {
//			System.out.println(typeMatcher.group(1));
//		}
//	}
}
