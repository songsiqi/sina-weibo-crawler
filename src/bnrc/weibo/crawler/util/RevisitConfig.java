/**
 * 
 */
package bnrc.weibo.crawler.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author songsiqi
 *
 */
public class RevisitConfig {
	
	private static ResourceBundle resourceBundle; // 资源束对象
	
	public static void refresh() {
		// 从配置文件中读取
		try {
			resourceBundle = ResourceBundle.getBundle("revisit");
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String prop) {
		return resourceBundle.getString(prop);
	}

	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		refresh();
		System.out.println(Float.valueOf(RevisitConfig.getProperty("revisit_ratio")));
		System.out.println(Float.valueOf(RevisitConfig.getProperty("min_statuses_frequency")));
		System.out.println(Integer.valueOf(RevisitConfig.getProperty("min_days_since_last_visit")));
	}

}
