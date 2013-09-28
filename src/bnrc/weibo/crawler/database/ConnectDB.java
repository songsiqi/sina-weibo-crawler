package bnrc.weibo.crawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * 
 * 维护单个连接，创建和关闭连接
 *
 */
public class ConnectDB {
	
	//总的连接数目
	public static int connNum = 0;
	
	private static ResourceBundle resourceBundle; // 资源束对象
	
	//SQL数据库引擎  
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//数据源
	private static String connectDB;
    //用户名
	private static String userName;
    //密码
	private static String password;
	
	private Connection conn = null;
    
    //创建连接
    public Connection getConnection() {
    	try {
    		// 从配置文件中读取
    		try {
    			resourceBundle = ResourceBundle.getBundle("database");
    		} catch (MissingResourceException e) {
    			e.printStackTrace();
    		}
    		String ip = resourceBundle.getString("ip");
    		String port = resourceBundle.getString("port");
    		String dbname = resourceBundle.getString("dbname");
    		userName = resourceBundle.getString("account");
    		password = resourceBundle.getString("password");
    		connectDB = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbname;
    		
    		// 创建连接
    		Class.forName(driver);
    		conn = DriverManager.getConnection(connectDB, userName, password);
    		connNum++;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return conn;
    }
    
    //关闭连接
    public static void closeConnection(Connection connection)
    {
    	try
    	{
    		if(connection != null && !connection.isClosed())
    		{
				connNum--;
				connection.close();
    		}
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    }
}
