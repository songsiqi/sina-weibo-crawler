package bnrc.weibo.crawler.crawl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Emotion;
import weibo4j.model.Status;
import weibo4j.model.User;
import bnrc.weibo.crawler.database.DBOperation;
import bnrc.weibo.crawler.model.StatusBean;
import bnrc.weibo.crawler.util.AccessToken;
import bnrc.weibo.crawler.util.WeiboInterImpl;

public class CrawlerTest {
	private static Weibo weibo = new Weibo();
	
	private static void getEmotions() {
		Timeline timeline = new Timeline();
		List<Emotion> emotions;
		try {
			emotions = timeline.getEmotions("face", "cnname");
			
			for (Emotion emo : emotions) {
				System.out.println(emo.getPhrase());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// Only for test
	public static void main(String[] args) {
		//AccessToken.generate();
		//AccessToken.getOneAccessToken();
//		weibo.setToken("2.00YFUEID0rPfuG150897b47arVwEEB");
//		WeiboInterImpl weiboInterImpl = new WeiboInterImpl();
//		
//		List<Status> statusList = weiboInterImpl.getStatusesByUserId("1985011167", 300);
//		List<StatusBean> statusBeanList = new ArrayList<StatusBean>();
//		for (Status status : statusList) {
//			statusBeanList.add(StatusBean.getStatusBean(status));
//		}
//		DBOperation.insert2StatusesTable(statusBeanList);
//		
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		for (Status status : statusList) {
//			System.out.println(status.getText());
//			System.out.println(simpleDateFormat.format(status.getCreatedAt()));
//		}
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -5);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simpleDateFormat.format(cal.getTime());
		System.out.println(time);
	}
}
