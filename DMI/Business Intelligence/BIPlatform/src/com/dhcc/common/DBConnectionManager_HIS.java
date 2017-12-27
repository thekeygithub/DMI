package com.dhcc.common;

import java.sql.*;
import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.*;
import com.dhcc.common.util.SystemConfig;



/**
 * 
 * 
 * 数据库建立管理实现类
 * 
 * @author itims
 *
 */

public class DBConnectionManager_HIS {
	
	private static final Logger logger = Logger.getLogger(DBConnectionManager_HIS.class);
	private static ComboPooledDataSource cpds = null;

	private static void init() {
		
		if(cpds==null)
		{
		// 建立数据库连接池
		String DRIVER_NAME = "oracle.jdbc.OracleDriver"; // 驱动器
		String DATABASE_URL = "jdbc:oracle:thin:@//20.20.1.107:1521/ora7"; // 数据库连接url
		String DATABASE_USER = "mobileapp"; // 数据库用户名
		String DATABASE_PASSWORD = "mobile12app"; // 数据库密码
		
		int Min_PoolSize = 2;
		int Max_PoolSize = 3;
		int Acquire_Increment = 10;
		int Initial_PoolSize = 2;
		int Idle_Test_Period = 3000;// 每隔3000s测试连接是否可以正常使用
		int MAX_IdleTime=60;
		int Timeout=10000;//
		int CheckoutTimeout=3000;
		int NumHelperThreads=3;
		
		
		String Validate = SystemConfig.getConfigInfomation("Validate");// 每次连接验证连接是否可用
		if (Validate.equals("")) {
			Validate = "false";
		}
		// 最小连接数
		try {
			Min_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("Min_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 增量条数
		try {
			Acquire_Increment = Integer.parseInt(SystemConfig
					.getConfigInfomation("Acquire_Increment"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 最大连接数
		try {
			Max_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("Max_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 初始化连接数
		try {
			Initial_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("Initial_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 每隔3000s测试连接是否可以正常使用
		try {
			Idle_Test_Period = Integer.parseInt(SystemConfig
					.getConfigInfomation("Idle_Test_Period"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		// 
		try {
			MAX_IdleTime = Integer.parseInt(SystemConfig
					.getConfigInfomation("MAX_IdleTime"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			CheckoutTimeout = Integer.parseInt(SystemConfig
					.getConfigInfomation("CheckoutTimeout"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			NumHelperThreads = Integer.parseInt(SystemConfig
					.getConfigInfomation("NumHelperThreads"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(DRIVER_NAME); // 驱动器
			cpds.setJdbcUrl(DATABASE_URL); // 数据库url
			cpds.setUser(DATABASE_USER); // 用户名
			cpds.setPassword(DATABASE_PASSWORD); // 密码
			cpds.setInitialPoolSize(Initial_PoolSize); // 初始化连接池大小
			cpds.setMinPoolSize(Min_PoolSize); // 最少连接数
			cpds.setMaxPoolSize(Max_PoolSize); // 最大连接数
			cpds.setAcquireIncrement(Acquire_Increment); // 连接数的增量
			cpds.setIdleConnectionTestPeriod(Idle_Test_Period); //测连接有效的时间间隔
			cpds.setTestConnectionOnCheckout(Boolean.getBoolean(Validate)); // 每次连接验证连接是否可用
			cpds.setMaxIdleTime(MAX_IdleTime);
			cpds.setCheckoutTimeout(CheckoutTimeout);
			cpds.setAutoCommitOnClose(true);
			cpds.setNumHelperThreads(NumHelperThreads);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		}
		
	}

	/**
	 * 获取数据连接池
	 * @return
	 */
    public static synchronized Connection getConnection() {    
        Connection con = null;    
        try {  
        	if (cpds == null) {
				init();
			}
            con = cpds.getConnection();   
            System.out.println("DBConnectionManager_HIS******最大连接数:"+cpds.getMaxPoolSize()
			+"最小连接数:"+cpds.getMinPoolSize()
			+"正在使用连接数:"+cpds.getNumBusyConnections()
			+"空闲连接数:"+cpds.getNumIdleConnections()
			+"总连接数:"+cpds.getNumConnections());
        } catch (SQLException e1) {    
            e1.printStackTrace();    
        }    
        return con;    
    }  
	
	/**
	 * 
	 * 关闭数据库连接（关闭数据源）
	 * 
	 */
	private static void release() {
		try {
			if (cpds != null) {
				cpds.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String arg[]) throws SQLException
	{
		
		DBConnectionManager_HIS db=new DBConnectionManager_HIS();
        db.getConnection();
        logger.info("获取数据库的链接。。。。");
        String SQL="SELECT * FROM view_xzqhdm WHERE xzqhdm in (SELECT xzqhdm FROM view_grjbxx_yl GROUP BY xzqhdm)";
        Connection con = null;    
        con= db.getConnection();
        //PreparedStatement pstmt = null;    
        //pstmt = con.prepareStatement(sql);    
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery(SQL);//数据库查询
        boolean flag=false;
        while(rs.next()) {    
            // 如果有记录，则执行此段代码    
            // 用户是合法的，可以登陆    
            flag = true;    
            logger.info(rs.getString("MC"));
            
           }    
        db.release();
        logger.info("运行成功==="+flag);
	}
}
