/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import weibo4j.Weibo;
import weibo4j.model.User;
import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.UserBean;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class UserCrawler {
	private WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户个人信息，返回用户的微博数
	// 若用户不存在则返回-1
	public int crawl(String userId) {
		User user = weiboInterImpl.getUserById(userId);
		if (user != null) {
			// 开启一个存储线程
			new SaveUserThread(user).start();
			
			int statusesCount = user.getStatusesCount();
			return statusesCount;
		} else { // 用户不存在
			return -1;
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
	
	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		AccessToken.generate();
		AccessToken.setOneAccessToken();
		//Weibo weibo = new Weibo();
		//weibo.setToken("2.00n6e6qBIySZSD28ff03b4a7gQVi9B");
		new UserCrawler().crawl("1447104391");
	}
	
}
