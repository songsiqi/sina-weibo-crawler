package bnrc.weibo.crawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import weibo4j.Oauth;
import weibo4j.Weibo;
import weibo4j.util.WeiboConfig;


public class AccessToken {
	
	private static String clientId;			// appKey
	private static String clientSecret;		// appSecret
	private static List<AccountBean> accountInfo = new ArrayList<AccountBean>();	// 用户信息数组
	private static List<String> accessToken = new ArrayList<String>();				// 每个用户对应的access_token
	private static Weibo weibo = new Weibo();
	private static final Logger logger = Logger.getLogger(AccessToken.class);
	
	// 初始化用户信息
	private static void initAccountInfo() {
		accountInfo.clear();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("userinfo.txt"))));
			String str = null;
			while ((str = br.readLine()) != null)  {
				String[] userinfo = str.split(" ");
				AccountBean account = new AccountBean();
				account.setUserId(userinfo[0]);
				account.setPasswd(userinfo[1]);
				accountInfo.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 获取一个access_token供爬虫使用
	public static void getOneAccessToken() {
		// 随机提供
		Random rand = new Random();
		int len = accessToken.size();
		int index = Math.abs(rand.nextInt() % len);
		weibo.setToken(accessToken.get(index));
	}
	
	// 授权，生成access_token
	// 使用情形：①程序初始化；②每隔一天左右重新授权access_token
	public static void generate() {
		initAccountInfo();
		accessToken.clear();
		
		logger.info("用户授权中...");
		try {
			// https://api.weibo.com/oauth2/authorize?client_id=750123511&redirect_uri=https://api.weibo.com/oauth2/default.html&response_type=code
			String url = "https://api.weibo.com/oauth2/authorize";
			String redirectUri = "https://api.weibo.com/oauth2/default.html";
			
			for (int i = 0; i < accountInfo.size(); i++) {
				// 获取应用的信息
				clientId = WeiboConfig.getValue("client_ID");
				clientSecret = WeiboConfig.getValue("client_SERCRET");
				
				// 构造授权的url参数
				PostMethod postMethod = new PostMethod(url);
				postMethod.addParameter("client_id", clientId);
				postMethod.addParameter("redirect_uri", redirectUri);
				postMethod.addParameter("userId", accountInfo.get(i).getUserId());
				postMethod.addParameter("passwd", accountInfo.get(i).getPasswd());
				postMethod.addParameter("isLoginSina", "0");
	            postMethod.addParameter("action", "submit");
				postMethod.addParameter("response_type", "code");
				HttpMethodParams param = postMethod.getParams();
				param.setContentCharset("UTF-8");
				
				// 伪造头部域信息
				List<Header> headers = new ArrayList<Header>();
				headers.add(new Header("Referer", "https://api.weibo.com/oauth2/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&from=sina&response_type=code"));
				headers.add(new Header("Host", "api.weibo.com"));
				headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0"));
				
				// 发送HTTP请求
				HttpClient client = new HttpClient();
				client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
				client.executeMethod(postMethod);
				
				// 获取授权响应
				int status = postMethod.getStatusCode();
				if (status == 302) {
					Header location = postMethod.getResponseHeader("location");
					if (location != null) {
						String retUrl = location.getValue();
						int begin = retUrl.indexOf("code=");
						int end = retUrl.length();
						String code = retUrl.substring(begin + 5, end);
						if (code != null) {
							Oauth oauth = new Oauth();
							String token = oauth.getAccessTokenByCode(code).getAccessToken();
							accessToken.add(token);
							logger.info("第" + (i + 1) + "个access_token：" + token);
						}
					}
				}
				else {
					logger.error("第" + (i + 1) + "个用户授权失败了！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("授权发生异常！");
		}
	}
	
}
