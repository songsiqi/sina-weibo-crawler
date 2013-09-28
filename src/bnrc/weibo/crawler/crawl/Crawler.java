/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.UserIdBean;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.RevisitConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author songsiqi
 *
 */
public class Crawler {
	
	private static UserCrawler userCrawler = new UserCrawler();
	private static StatusCrawler statusCrawler = new StatusCrawler();
	private static RelationCrawler relationCrawler = new RelationCrawler();
	private static TagCrawler tagCrawler = new TagCrawler();
	
	private static final Logger logger = Logger.getLogger(Crawler.class);
	
	// 刷新token相关
	private static Date lastRefreshTokenTime = null;			// 上次刷新token的时间
	private static final int intervalOfRefreshToken = 1000 * 60 * 60 * 23;	// 两次刷新token之间的时间间隔
	
	// 请求控制相关
	private static final int intervalOfRequest = 1000 * 5;		// 每次请求之间的间隔
	private static final int maxStatusesPage = 300;				// 最多方位微博的页数
	
	// 重访相关
	private static float revisitRatio = 0.5f;					// 重访所占的比例
	private static float minStatusesFrequency = 0.01f;			// 用户最小的发微博频率
	private static int minDaysSinceLastVisit = 5;				// 最小重访时间间隔
	private static Date lastRefreshRevisitConfigTime = null;	// 上次刷新token的时间
	private static final int intervalOfRefreshRevisitConfig = 1000 * 60 * 60 * 1;	// 两次刷新重访配置之间的时间间隔
	private static final int iterationInterval = 3;				// 重访次数为3的倍数时才重访用户的社会化关系和标签
	
	// 决定一次爬取是否为重访
	public static boolean isRevisit() {
		int radix = 100;
		Random rand = new Random();
		int randInt = rand.nextInt(radix) + 1;
		if (randInt <= radix * revisitRatio) {
			return true;
		}
		return false;
	}
	
	// 以用户为中心的整个爬取流程
	private static void crawling() {
		
		// 爬取用户微博信息的最多页数，默认值为300，在重访的时候会调整这个变量的值
		int statusPageLimit = maxStatusesPage;
		
		String userId = "0";
		long uId = 0;
		int previousStatusesCount = 0;
		int iteration = 0;
		
		// 决定本地爬取是否为重访
		boolean revisit = isRevisit();

		// 本次爬取新用户
		if (!revisit) {
			// 获取待爬新用户
			do {
				uId = DBOperation.getNewUserId();
				userId = String.valueOf(uId);
			} while (userId.equals("0"));
			
			logger.info("★★★★★正在爬取新用户" + userId + "★★★★★");
		}
		
		// 本次重访已经爬取过的用户
		else {
			// 时间转换，获取需要重访的时间线
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -minDaysSinceLastVisit);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String maxNeedUpdateTime = simpleDateFormat.format(cal.getTime());
			
			// 获取待重访用户
			Map<String, Object> userMap = DBOperation.getRevisitUser(minStatusesFrequency, maxNeedUpdateTime);
			uId = (Long) userMap.get("user_id");
			previousStatusesCount = (Integer) userMap.get("statuses_count"); // 数据库中用户的微博数
			iteration = (Integer) userMap.get("iteration"); // 已经访问的次数
			userId = String.valueOf(uId);
			
			// 如果没有符合要求的待重访用户，则改为爬取新用户
			if (userId.equals("0")) {
				// 获取待爬新用户
				do {
					uId = DBOperation.getNewUserId();
					userId = String.valueOf(uId);
				} while (userId.equals("0"));
				revisit = false;
				logger.info("★★★★★正在爬取新用户" + userId + "★★★★★");
			} else {
				logger.info("☆☆☆☆☆正在重访用户" + userId + "，已访问" + iteration + "次☆☆☆☆☆");
			}
		}

		// 爬取并存储用户个人信息
		logger.info("正在爬取用户" + userId + "的个人信息");
		AccessToken.setOneAccessToken();
		int currentStatusesCount = userCrawler.crawl(userId); // 用户最新的微博数
		boolean exist = currentStatusesCount > -1;
		
		// 如果用户存在，则继续爬其他内容
		if (exist) {
			try {
				Thread.sleep(intervalOfRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 爬取并存储用户社会化关系信息
			if (iteration % iterationInterval == 0) {
				logger.info("正在爬取用户" + userId + "的社会化关系信息");
				AccessToken.setOneAccessToken();
				relationCrawler.crawl(userId);
				
				try {
					Thread.sleep(intervalOfRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 爬取并存储用户微博信息
			logger.info("原微博数：" + previousStatusesCount + "，现微博数：" + currentStatusesCount);
			if (currentStatusesCount > previousStatusesCount) {
				statusPageLimit = (currentStatusesCount - previousStatusesCount) / 100 + 1;
				if (statusPageLimit > maxStatusesPage) { // 最多300页
					statusPageLimit = maxStatusesPage;
				}
				logger.info("正在爬取用户" + userId + "的微博信息");
				AccessToken.setOneAccessToken();
				statusCrawler.crawl(userId, intervalOfRequest, statusPageLimit);
				
				try {
					Thread.sleep(intervalOfRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 爬取并存储用户标签信息
			if (iteration % iterationInterval == 0) {
				logger.info("正在爬取用户" + userId + "的标签信息");
				AccessToken.setOneAccessToken();
				tagCrawler.crawl(userId);
			}
		}
			
		DBOperation.endVisitForUserId(uId, exist);
		
		try {
			Thread.sleep(intervalOfRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date currentTime = Calendar.getInstance().getTime();
		
		// 检测是否需要刷新token，以及更新重访参数
		if (currentTime.getTime() - lastRefreshTokenTime.getTime() > intervalOfRefreshToken) {
			AccessToken.generate();
			lastRefreshTokenTime = currentTime;
		}
		
		// 检测是否需要更新重访参数
		if (currentTime.getTime() - lastRefreshRevisitConfigTime.getTime() > intervalOfRefreshRevisitConfig) {
			RevisitConfig revisitConfig = new RevisitConfig();
			Map<String, Object> configMap = revisitConfig.getConfig();
			revisitRatio = (Float) configMap.get("revisit_ratio");
			minStatusesFrequency = (Float) configMap.get("min_statuses_frequency");
			minDaysSinceLastVisit = (Integer) configMap.get("min_days_since_last_visit");
			lastRefreshRevisitConfigTime = currentTime;
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
		
		Date currentTime = Calendar.getInstance().getTime();
		
		// 生成token
		AccessToken.generate();
		lastRefreshTokenTime = currentTime;
		
		// 重访参数设定
		RevisitConfig revisitConfig = new RevisitConfig();
		Map<String, Object> configMap = revisitConfig.getConfig();
		revisitRatio = (Float) configMap.get("revisit_ratio");
		minStatusesFrequency = (Float) configMap.get("min_statuses_frequency");
		minDaysSinceLastVisit = (Integer) configMap.get("min_days_since_last_visit");
		lastRefreshRevisitConfigTime = currentTime;
		
		while (true) {
			crawling();
		}
	}
	
}
