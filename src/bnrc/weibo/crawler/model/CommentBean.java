package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weibo4j.model.Comment;

public class CommentBean {
	
	private long commentId;					// 评论id
	private String content;					// 评论内容
	private String sourceUrl;				// 评论来源的url
	private String sourceName;				// 评论来源的名字
	private Timestamp createdAt;			// 评论时间
	private long userId;					// 评论者id
	private long statusId;					// 所评论的微博的id
	private long mid;						// 评论mid
	private long replyCommentId;			// 回复的评论ID
	private int iteration;					// 爬虫访问次数
	private Timestamp updateTime;			// 该条记录的更新时间
	
	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public long getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(long replyCommentId) {
		this.replyCommentId = replyCommentId;
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

	// 将Comment对象转换成为CommentBean对象
	public static CommentBean getCommentBean(Comment comment) {
		CommentBean commentBean = new CommentBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		commentBean.setCommentId(comment.getId());
		commentBean.setContent(comment.getText());
		String source = comment.getSource();
		Pattern p1 = Pattern.compile("(http://.*)\" ");
		Matcher m1 = p1.matcher(source);
		if (m1.find()) {
			commentBean.setSourceUrl(m1.group(1));
		}
		Pattern p2 = Pattern.compile(">(.*)</a>");
		Matcher m2 = p2.matcher(source);
		if (m2.find()) {
			commentBean.setSourceName(m2.group(1));
		}
		time = simpleDateFormat.format(comment.getCreatedAt());
		commentBean.setCreatedAt(Timestamp.valueOf(time));
		if (comment.getUser() != null) {
			commentBean.setUserId(new BigInteger(comment.getUser().getId()).longValue());
		}
		if (comment.getStatus() != null) {
			commentBean.setStatusId(new BigInteger(comment.getStatus().getId()).longValue());
		}
		if (comment.getReplycomment() != null) {
			commentBean.setReplyCommentId(comment.getReplycomment().getId());
		}
		commentBean.setMid(new BigInteger(comment.getMid()).longValue());
		time = simpleDateFormat.format(Calendar.getInstance().getTime());
		commentBean.setUpdateTime(Timestamp.valueOf(time));
		commentBean.setIteration(0);
    	
    	return commentBean;
	}
}
