package com.aghit.task.socket;


public interface IService {

	
	 /**
     * 服务初始化
     * @param param
     * @return
     * @throws MQException 
     */
    public boolean init(Object param) throws Exception;
    /**
     * 服务启动
     * @throws Exception
     */
    public void start() throws Exception;
    /**
     * 服务中止
     * @throws Exception
     */
    public void stop() throws Exception;
    
}
