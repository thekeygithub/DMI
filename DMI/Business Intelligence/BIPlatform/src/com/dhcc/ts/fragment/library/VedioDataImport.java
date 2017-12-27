package com.dhcc.ts.fragment.library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dhcc.common.util.CommUtil;
import com.dhcc.ts.database.DBManager_LIBRARY;


public class VedioDataImport {

//	private List<String> basicList;
//	private List<String> detailList;
//	private Connection conn;
//	private PreparedStatement pstmt;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		VedioDataImport dataImport = new VedioDataImport();
		List<String> fileList = dataImport.getListFormFtp("10.10.40.220", 21, "ftper", "ftper", "/library/1/");
		String path = "C:\\Users\\xiangbao.guan\\Desktop\\new.xlsx";
		Map<String, Map<String, String>> dataMap = dataImport.getMapFormExcel(path);
		List<Map<String, String>> list = dataImport.getListAfterCompare(fileList, dataMap);
		dataImport.saveBasicData2DB(list);
		dataImport.saveDetailData2DB(list);
	}
	
//	private String getSqlTemplate() {
//		
//		return "";
//	}
	
//	private void init() throws SQLException {
//		basicList.add("QLBI_ID-id");
//		basicList.add("TITLE_CHS-name");
//		basicList.add("RESOURCE_TYPE-'6'");
//		basicList.add("VIEW_COUNT-0");
//		basicList.add("CREATOR-'system'");
//		basicList.add("ABSTRACT_CHS-categories_name>>subjecttype_name>>professional_name ");
//		basicList.add("DESCRIPTOR_CHS-keyword");
//		
//		detailList.add("QLDI_ID-uuid");
//		detailList.add("QLBI_ID-id");
//		detailList.add("DETAILES-filename+filetype");
//		detailList.add("batch_id-id");
//		
//		conn = DBConnectionManager_LIBRARY.getConnection();
//		pstmt = conn.prepareStatement(getSqlTemplate());
//	}
	
	public void saveBasicData2DB(List<Map<String, String>> list) {
		DBManager_LIBRARY db_lib = new DBManager_LIBRARY();
		StringBuilder content = new StringBuilder();
		for (Map<String, String> row : list) {
			String QLBI_ID = row.get("id");
			QLBI_ID = QLBI_ID.substring(0, QLBI_ID.indexOf('.'));
			String TITLE_CHS = row.get("name");
			String RESOURCE_TYPE = "6";
			String VIEW_COUNT = "0";
			String CREATOR = "system";
			String categories_name = row.get("categories_name");
			String subjecttype_name = row.get("subjecttype_name");
			String professional_name = row.get("professional_name");
			String ABSTRACT_CHS = categories_name + ">>" + subjecttype_name + (StringUtils.isBlank(professional_name)?"":">>"+professional_name);
			String DESCRIPTOR_CHS = row.get("keyword");
			
			StringBuilder sql = new StringBuilder("insert into q_library_basic_info (QLBI_ID, TITLE_CHS, RESOURCE_TYPE, VIEW_COUNT, CREATOR, ABSTRACT_CHS, DESCRIPTOR_CHS, JOURNAL_TITLE, AUTHOR_CHS) values (");
			sql.append("'").append(QLBI_ID).append("', '").append(TITLE_CHS).append("', '")
				.append(RESOURCE_TYPE).append("', ").append(VIEW_COUNT).append(", '")
				.append(CREATOR).append("', '").append(ABSTRACT_CHS).append("', '")
				.append(DESCRIPTOR_CHS).append("', '中国疾病知识仓库', '佚名')");
			db_lib.executeUpdate(sql.toString());
			content.append(sql).append(";"+System.getProperty("line.separator"));
			System.out.println("====>> saveBasicData2DB:"+sql.toString());
		}
		FileWriter.write2File("C:\\Users\\xiangbao.guan\\Desktop\\basic.txt", content.toString());
		db_lib.close();
	}
	
	public void saveDetailData2DB(List<Map<String, String>> list) {
		DBManager_LIBRARY db_lib = new DBManager_LIBRARY();
		StringBuilder content = new StringBuilder();
		for (Map<String, String> row : list) {
			String QLDI_ID = CommUtil.getID();
			String QLBI_ID = row.get("id");
			QLBI_ID = QLBI_ID.substring(0, QLBI_ID.indexOf('.'));
			String DETAILES = row.get("filename")+row.get("filetype");
			String batch_id = "system";
			
			StringBuilder sql = new StringBuilder("insert into q_library_detaile_info (QLDI_ID, QLBI_ID, DETAILES, batch_id) values (");
			sql.append("'").append(QLDI_ID).append("', '").append(QLBI_ID).append("', '")
				.append(DETAILES).append("', '").append(batch_id).append("')");
			db_lib.executeUpdate(sql.toString());
			content.append(sql).append(";"+System.getProperty("line.separator"));
			System.out.println("====>> saveDetailData2DB:"+sql.toString());
		}
		FileWriter.write2File("C:\\Users\\xiangbao.guan\\Desktop\\detail.txt", content.toString());
		db_lib.close();
	}
	
	public List<Map<String, String>> getListAfterCompare(List<String> fileList, Map<String, Map<String, String>> dataMap) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
//		int index = 0;
		for (String file : fileList) {
			Map<String, String> data = dataMap.get(file);
			if (data != null) {
				result.add(data);
//				index++;
//				System.out.println("[" + index + "] " + data);
			}
		}
		
		return result;
	}

	public List<String> getListFormFtp(String host, int port, String username, String password, String dir) throws IOException {
		long start = System.currentTimeMillis();
//		List<String> nameList = FtpsFileViewer.listFileNames(host, port, username, password, dir);
		FTPFileViewer f = new FTPFileViewer(true);
		List<String> nameList = null;
		if(f.login("10.10.40.220", 21, "ftper", "ftper")){
			nameList = f.listFile("/library/1/");
			f.disConnection();
			System.out.println("====>> getListFormFtp():耗时："+(System.currentTimeMillis()-start)/1000+"s.");
			System.out.println("FTP中文件总数：" + nameList.size());
			return nameList;
		}
		return new ArrayList<String>();
	}
	
	public Map<String, Map<String, String>> getMapFormExcel(String execelFile) {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
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
					for (int j = 0; j < header.size(); j++) {
						Cell cell = row.getCell(j);
						rowMap.put(header.get(j), cell == null ? "index["+i+"]["+j+"]" : cell.toString().trim());
					}
					String key = rowMap.get("filename") + rowMap.get("filetype");
					result.put(key, rowMap);
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
