package bnrc.weibo.crawler.database;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * 管理用户连接
 * @author lht
 *
 */
public class ConnectManager
{
	private Connection connection;
	public static int poolConnNums = 0;
	protected Timestamp setConnTime;
	
	/**
	 * @author lht
	 * 构造方法，初始化这个类
	 * @param conn
	 */
	public ConnectManager(Connection conn)
	{
		setConnection(conn);
		poolConnNums++;
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void release()
	{
		ConnectPool.getConnectPool().releasePooledConn(this.connection);
		poolConnNums--;
	}
	
	protected void setConnection(Connection connection)
	{
		this.connection = connection;
		setConnTime = new Timestamp(System.currentTimeMillis());
	}
}