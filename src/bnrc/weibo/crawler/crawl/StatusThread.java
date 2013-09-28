/**
 * 
 */
package bnrc.weibo.crawler.crawl;

import java.util.ArrayList;
import java.util.List;
import bnrc.weibo.crawler.model.StatusBean;
import weibo4j.model.Status;
import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.util.WeiboInterImpl;

/**
 * @author songsiqi
 *
 */
public class StatusThread {
	
	WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
	
	// 爬取用户的微博信息
	public void crawl(String userId) {
		List<Status> statusList = weiboInterImpl.getStatusesByUserId(userId);
		if (statusList != null) {
			// 开启一个存储线程
			new SaveStatusesThread(statusList).start();
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
//			for (StatusBean s : statusBeanList) {
//				if (s.getContent().length() > 130) {
//					System.out.println(s.getStatusId());
//					System.out.println(s.getContent());
//					System.out.println(s.getSourceUrl());
//					System.out.println(s.getSourceName());
//					System.out.println(s.getInReplyToScreenName());
//					System.out.println(s.getThumbnailPic());
//					System.out.println(s.getBmiddlePic());
//					System.out.println(s.getOriginalPic());
//				}
//			}
		}
	}
	
}
