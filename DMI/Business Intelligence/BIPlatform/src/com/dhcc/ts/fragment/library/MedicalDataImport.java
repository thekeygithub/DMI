package com.dhcc.ts.fragment.library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dhcc.common.util.CommUtil;
import com.dhcc.ts.database.DBManager_LIBRARY;

public class MedicalDataImport {

	public static void main(String[] args) {
		String path = "C:\\Users\\xiangbao.guan\\Desktop\\new3.xlsx";
		MedicalDataImport dataImport = new MedicalDataImport();
		List<Map<String, String>> list = dataImport.getListFormExcel(path);
//		System.out.println(list);
		dataImport.saveBasicData2DB(list);
		dataImport.saveDetailData2DB(list);
	}
	
	public void saveBasicData2DB(List<Map<String, String>> list) {
		DBManager_LIBRARY db_lib = new DBManager_LIBRARY();
		StringBuilder content = new StringBuilder();
		for (Map<String, String> row : list) {
			String QLBI_ID = row.get("QLBI_ID");
			String TITLE_CHS = row.get("TITLE_CHS");
			String ABSTRACT_CHS = row.get("ABSTRACT_CHS").replaceAll("\\'", "\\\\'");
			String DESCRIPTOR_CHS = row.get("DESCRIPTOR_CHS").replaceAll("\\'", "\\\\'");
			String RESOURCE_TYPE = "5";
			String VIEW_COUNT = "0";
			String CREATOR = "system";
			
			StringBuilder sql = new StringBuilder("insert into q_library_basic_info (QLBI_ID, TITLE_CHS, ABSTRACT_CHS, DESCRIPTOR_CHS, RESOURCE_TYPE, VIEW_COUNT, CREATOR) values (");
			sql.append("'").append(QLBI_ID).append("', '").append(TITLE_CHS).append("', '")
				.append(ABSTRACT_CHS).append("', '").append(DESCRIPTOR_CHS).append("', '")
				.append(RESOURCE_TYPE).append("', ").append(VIEW_COUNT).append(", '")
				.append(CREATOR).append("')");
			db_lib.executeUpdate(sql.toString());
			content.append(sql).append(";"+System.getProperty("line.separator"));
//			System.out.println("====>> saveBasicData2DB:"+sql.toString());
		}
		FileWriter.write2File("C:\\Users\\xiangbao.guan\\Desktop\\medical_basic.txt", content.toString());
		db_lib.close();
	}
	
	public void saveDetailData2DB(List<Map<String, String>> list) {
		DBManager_LIBRARY db_lib = new DBManager_LIBRARY();
		StringBuilder content = new StringBuilder();
		for (Map<String, String> row : list) {
			String QLDI_ID = "'" + CommUtil.getID() + "', ";
			String QLBI_ID = "'" + row.get("QLBI_ID") + "', ";
			String DETAILES = "'" + row.get("DETAILES").replaceAll("\\'", "\\\\'") + "', ";
			String batch_id = "'medical_1'";
			
			StringBuilder sql = new StringBuilder("insert into q_library_detaile_info (QLDI_ID, QLBI_ID, DETAILES, batch_id) values (");
			sql.append(QLDI_ID).append(QLBI_ID).append(DETAILES).append(batch_id).append(")");
			db_lib.executeUpdate(sql.toString());
			content.append(sql).append(";"+System.getProperty("line.separator"));
//			System.out.println("====>> saveDetailData2DB:"+sql.toString());
		}
		FileWriter.write2File("C:\\Users\\xiangbao.guan\\Desktop\\medical_detail.txt", content.toString());
		db_lib.close();
	}
	
	public List<Map<String, String>> getListFormExcel(String execelFile) {
		List< Map<String, String>> result = new ArrayList< Map<String, String>>();
		List<String> header = new ArrayList<String>();
		Workbook book = null;
		long start = System.currentTimeMillis();
		try {
			FileInputStream fileInputStream = new FileInputStream(execelFile);
			try {
				book = new XSSFWorkbook(fileInputStream);
			} catch (Exception ex) {
				book = new HSSFWorkbook(fileInputStream);
			}
			Sheet sheetOne = book.getSheetAt(0);
			
			int firstRowNum = sheetOne.getFirstRowNum();
			Row title = sheetOne.getRow(firstRowNum);
			for(int i = 0; i < title.getLastCellNum(); i++){
				Cell cell = title.getCell(i);
				header.add(cell == null ? "index["+firstRowNum+"]["+i+"]" : cell.toString().trim());
			}
			
			int numberlast = sheetOne.getLastRowNum();
			for(int i = firstRowNum + 1; i <= numberlast; i++) {
				Row row = sheetOne.getRow(i);
				if(row != null){
					Map<String, String> rowMap = new HashMap<String, String>();
					rowMap.put("QLBI_ID", CommUtil.getID());
					for (int j = 0; j < header.size(); j++) {
						Cell cell = row.getCell(j);
						rowMap.put(header.get(j), cell == null ? "index["+i+"]["+j+"]" : cell.toString().trim());
					}
					result.add(rowMap);
				}
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("====>> getListFormExcel():耗时："+(System.currentTimeMillis()-start)/1000+"s.");
		System.out.println("Excel中记录总数：" + result.size());
		return result;
	}
}
