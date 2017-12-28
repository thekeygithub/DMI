package com.ts.service.mts.standardize;

public interface StandardizeService {

	public String standardizeData(String visitId, String visitType, String dataSource, String dataType,
			String parameters, String batchNum,String application,String areaId) throws Exception;
	
	public String standardizeDataNew(String visitId, String visitType, String dataSource, String dataType,
			String parameters, String batchNum,String application,String areaId,String RECORD_INFO_ID,String DB_DATA_ID) throws Exception;
	
	public String standardizeDataForZNWD(String parameters,String areaId) throws Exception;
}
