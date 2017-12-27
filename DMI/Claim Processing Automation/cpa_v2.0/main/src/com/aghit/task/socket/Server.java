package com.aghit.task.socket;

import com.aghit.task.common.service.impl.CacheServiceImpl;
import com.aghit.task.manager.BreakDetailMgr;

public class Server {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		CacheServiceImpl cach=new CacheServiceImpl();
		BreakDetailMgr de=new BreakDetailMgr();
		cach.setJdbcService(de.getJdbcService());
		
		cach.initCache();
		
		IService service =DealService.getInstance();
		service.init(null);
		service.start();

	}
	
	public void run(){
		
	}
	
	

}
