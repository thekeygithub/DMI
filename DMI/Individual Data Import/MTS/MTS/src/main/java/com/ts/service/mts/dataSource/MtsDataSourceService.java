package com.ts.service.mts.dataSource;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataSource;
import com.ts.util.PageData;

public interface MtsDataSourceService {

	public void addMtsDataSource(MtsDataSource mtsDataSource) throws Exception;

	public void editMtsDataSource(MtsDataSource mtsDataSource) throws Exception;

	public void deleteMtsDataSource(MtsDataSource mtsDataSource) throws Exception;

	public List<MtsDataSource> findMtsDataSource(MtsDataSource mtsDataSource) throws Exception;
	
	public List<PageData> mtsDataSourcelistPage(Page page)throws Exception;
	
}
