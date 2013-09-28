/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import weibo4j.model.User;
import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.UserBean;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class UserThread {
	WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户个人信息
	public boolean crawl(String userId) {
		User user = weiboInterImpl.getUserById(userId);
		if (user != null) {
			// 开启一个存储线程
			new SaveUserThread(user).start();
			return true;
		} else { // 用户不存在
			return false;
		}
	}
	
	// 存储用户个人信息
	class SaveUserThread extends Thread {
		User user;
		public SaveUserThread(User user) {
			this.user = user;
		}
		public void run() {
			DBOperation.updateUsersTable(UserBean.getUserBean(user));
		}
	}
}
