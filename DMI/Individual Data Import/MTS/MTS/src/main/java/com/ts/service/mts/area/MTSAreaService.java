package com.ts.service.mts.area;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.util.PageData;

public interface MTSAreaService {

	public void addMtsArea(MtsArea mtsArea) throws Exception;

	public void editMtsArea(MtsArea mtsArea) throws Exception;

	public void deleteMtsArea(MtsArea mtsArea) throws Exception;

	public List<MtsArea> findMtsArea(MtsArea mtsArea) throws Exception;
	
	public List<PageData> mtsAreaListPage(Page page)throws Exception;
	
}
