/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import java.util.ArrayList;
import java.util.List;
import bnrc.weibo.crawler.model.StatusBean;
import weibo4j.model.Status;
import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class StatusCrawler {
	
	private WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户的微博信息
//	public void crawl(String userId, int intervalOfRequest, int pageLimit) {
//		List<Status> statusList = weiboInterImpl.getStatusesByUserId(userId, intervalOfRequest, pageLimit);
//		if (statusList != null) {
//			// 开启一个存储线程
//			new SaveStatusesThread(statusList).start();
//		}
//	}
	
	// 爬取用户的微博信息
	public void crawl(String userId, int intervalOfRequest, int pageLimit) {
		int pagesPerTime = 30;
		int start = 0;
		int end = 0;
		
		// 每爬30页，就存储一次
		while (true) {
			start = end + 1;
			end += pagesPerTime;
			if (end > pageLimit) {
				end = pageLimit;
			}
			
			List<Status> statusList = weiboInterImpl.getStatusesByUserId(userId, intervalOfRequest, start, end);
			if (statusList != null) {
				// 开启一个存储线程
				new SaveStatusesThread(statusList).start();
			}
			
			if (end == pageLimit) {
				break;
			}
		}
	}
	
	// 存储用户的微博信息
	class SaveStatusesThread extends Thread {
		List<Status> statusList;
		List<StatusBean> statusBeanList = new ArrayList<StatusBean>();
		
		public SaveStatusesThread(List<Status> statusList) {
			this.statusList = statusList;
		}
		
		public void run() {
			for (Status status : statusList) {
				statusBeanList.add(StatusBean.getStatusBean(status));
			}
			DBOperation.insert2StatusesTable(statusBeanList);
		}
	}
	
	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		AccessToken.generate();
		AccessToken.setOneAccessToken();
		new StatusCrawler().crawl("1190573714", 5 * 1000, 100);
	}
	
}
