package bnrc.weibo.crawler.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
/**
 * 连接池
 * @author lht
 *
 */
public class ConnectPool
{
	//池最大值的大小
	private int poolMaxSize;
	//池稳定的大小
	private int poolSteadySize;
	//池初始的大小
	private int poolInitSize;
	//可用池
    public  List<Connection> validPooledConnList = new ArrayList<Connection>();
    //管理池
    public  List<Connection> allPooledConnList = new ArrayList<Connection>();   
    //单例模式
    private static ConnectPool connectPool = null;
    
    //私有构造方法
    private ConnectPool()
    {
        poolMaxSize = 50;
        poolSteadySize = 30;
        poolInitSize = 20;
    	initPool();
    }
    
    //得到连接池的单例
    public static ConnectPool getConnectPool()
    {
    	if(connectPool == null)
    	{
    		connectPool = new ConnectPool();
		}
    	return connectPool;
    }

    /**
     * @author lht
     * 初始化数据库连接池，以初始化数量向连接池中加入数据库实际连接
     * ，同时启动一个用于维护数据库连接池的连接的线程。
     */
    private void initPool()
    {
    	ConnCheckThread cct = new ConnCheckThread();
    	for(int i = 0; i < poolInitSize; i++)
    	{
			addOneConnToPool();
		}
    	cct.start();
    }
    
    /**
     * @author lht
     * 向连接池中加入一个连接
     * @return boolean
     */
    private boolean addOneConnToPool()
    {
    	boolean b = true;
    	Connection conn = new ConnectDB().getConnection();
    	
    	if(conn != null)
    	{
			try
			{
				validPooledConnList.add(conn);
				allPooledConnList.add(conn);
				
			}
			catch (Exception e)
			{
				b = false;
				ConnectDB.closeConnection(conn);
				e.printStackTrace();
			}
		}
    	
    	return b;
	}
    
    /**
     * @author lht
     * 移除连接池中的一个连接，同时关闭其物理连接.
     */
    private void removeOneConnFromPool()
    {
    	Connection conn = validPooledConnList.remove(0);
    	allPooledConnList.remove(conn);
    	ConnectDB.closeConnection(conn);
    }
    
    /**
     * @author lht
     * 维护数据库连接池稳定时的连接数量
     */
    public synchronized void packPool()
    {
    	while(validPooledConnList.size() > poolSteadySize)
    	{
    		removeOneConnFromPool();
    	}
    }
    
    /**
     * @author lht
     * 将释放的连接放入到连接池中
     * @param conn
     */
    public synchronized void releasePooledConn(Connection conn)
    {
    	System.out.println("释放缓冲连接：" + conn);
    	if(allPooledConnList.contains(conn) && !validPooledConnList.contains(conn))
    	{
    		validPooledConnList.add(conn);
		}
    	else
    	{
    		ConnectDB.closeConnection(conn);
		}
    }
    
    /**
     * @author lht
     * 获得一个ConnectManager对象，这个对象用于与客户端交互。
     * @return ConnectManager
     */
    public synchronized ConnectManager getConnectManager()
    {
    	ConnectManager pconn = null;
    	
    	if(validPooledConnList.isEmpty())
    	{
			if(allPooledConnList.size() >= poolMaxSize)
			{
				return null;
			}
			else
			{
				if(addOneConnToPool())
				{
					pconn = new ConnectManager(validPooledConnList.remove(0));
				}
				else
				{
					return null;
				}
			}
		}
    	else
    	{
			pconn = new ConnectManager(validPooledConnList.remove(0));
		}
    	
    	if(pconn != null)
    	{
			System.out.println("获取缓冲池连接" + pconn);
		}
    	
    	return pconn;
    }
    
    /**
     * 
     * @author lht
     * 维护数据库连接稳定的线程类
     *
     */
	class ConnCheckThread extends Thread
	{

		@Override
		public void run()
		{
			packPool();
		}
    }
	
	/**
	 * @author lht
     * 用于释放池中所有连接
	 */
	public void releaseAllPooledConn()
	{
		for(Connection conn : allPooledConnList)
		{
			ConnectDB.closeConnection(conn);
		}
	}
}