package com.ts.service.pay.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.FaceComparison.P_face_compare;
import com.ts.service.pay.FaceCompareManager;

@Service("faceCompareService")
public class FaceCompareService implements FaceCompareManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void insert(P_face_compare pfc) throws Exception {
		 dao.save("FaceCompareMapper.save", pfc);
	}
	
}