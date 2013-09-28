package bnrc.weibo.crawler.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;
import bnrc.weibo.crawler.model.*;


/**
 * 数据库操作类
 * @author songsiqi
 *
 */
public class DBOperation {
	
	private static final Logger logger = Logger.getLogger(DBOperation.class);
	
	// ----- users表 -----
	
	// 将用户id插入到users表中，批量插入
	public static void insert2UsersTable(List<UserIdBean> userIdBeanList) {
		PreparedStatement pstmt = null;
		ConnectManager cm = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			//sql = "if not exists (select * from users where user_id=?) insert into users(user_id,iteration,update_time) values(?,?,?);";
			
			sql = "begin tran;";
			sql += "update users set visited=visited where user_id=?;";
			sql += "if @@rowcount = 0 ";
			sql += "insert into users(user_id,iteration,update_time) values(?,?,?);";
			sql += "commit tran;";
			
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (UserIdBean userIdBean : userIdBeanList) {
				pstmt.setLong(1, userIdBean.getUserId());
				pstmt.setLong(2, userIdBean.getUserId());
				pstmt.setInt(3, userIdBean.getIteration());
				pstmt.setTimestamp(4, userIdBean.getUpdateTime());
				pstmt.addBatch();
			}
			
	    	pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入用户id错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2UsersTable(userIdBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 更新users表中的用户信息
	public static void updateUsersTable(UserBean userBean) {
		PreparedStatement pstmt = null;
		ConnectManager cm = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			sql = "update users set screen_name=?,name=?,province=?,city=?,location=?,description=?,url=?,profile_image_url=?,domain=?,gender=?,followers_count=?,friends_count=?,statuses_count=?,favourites_count=?,created_at=?,following=?,verified=?,verified_type=?,allow_all_act_msg=?,allow_all_comment=?,follow_me=?,avatar_large=?,online_status=?,bi_followers_count=?,remark=?,lang=?,verified_reason=?,weihao=?,geo_enabled=?,statuses_frequency=?,update_time=? where user_id=?;";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			pstmt.setString(1, userBean.getScreenName());
			pstmt.setString(2, userBean.getName());
			pstmt.setString(3, userBean.getProvince());
			pstmt.setString(4, userBean.getCity());
			pstmt.setString(5, userBean.getLocation());
			pstmt.setString(6, userBean.getDescription());
			pstmt.setString(7, userBean.getUrl());
			pstmt.setString(8, userBean.getProfileImageUrl());
			pstmt.setString(9, userBean.getUserDomain());
			pstmt.setString(10, userBean.getGender());
			pstmt.setInt(11, userBean.getFollowersCount());
			pstmt.setInt(12, userBean.getFriendsCount());
			pstmt.setInt(13, userBean.getStatusesCount());
			pstmt.setInt(14, userBean.getFavouritesCount());
			pstmt.setTimestamp(15, userBean.getCreatedAt());
			pstmt.setBoolean(16, userBean.isFollowing());
			pstmt.setBoolean(17, userBean.isVerified());
			pstmt.setInt(18, userBean.getVerifiedType());
			pstmt.setBoolean(19, userBean.isAllowAllActMsg());
			pstmt.setBoolean(20, userBean.isAllowAllComment());
			pstmt.setBoolean(21, userBean.isFollowMe());
			pstmt.setString(22, userBean.getAvatarLarge());
			pstmt.setInt(23, userBean.getOnlineStatus());
			pstmt.setInt(24, userBean.getBiFollowersCount());
			pstmt.setString(25, userBean.getRemark());
			pstmt.setString(26, userBean.getLang());
			pstmt.setString(27, userBean.getVerifiedReason());
			pstmt.setString(28, userBean.getWeihao());
			pstmt.setBoolean(29, userBean.isGeoEnabled());
			pstmt.setDouble(30, userBean.getStatusesFrequency());
			pstmt.setTimestamp(31, userBean.getUpdateTime());
			pstmt.setLong(32, userBean.getUserId());
	    	
	    	pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新用户信息错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			updateUsersTable(userBean);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- user_relation表 -----
	
	// 将用户社会化关系数据插入到user_relation表中，批量插入
	public static void insert2UserRelationTable(List<UserRelationBean> userRelationBeanList) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			//sql = "if not exists (select * from user_relation where source_user_id=? and target_user_id=?) insert into user_relation(source_user_id,target_user_id,relation_state,iteration,update_time) values(?,?,?,?,?);";
			
			sql = "begin tran;";
			sql += "update user_relation set iteration=iteration where source_user_id=? and target_user_id=?;";
			sql += "if @@rowcount = 0 ";
			sql += "insert into user_relation(source_user_id,target_user_id,relation_state,iteration,update_time) values(?,?,?,?,?);";
			sql += "commit tran;";
			
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (UserRelationBean userRelationBean : userRelationBeanList) {
				pstmt.setLong(1, userRelationBean.getSourceUserId());
				pstmt.setLong(2, userRelationBean.getTargetUserId());
				pstmt.setLong(3, userRelationBean.getSourceUserId());
				pstmt.setLong(4, userRelationBean.getTargetUserId());
				pstmt.setInt(5, userRelationBean.getRelationState());
				pstmt.setInt(6, userRelationBean.getIteration());
				pstmt.setTimestamp(7, userRelationBean.getUpdateTime());
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入用户用户社会化关系错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2UserRelationTable(userRelationBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- statuses表 -----
	
	// 插入数据到statuses表，批量插入
	public static void insert2StatusesTable(List<StatusBean> statusBeanList) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while(cm == null);
			
			sql = "begin tran;";
			sql += "update statuses set user_id=?,created_at=?,mid=?,content=?,source_url=?,source_name=?,favorited=?,truncated=?,in_reply_to_status_id=?,in_reply_to_user_id=?,in_reply_to_screen_name=?,thumbnail_pic=?,bmiddle_pic=?,original_pic=?,retweeted_status_id=?,geo_type=?,geo_coordinates_x=?,geo_coordinates_y=?,reposts_count=?,comments_count=?,mlevel=?,iteration=?,update_time=? where status_id=?;";
			sql += "if @@rowcount = 0 ";
			sql += "insert into statuses(status_id,user_id,created_at,mid,content,source_url,source_name,favorited,truncated,in_reply_to_status_id,in_reply_to_user_id,in_reply_to_screen_name,thumbnail_pic,bmiddle_pic,original_pic,retweeted_status_id,geo_type,geo_coordinates_x,geo_coordinates_y,reposts_count,comments_count,mlevel,iteration,update_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			sql += "commit tran;";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (StatusBean statusBean : statusBeanList) {
				pstmt.setLong(1, statusBean.getUserId());
				pstmt.setTimestamp(2, statusBean.getCreatedAt());
				pstmt.setLong(3, statusBean.getMid());
				pstmt.setString(4, statusBean.getContent());
				pstmt.setString(5, statusBean.getSourceUrl());
				pstmt.setString(6, statusBean.getSourceName());
				pstmt.setBoolean(7, statusBean.isFavorited());
				pstmt.setBoolean(8, statusBean.isTruncated());
				pstmt.setLong(9, statusBean.getInReplyToStatusId());
				pstmt.setLong(10, statusBean.getInReplyToUserId());
				pstmt.setString(11, statusBean.getInReplyToScreenName());
				pstmt.setString(12, statusBean.getThumbnailPic());
				pstmt.setString(13, statusBean.getBmiddlePic());
				pstmt.setString(14, statusBean.getOriginalPic());
				pstmt.setLong(15, statusBean.getRetweetedStatusId());
				pstmt.setString(16, statusBean.getGeoType());
				pstmt.setDouble(17, statusBean.getGeoCoordinatesX());
				pstmt.setDouble(18, statusBean.getGeoCoordinatesY());
				pstmt.setInt(19, statusBean.getRepostsCount());
				pstmt.setInt(20, statusBean.getCommentsCount());
				pstmt.setInt(21, statusBean.getMlevel());
				pstmt.setInt(22, statusBean.getIteration());
				pstmt.setTimestamp(23, statusBean.getUpdateTime());
				pstmt.setLong(24, statusBean.getStatusId());
				
				pstmt.setLong(25, statusBean.getStatusId());
				pstmt.setLong(26, statusBean.getUserId());
				pstmt.setTimestamp(27, statusBean.getCreatedAt());
				pstmt.setLong(28, statusBean.getMid());
				pstmt.setString(29, statusBean.getContent());
				pstmt.setString(30, statusBean.getSourceUrl());
				pstmt.setString(31, statusBean.getSourceName());
				pstmt.setBoolean(32, statusBean.isFavorited());
				pstmt.setBoolean(33, statusBean.isTruncated());
				pstmt.setLong(34, statusBean.getInReplyToStatusId());
				pstmt.setLong(35, statusBean.getInReplyToUserId());
				pstmt.setString(36, statusBean.getInReplyToScreenName());
				pstmt.setString(37, statusBean.getThumbnailPic());
				pstmt.setString(38, statusBean.getBmiddlePic());
				pstmt.setString(39, statusBean.getOriginalPic());
				pstmt.setLong(40, statusBean.getRetweetedStatusId());
				pstmt.setString(41, statusBean.getGeoType());
				pstmt.setDouble(42, statusBean.getGeoCoordinatesX());
				pstmt.setDouble(43, statusBean.getGeoCoordinatesY());
				pstmt.setInt(44, statusBean.getRepostsCount());
				pstmt.setInt(45, statusBean.getCommentsCount());
				pstmt.setInt(46, statusBean.getMlevel());
				pstmt.setInt(47, statusBean.getIteration());
				pstmt.setTimestamp(48, statusBean.getUpdateTime());
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入用户微博错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2StatusesTable(statusBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 更新statuses表中的微博信息
	public static void updateStatusesTable(StatusBean statusBean) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while(cm == null);
			
			sql = "update statuses set user_id=?,created_at=?,mid=?,content=?,source_url=?,source_name=?,favorite=?,truncated=?,in_replay_to_status_id=?,in_replay_to_user_id=?,in_replay_to_screen_name=?,thumbnail_pic=?,bmiddle_pic=?,original_pic=?,retweeted_status_id=?,geo_type=?,geo_coordinates_x=?,geo_coordinates_y=?,reposts_count=?,comments_count=?,mlevel=?,iteration=?,update_time=? where status_id=?;";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			pstmt.setLong(1, statusBean.getUserId());
			pstmt.setTimestamp(2, statusBean.getCreatedAt());
			pstmt.setLong(3, statusBean.getMid());
			pstmt.setString(4, statusBean.getContent());
			pstmt.setString(5, statusBean.getSourceUrl());
			pstmt.setString(6, statusBean.getSourceName());
			pstmt.setBoolean(7, statusBean.isFavorited());
			pstmt.setBoolean(8, statusBean.isTruncated());
			pstmt.setLong(9, statusBean.getInReplyToStatusId());
			pstmt.setLong(10, statusBean.getInReplyToUserId());
			pstmt.setString(11, statusBean.getInReplyToScreenName());
			pstmt.setString(12, statusBean.getThumbnailPic());
			pstmt.setString(13, statusBean.getBmiddlePic());
			pstmt.setString(14, statusBean.getOriginalPic());
			pstmt.setLong(15, statusBean.getRetweetedStatusId());
			pstmt.setString(16, statusBean.getGeoType());
			pstmt.setDouble(17, statusBean.getGeoCoordinatesX());
			pstmt.setDouble(18, statusBean.getGeoCoordinatesY());
			pstmt.setInt(19, statusBean.getRepostsCount());
			pstmt.setInt(20, statusBean.getCommentsCount());
			pstmt.setInt(21, statusBean.getMlevel());
			pstmt.setInt(22, statusBean.getIteration());
			pstmt.setTimestamp(23, statusBean.getUpdateTime());
			pstmt.setLong(24, statusBean.getStatusId());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新用户微博错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			updateStatusesTable(statusBean);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- comments表 -----
	
	// 将评论数据插入到comments表中，批量插入
	public static void insert2CommentsTable(List<CommentBean> commentBeanList) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while(cm == null);
			
			sql = "insert into comments(comment_id,mid,content,source_url,source_name,created_at,user_id,status_id,reply_comment_id,iteration,update_time) values(?,?,?,?,?,?,?,?,?,?,?);";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (CommentBean commentBean : commentBeanList) {
				pstmt.setLong(1, commentBean.getCommentId());
				pstmt.setLong(2, commentBean.getMid());
				pstmt.setString(3, commentBean.getContent());
				pstmt.setString(4, commentBean.getSourceUrl());
				pstmt.setString(5, commentBean.getSourceName());
				pstmt.setTimestamp(6, commentBean.getCreatedAt());
				pstmt.setLong(7, commentBean.getUserId());
				pstmt.setLong(8, commentBean.getStatusId());
				pstmt.setLong(9, commentBean.getReplyCommentId());
				pstmt.setInt(10, commentBean.getIteration());
				pstmt.setTimestamp(11, commentBean.getUpdateTime());
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入微博评论错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2CommentsTable(commentBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- user_tag表 -----
	
	// 将数用户标签据插入到user_tag表中，批量插入
	public static void insert2UserTagTable(List<UserTagBean> userTagBeanList) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			sql = "if not exists (select * from user_tag where user_id=? and tag_id=?) insert into user_tag(user_id,tag_id,iteration,update_time) values(?,?,?,?);";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (UserTagBean userTagBean : userTagBeanList) {
				pstmt.setLong(1, userTagBean.getUserId());
				pstmt.setLong(2, userTagBean.getTagId());
				pstmt.setLong(3, userTagBean.getUserId());
				pstmt.setLong(4, userTagBean.getTagId());
				pstmt.setInt(5, userTagBean.getIteration());
				pstmt.setTimestamp(6, userTagBean.getUpdateTime());
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入用户标签错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2UserTagTable(userTagBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- tags表 -----
	
	//将数据插入到tags表中，批量插入
	public static void insert2TagsTable(List<TagBean> tagBeanList) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			sql = "begin tran;";
			sql += "update tags set tag=?,weight=?,iteration=?,update_time=? where tag_id=?;";
			sql += "if @@rowcount = 0 ";
			sql += "insert into tags(tag_id,tag,weight,iteration,update_time) values(?,?,?,?,?);";
			sql += "commit tran;";
			pstmt = cm.getConnection().prepareStatement(sql);
			
			for (TagBean tagBean : tagBeanList) {
				pstmt.setString(1, tagBean.getTagName());
				pstmt.setInt(2, tagBean.getWeight());
				pstmt.setInt(3, tagBean.getIteration());
				pstmt.setTimestamp(4, tagBean.getUpdateTime());
				pstmt.setLong(5, tagBean.getTagId());
				
				pstmt.setLong(6, tagBean.getTagId());
				pstmt.setString(7, tagBean.getTagName());
				pstmt.setInt(8, tagBean.getWeight());
				pstmt.setInt(9, tagBean.getIteration());
				pstmt.setTimestamp(10, tagBean.getUpdateTime());
				
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入标签信息错误，重新插入！");
			
			// 写入数据库失败，重新调用函数
			insert2TagsTable(tagBeanList);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----- 获取待爬用户访问控制 -----
	
	// 获取待爬用户id，并设置数据库标记
	public static long getNewUserId() {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		long userId = 0;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			sql = "select top 1 user_id from users where iteration=0 and visited=0 order by update_time asc;";
			pstmt = cm.getConnection().prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				userId = rs.getLong(1);
			}
			if (userId != 0) {
				sql = "update users set visited=1 where user_id=?;";
				pstmt = cm.getConnection().prepareStatement(sql);
				pstmt.setLong(1, userId);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取待爬用户错误！");
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return userId;
	}
	
	// 爬取完成，设置数据库标记
	public static void endNewUserId(long userId, boolean exist) {
		ConnectManager cm = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			do {
				cm = ConnectPool.getConnectPool().getConnectManager();
			} while (cm == null);
			
			if (exist) {
				sql = "update users set visited=0,iteration=iteration+1 where user_id=?;";
			} else {
				sql = "update users set visited=2 where user_id=?;";
			}
			pstmt = cm.getConnection().prepareStatement(sql);
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("爬取完成设置用户标记错误！");
			
			endNewUserId(userId, exist);
		} finally {
			// 关闭会话，释放连接
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				cm.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
