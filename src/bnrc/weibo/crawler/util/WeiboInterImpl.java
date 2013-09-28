package bnrc.weibo.crawler.util;

import java.util.List;
import org.apache.log4j.Logger;

import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Tags;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.Tag;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class WeiboInterImpl implements WeiboInter {
	
	private Timeline timeline = new Timeline();
	private Users users = new Users();
	private Friendships friendships = new Friendships();
	private Tags tags = new Tags();
	private Comments comments = new Comments();
	
	private static final Logger logger = Logger.getLogger(WeiboInterImpl.class);
	
	@Override
	public User getUserById(String userId) {
		User user = null;
		
		try {
			user = users.showUserById(userId);
		} catch (Exception e) {
			logger.error("爬取用户信息过程中出错");
			if (e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getUserById(userId);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getUserById(userId);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getUserById(userId);
				} else if (errorCode == 20003) { // 用户不存在
					user = null;
				}
			} else {
				e.printStackTrace();
			}
		}
		
		return user;
	}

	@Override
	public String[] getFollowersIdsByUserId(String userId) {
		String[] followersIds = null;
		
		try {
			followersIds = friendships.getFollowersIdsById(userId, 5000, 0);
		} catch (Exception e) {
			logger.error("爬取用户粉丝过程中出错");
			if (e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getFollowersIdsByUserId(userId);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getFollowersIdsByUserId(userId);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getFollowersIdsByUserId(userId);
				} else if (errorCode == 20003) { // 用户不存在
					followersIds = null;
				}
			} else {
				e.printStackTrace();
			}
		}
		
		return followersIds;
	}

	@Override
	public String[] getFolloweesIdsByUserId(String userId) {
		String[] followeesIds = null;
		
		try {
			followeesIds = friendships.getFriendsIdsByUid(userId, 5000, 0);
		} catch (Exception e) {
			logger.error("爬取用户关注过程中出错");
			if (e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getFolloweesIdsByUserId(userId);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getFolloweesIdsByUserId(userId);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getFolloweesIdsByUserId(userId);
				} else if (errorCode == 20003) { // 用户不存在
					followeesIds = null;
				}
			} else {
				e.printStackTrace();
			}
		}
		
		return followeesIds;
	}

	@Override
	public List<Status> getStatusesByUserId(String userId, int intervalOfRequest, int pageLimit) {
		List<Status> statusList = null;
		
		try {
			Paging page = new Paging();
			page.setCount(100);
			int pageCount = 0;
			while (true) {
				pageCount++;
				logger.info("正在爬取微博第" + pageCount + "页");
				
				// 每10页变一次token
				if (pageCount % 10 == 0) {
					AccessToken.setOneAccessToken();
				}
				
				page.setPage(pageCount);
				if (statusList == null) {
					statusList = timeline.getUserTimelineByUid(userId, page, 0, 0).getStatuses();
				} else {
					List<Status> temp = timeline.getUserTimelineByUid(userId, page, 0, 0).getStatuses();
					if (temp == null || temp.size() == 0) {
						break;
					} else {
						statusList.addAll(temp);
					}	
				}
				
				// 爬取微博的页数限制
				if (pageCount == pageLimit) {
					break;
				}
				
				try {
					Thread.sleep(intervalOfRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		} catch (Exception e) {
			logger.error("爬取用户微博过程中出错");
			if(e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getStatusesByUserId(userId, intervalOfRequest, pageLimit);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getStatusesByUserId(userId, intervalOfRequest, pageLimit);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getStatusesByUserId(userId, intervalOfRequest, pageLimit);
				} else if (errorCode == 20003) { // 用户不存在
					statusList = null;
				}
			} else {
				e.printStackTrace();
			}
		}

		return statusList;
	}

	@Override
	public List<Comment> getCommentsByStatusId(String statusId) {
		List<Comment> commentList = null;
		
		try {
			commentList = comments.getCommentById(statusId).getComments();
		} catch (Exception e) {
			logger.error("爬取用户评论过程中出错");
			if (e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getCommentsByStatusId(statusId);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getCommentsByStatusId(statusId);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getCommentsByStatusId(statusId);
				} else if (errorCode == 20003) { // 用户不存在
					commentList = null;
				}
			} else {
				e.printStackTrace();
			}
		}
		
		return commentList;
	}

	@Override
	public List<Tag> getTagsByUserId(String userId) {
		List<Tag> tagList = null;
		
		try {
			tagList = tags.getTags(userId);
		} catch (Exception e) {
			logger.error("爬取用户标签过程中出错");
			if (e instanceof WeiboException) {
				int errorCode = ((WeiboException) e).getErrorCode();
				String errorInfo = ((WeiboException) e).getError();
				logger.error("Error Code: " + errorCode + " : " + errorInfo);
				
				if (errorCode == 10023) { // 用户请求频次超过上限
					logger.error("用户请求频次超过上限");
					AccessToken.setOneAccessToken();
					return getTagsByUserId(userId);
				} else if (errorCode == 10022) { // IP请求频次超过上限
					logger.error("IP请求频次超过上限");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getTagsByUserId(userId);
				} else if (errorCode == -1) { // 新浪微博API提供数据出错
					logger.error("新浪微博API提供数据出错");
					try {
						Thread.sleep(1000 * 60 * 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return getTagsByUserId(userId);
				} else if (errorCode == 20003) { // 用户不存在
					tagList = null;
				}
			} else {
				e.printStackTrace();
			}
		}
		
		return tagList;
	}
	
}
