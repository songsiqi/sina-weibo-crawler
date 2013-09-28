package bnrc.weibo.crawler.util;

import java.util.List;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.Tag;
import weibo4j.model.User;

public interface WeiboInter {
	//根据用户id获取用户信息
	public User getUserById(String userId);
	
	//根据用户id获取用户的粉丝id列表（新浪微博api限制为5000）
	public String[] getFollowersIdsByUserId(String userId);
	
	//根据用户id获取用户的关注id列表（新浪微博api限制为5000）
	public String[] getFolloweesIdsByUserId(String userId);
	
	//根据用户id获取用户的微博列表
	public List<Status> getStatusesByUserId(String userId, int intervalOfRequest, int pageLimit);
	
	//根据微博id获取微博的评论列表（可提取评论用户的id）
	public List<Comment> getCommentsByStatusId(String statusId);
	
	//根据用户id获取用户的标签列表
	public List<Tag> getTagsByUserId(String userId);
	
}
