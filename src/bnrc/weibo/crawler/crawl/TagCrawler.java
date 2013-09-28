/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import java.util.ArrayList;
import java.util.List;

import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.TagBean;
import bnrc.weibo.crawler.model.UserTagBean;

import weibo4j.model.Tag;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class TagCrawler {
	
	private WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户的标签信息
	public void crawl(String userId) {
		List<Tag> tagList = weiboInterImpl.getTagsByUserId(userId);
		if (tagList != null) {
			new SaveTagThread(tagList).start();
			new SaveUserTagThread(tagList, userId).start();
		}
	}
	
	// 存储用户的标签信息
	class SaveUserTagThread extends Thread {
		List<UserTagBean> userTagBeanList = new ArrayList<UserTagBean>();
		List<Tag> tagList;
		String userId;
		
		public SaveUserTagThread(List<Tag> tagList, String userId) {
			this.tagList = tagList;
			this.userId = userId;
		}
		
		public void run() {
			for (Tag tag : tagList) {
				userTagBeanList.add(UserTagBean.getUserTagBean(tag, userId));
			}
			DBOperation.insert2UserTagTable(userTagBeanList);
		}
	}
	
	// 存储标签信息
	class SaveTagThread extends Thread {
		List<TagBean> tagBeanList = new ArrayList<TagBean>();
		List<Tag> tagList;
		
		public SaveTagThread(List<Tag> tagList) {
			this.tagList = tagList;
		}
		
		public void run() {
			for (Tag tag : tagList) {
				tagBeanList.add(TagBean.getTagBean(tag));
			}
			DBOperation.insert2TagsTable(tagBeanList);
		}
	}
	
	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		AccessToken.generate();
		AccessToken.setOneAccessToken();
		new TagCrawler().crawl("1190573714");
	}
	
}
