package com.ts.controller.app.appTimer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.base.BaseController;
import com.ts.entity.FaceComparison.P_face_compare;
import com.ts.service.pay.FaceCompareManager;
import com.ts.util.FaceLinkedQueue;

@Service
public class FaceTimeTask extends BaseController {
	
	@Resource(name = "faceCompareService")
	private FaceCompareManager faceCompareService;  

	/**
     * @Description: 定时保存人脸比对数据
     * @author 刘峰
     * @date 2017年6月13日
     */
	public void faceTask() {
		logBefore(logger, "face队列数>>>:"+FaceLinkedQueue.getQueuePaySize() + ";当前线程名字：" + Thread.currentThread().getName()+ "," + Thread.currentThread().getId());
        try {
            while(FaceLinkedQueue.getQueuePaySize() > 0 )
            {
            	P_face_compare pfc = FaceLinkedQueue.getQueuePay();
            	faceCompareService.insert(pfc);
                logBefore(logger, "数据插入：>>"+FaceLinkedQueue.getQueuePaySize());
                
            }  
            logBefore(logger, "face队列为空等待中！！！");
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
                
    }
	
}