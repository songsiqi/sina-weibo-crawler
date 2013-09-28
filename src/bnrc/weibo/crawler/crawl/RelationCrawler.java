/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.UserIdBean;
import bnrc.weibo.crawler.model.UserRelationBean;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class RelationCrawler {
	WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户的社会化关系信息
	public void crawl(String userId) {
		// 爬粉丝
		String[] followersIds = weiboInterImpl.getFollowersIdsByUserId(userId);
		// 爬关注
		String[] followeesIds = weiboInterImpl.getFolloweesIdsByUserId(userId);
		
		//开启一个存储线程
		new SaveUserRelationThread(followersIds, followeesIds, userId).start();
		if ((followersIds != null && !followersIds[0].equals("")) || (followeesIds != null && !followeesIds[0].equals(""))) {
			// 开启一个存储线程
			new SaveUserRelationThread(followersIds, followeesIds, userId).start();
		}
	}
	
	// 存储用户的社会化关系信息
	class SaveUserRelationThread extends Thread {
		String[] followersIds;
		String[] followeesIds;
		String userId;
		List<UserRelationBean> userRelationBeanList = new ArrayList<UserRelationBean>();
		List<UserIdBean> userIdBeanList = new ArrayList<UserIdBean>();
		
		public SaveUserRelationThread(String[] followersIds, String[] followeesIds, String userId) {
			this.followeesIds = followeesIds;
			this.followersIds = followersIds;
			this.userId = userId;
		}
		
		public void run() {
			// 使用Set去重
			Set<String> followersIdSet = new HashSet<String>();
			Set<String> followeesIdSet = new HashSet<String>();
			Set<String> userIdSet = new HashSet<String>();
			
			for (String followersId : followersIds) {
				if (followersId.length() >= 4) {
					if (followersIdSet.add(followersId)) {
						userRelationBeanList.add(UserRelationBean.getUserRelationBean(followersId, userId));
					}
					if (userIdSet.add(followersId)) {
						userIdBeanList.add(UserIdBean.getUserIdBean(followersId));
					}
				}
			}
			for (String followeesId : followeesIds) {
				if (followeesId.length() >= 4) {
					if (followeesIdSet.add(followeesId)) {
						userRelationBeanList.add(UserRelationBean.getUserRelationBean(userId, followeesId));
					}
					if (userIdSet.add(followeesId)) {
						userIdBeanList.add(UserIdBean.getUserIdBean(followeesId));
					}
				}
			}
			DBOperation.insert2UserRelationTable(userRelationBeanList);
			DBOperation.insert2UsersTable(userIdBeanList);
		}
	}
	
	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		AccessToken.generate();
		AccessToken.setOneAccessToken();
		new RelationCrawler().crawl("1190573714");
	}
	
}
