/**
 * 
 */
package bnrc.weibo.crawler.util;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author songsiqi
 *
 */
public class RevisitConfig {
	
	private static ResourceBundle resourceBundle; // 资源束对象
	
	public Map<String, Object> getConfig() {
		Map<String, Object> configMap = new HashMap<String, Object>();
		
		// 从配置文件中读取
		try {
			resourceBundle = ResourceBundle.getBundle("revisit");
			configMap.put("revisit_ratio", Float.valueOf(resourceBundle.getString("revisit_ratio")));
			configMap.put("min_statuses_frequency", Float.valueOf(resourceBundle.getString("min_statuses_frequency")));
			configMap.put("min_days_since_last_visit", Integer.valueOf(resourceBundle.getString("min_days_since_last_visit")));
			ResourceBundle.clearCache();	// 刷新缓存，避免每次的值都一样
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		
		return configMap;
	}
	
	/**
	 * Only for test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int i = 4;
		do {
			RevisitConfig revisitConfig = new RevisitConfig();
			Map<String, Object> configMap = revisitConfig.getConfig();
			float revisitRatio = (Float) configMap.get("revisit_ratio");
			float minStatusesFrequency = (Float) configMap.get("min_statuses_frequency");
			int minDaysSinceLastVisit = (Integer) configMap.get("min_days_since_last_visit");
			System.out.println(String.format("%f %f %d", revisitRatio, minStatusesFrequency, minDaysSinceLastVisit));
			
			try {
				Thread.sleep(1000 * 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (--i > 0);
	}

}
