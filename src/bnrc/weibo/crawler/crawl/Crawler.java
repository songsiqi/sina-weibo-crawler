/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.UserIdBean;
import bnrc.weibo.crawler.util.AccessToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author songsiqi
 *
 */
public class Crawler {
	
	private static UserThread userThread = new UserThread();
	private static StatusThread statusThread = new StatusThread();
	private static RelationThread relationThread = new RelationThread();
	private static TagThread tagThread = new TagThread();
	
	private static final Logger logger = Logger.getLogger(Crawler.class);
	
	// 上次刷新token的时间
	private static Date lastRefreshTokenTime = null;
	
	// 两次刷新token之间的时间间隔
	private static final int interval = 1000 * 60 * 60 * 23;
	
	// 以用户为中心的整个爬取流程
	private static void crawling() {
		// 获取待爬用户
		String userId = "0";
		long uId = 0;
		do {
			uId = DBOperation.getNewUserId();
			userId = String.valueOf(uId);
		} while (userId.equals("0"));
		
		//String userId = "1190573714";
		//long uId = 1190573714;
		
		logger.info("★★★★★正在爬取用户" + userId + "★★★★★");
		
		// 爬取并存储用户个人信息
		logger.info("正在爬取用户" + userId + "的个人信息");
		AccessToken.getOneAccessToken();
		boolean exist = userThread.crawl(userId);
		
		// 如果用户存在，则继续爬其他内容
		if (exist) {
			try {
				Thread.sleep(5 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 爬取并存储用户社会化关系信息
			logger.info("正在爬取用户" + userId + "的社会化关系信息");
			AccessToken.getOneAccessToken();
			relationThread.crawl(userId);
			try {
				Thread.sleep(5 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 爬取并存储用户微博信息
			logger.info("正在爬取用户" + userId + "的微博信息");
			AccessToken.getOneAccessToken();
			statusThread.crawl(userId);
			try {
				Thread.sleep(5 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 爬取并存储用户标签信息
			logger.info("正在爬取用户" + userId + "的标签信息");
			AccessToken.getOneAccessToken();
			tagThread.crawl(userId);
			try {
				Thread.sleep(5 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		DBOperation.endNewUserId(uId, exist);
		
		// 检测是否需要刷新token
		Date currentTime = Calendar.getInstance().getTime();
		if (currentTime.getTime() - lastRefreshTokenTime.getTime() > interval) {
			// 刷新token
			AccessToken.generate();
			lastRefreshTokenTime = Calendar.getInstance().getTime();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// 种子用户，初始化数据库
		String[] seedUserIds = {"2007962507", "2322151934"}; // 2007962507, 2322151934, 1197161814
		for (String uId : seedUserIds) {
			UserIdBean userIdBean = UserIdBean.getUserIdBean(uId);
			List<UserIdBean> userIdBeanList = new ArrayList<UserIdBean>();
			userIdBeanList.add(userIdBean);
			DBOperation.insert2UsersTable(userIdBeanList);
		}
		
		// 刷新token
		AccessToken.generate();
		lastRefreshTokenTime = Calendar.getInstance().getTime();
		
		while (true) {
			crawling();
		}
		
	}
	
}
