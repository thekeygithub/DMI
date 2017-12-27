package com.dhcc.common.database;

import java.sql.SQLException;

import com.dhcc.modal.system.Tsuser;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author tiger
 * @创建日期 2010-8-24 上午01:05:06
 * @功能 实现数据库连接，提供数据库访问常用方法
 * 
 */
public class Test {
	public static void main(String[] args) {
		while(true){
			try {
				DBManager dbm = new DBManager();
				String sql = "select * from tsuser where loginname=  'admin' and userpass='96e79218965eb72c92a549dd5a330112'";
				Tsuser tsuser = (Tsuser) dbm.getObject(Tsuser.class, sql);
				System.out.println("************************"+tsuser.getUsername()+"            "+tsuser.getId());
				dbm.close();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
}
