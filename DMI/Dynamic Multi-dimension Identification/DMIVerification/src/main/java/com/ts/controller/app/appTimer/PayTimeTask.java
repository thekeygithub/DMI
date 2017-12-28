package com.ts.controller.app.appTimer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.base.BaseController;
import com.ts.entity.P_drugstore_inte_info;
import com.ts.entity.P_inte_info;
import com.ts.service.pay.PayManager;
import com.ts.util.PageData;
import com.ts.util.PayLinkedQueue;
import com.ts.util.PayLinkedQueue_Drugstore;

@Service
public class PayTimeTask extends BaseController {

    /*@Resource(name = "puserService")
    private PuserManager PuserService;
    
    @Resource(name = "pinteinfoService")
    private PinteinfoManager PinteinfoService;
    
    @Resource(name = "presultService")
    private PresultManager PresultService;
    
    @Resource(name = "poverdetailService")
    private PoverdetailManager PoverdetailService;
    
    @Resource(name = "pinsuinfoService")
    private PinsuinfoManager PinsuinfoService;  
    
    @Resource(name = "pfundService")
    private PfundManager PfundService;  
    
    @Resource(name = "pfeeService")
    private PfeeManager PfeeService;    
    
    @Resource(name = "pbillService")
    private PbillManager PbillService;  
    
    @Resource(name = "pbillitemService")
    private PbillitemManager PbillitemService;  */
    
    @Resource(name = "payService")
    private PayManager payService;  
    
    /**
     * @Description: 定时保存pay数据
     * @author 李世博
     * @date 2016年9月28日
     */
//  @Scheduled(cron = "0 0/1 * * * ?")
    public void payTask() {
        
        logBefore(logger,"pay队列数>>>:"+PayLinkedQueue.getQueuePaySize() + ";当前线程名字：" + Thread.currentThread().getName()+ "," + Thread.currentThread().getId());
        try {
            while(PayLinkedQueue.getQueuePaySize() > 0 )
            {
                P_inte_info pif = PayLinkedQueue.getQueuePay();
                payService.insertPay(pif);
                logBefore(logger, "数据插入：>>"+PayLinkedQueue.getQueuePaySize());
                
            }  
            logBefore(logger, "pay队列为空等待中！！！");

        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error(e.getMessage(), e);
        }
                
    }
    public void payTaskUP() {
        //logBefore(logger, "payUP队列数>>>:"+PayLinkedQueue.getQueuePayUPSize());
        try {
//          while(PayLinkedQueue.getQueuePayUPSize() > 0 )
//          {
////            ·   P_inte_info pif = PayLinkedQueue.getQueuePayUP();
////                payService.insertPay(pif);
////                logBefore(logger, "PayUP数据插入：>>"+PayLinkedQueue.getQueuePayUPSize());
//              
//          }
            //logBefore(logger, "pay队列为空等待中！！！");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error(e.getMessage(), e);
        }
        
    }
    
    public void payTask_Drugstore() {
        
        logBefore(logger,"药店支付pay队列数>>>:"+PayLinkedQueue_Drugstore.getQueuePaySize() + ";当前线程名字：" + Thread.currentThread().getName()+ "," + Thread.currentThread().getId());
        try {
            while(PayLinkedQueue_Drugstore.getQueuePaySize() > 0 )
            {
                P_drugstore_inte_info pif = PayLinkedQueue_Drugstore.getQueuePay();
                payService.insertPay_Drugstore(pif);
                logBefore(logger, "数据插入：>>"+PayLinkedQueue_Drugstore.getQueuePaySize());
                
            }  
            logBefore(logger, "药店支付pay队列为空等待中！！！");

        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error(e.getMessage(), e);
        }
                
    }

}