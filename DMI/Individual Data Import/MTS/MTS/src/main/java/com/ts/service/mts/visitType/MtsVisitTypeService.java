package com.ts.service.mts.visitType;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsVisitType;
import com.ts.util.PageData;

public interface MtsVisitTypeService {

	public void addMtsVisitType(MtsVisitType mtsVisitType) throws Exception;

	public void editMtsVisitType(MtsVisitType mtsVisitType) throws Exception;

	public void deleteMtsVisitType(MtsVisitType mtsVisitType) throws Exception;

	public List<MtsVisitType> findMtsVisitType(MtsVisitType mtsVisitType) throws Exception;
	
	public List<PageData> mtsVisitTypelistPage(Page page)throws Exception;
	
}
