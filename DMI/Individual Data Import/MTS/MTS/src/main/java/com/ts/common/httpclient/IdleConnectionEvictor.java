package com.ts.common.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;  

/** 
 * 定期清理无效的http连接 
 */  
public class IdleConnectionEvictor extends Thread {  
  
    private final HttpClientConnectionManager connMgr;  
      
    private Integer waitTime;  
  
    private volatile boolean shutdown;  
  
    public IdleConnectionEvictor(HttpClientConnectionManager connMgr,Integer waitTime) {  
        this.connMgr = connMgr;  
        this.waitTime = waitTime;  
        this.start();  
    }  
  
    @Override  
    public void run() {  
        try {  
            while (!shutdown) {  
                synchronized (this) {  
                    wait(waitTime);  
                    // 关闭失效的连接  
                    connMgr.closeExpiredConnections();  
                }  
            }  
        } catch (InterruptedException ex) {  
            // 结束  
        }  
    }  
  
    /** 
     * 销毁释放资源 
     */  
    public void shutdown() {  
        shutdown = true;  
        synchronized (this) {  
            notifyAll();  
        }  
    }  
}  