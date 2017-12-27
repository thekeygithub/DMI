package com.dhcc.ts.yyxg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

@SuppressWarnings("unchecked")
public class Constant {

	public static final List<String> EXAM_REPORT_LIST = new ArrayList<String>();
	
	private static Map<String, String> ICD10_MAP;
	
	static {
		EXAM_REPORT_LIST.add("B160120N00030.png");
		EXAM_REPORT_LIST.add("B160222N00070.png");
		EXAM_REPORT_LIST.add("B160530W00230.png");
		EXAM_REPORT_LIST.add("BG201601010015110000.jpg");
		EXAM_REPORT_LIST.add("BG201601040021410000.jpg");
		EXAM_REPORT_LIST.add("BG201601050016310000.jpg");
		EXAM_REPORT_LIST.add("BG201601060004410000.jpg");
		EXAM_REPORT_LIST.add("BG201601130004910001.jpg");
		EXAM_REPORT_LIST.add("BG201601160021410000.jpg");
		EXAM_REPORT_LIST.add("BG201601180029410001.jpg");
		EXAM_REPORT_LIST.add("BG201601210041110001.jpg");
		EXAM_REPORT_LIST.add("BG201601220011010000.jpg");
		EXAM_REPORT_LIST.add("BG201601230009610001.jpg");
		EXAM_REPORT_LIST.add("BG201601280001510001.jpg");
		EXAM_REPORT_LIST.add("BG201601300007110001.jpg");
		EXAM_REPORT_LIST.add("BG201602010035010001.jpg");
		EXAM_REPORT_LIST.add("BG201602010046810001.jpg");
		EXAM_REPORT_LIST.add("BG201602010049010001.jpg");
		EXAM_REPORT_LIST.add("BG201602010056310001.jpg");
		EXAM_REPORT_LIST.add("BG201602020002910000.jpg");
		EXAM_REPORT_LIST.add("BG201602040010310000.jpg");
		EXAM_REPORT_LIST.add("BG201602050008410000.jpg");
		EXAM_REPORT_LIST.add("BG201602110021710000.jpg");
		EXAM_REPORT_LIST.add("BG201602120003410001.jpg");
		EXAM_REPORT_LIST.add("BG201602140045410001.jpg");
		EXAM_REPORT_LIST.add("BG201602160029610000.jpg");
		EXAM_REPORT_LIST.add("BG201602170059310001.jpg");
		EXAM_REPORT_LIST.add("BG201602200007810001.jpg");
		EXAM_REPORT_LIST.add("BG201602200050610001.jpg");
		EXAM_REPORT_LIST.add("BG201602210022010001.jpg");
		EXAM_REPORT_LIST.add("BG201602220040910000.jpg");
		EXAM_REPORT_LIST.add("BG201602220041010000.jpg");
		EXAM_REPORT_LIST.add("BG201602220041310001.jpg");
		EXAM_REPORT_LIST.add("BG201602260010010000.jpg");
		EXAM_REPORT_LIST.add("BG201602260021610000.jpg");
		EXAM_REPORT_LIST.add("BG201602260048010000.jpg");
		EXAM_REPORT_LIST.add("BG201602270030210000.jpg");
		EXAM_REPORT_LIST.add("BG201602290058410001.jpg");
		EXAM_REPORT_LIST.add("BG201603010055810000.jpg");
		EXAM_REPORT_LIST.add("BG201603020015410000.jpg");
		EXAM_REPORT_LIST.add("BG201603140019710001.jpg");
		EXAM_REPORT_LIST.add("BG201603140063310000.jpg");
		EXAM_REPORT_LIST.add("BG201603140068010001.jpg");
		EXAM_REPORT_LIST.add("BG201603150068810001.jpg");
		EXAM_REPORT_LIST.add("BG201603160059510001.jpg");
		EXAM_REPORT_LIST.add("BG201603160067810001.jpg");
		EXAM_REPORT_LIST.add("BG201603180003410000.jpg");
		EXAM_REPORT_LIST.add("BG201603210061210000.jpg");
		EXAM_REPORT_LIST.add("BG201603210070510001.jpg");
		EXAM_REPORT_LIST.add("BG201603290018910000.jpg");
		EXAM_REPORT_LIST.add("BG201603290062010001.jpg");
		EXAM_REPORT_LIST.add("BG201603300001210001.jpg");
		EXAM_REPORT_LIST.add("BG201604010013110000.jpg");
		EXAM_REPORT_LIST.add("BG201604030025510001.jpg");
		EXAM_REPORT_LIST.add("BG201604070048710000.jpg");
		EXAM_REPORT_LIST.add("BG201604070065110001.jpg");
		EXAM_REPORT_LIST.add("BG201604110047310001.jpg");
		EXAM_REPORT_LIST.add("BG201604170008910001.jpg");
		EXAM_REPORT_LIST.add("BG201604210046710001.jpg");
		EXAM_REPORT_LIST.add("BG201604230006110000.jpg");
		EXAM_REPORT_LIST.add("BG201605050049510000.jpg");
		EXAM_REPORT_LIST.add("BG201605050061110001.jpg");
		EXAM_REPORT_LIST.add("BG201605070019410001.jpg");
		EXAM_REPORT_LIST.add("BG201605230011010001.jpg");
		EXAM_REPORT_LIST.add("BG201605230060310000.jpg");
		EXAM_REPORT_LIST.add("BG201605240005210000.jpg");
		EXAM_REPORT_LIST.add("BG201605250002610001.jpg");
		EXAM_REPORT_LIST.add("BG201605270039510001.jpg");
		EXAM_REPORT_LIST.add("BG201605300025010001.jpg");
		EXAM_REPORT_LIST.add("BG201605310049810000.jpg");
		
		initIDC10_MAP();
	}
	
	
	
	public static Map<String, String> getICD10_MAP() {
		if (ICD10_MAP == null) initIDC10_MAP();
		return ICD10_MAP;
	}

	private static void initIDC10_MAP() {
		ICD10_MAP = new HashMap<String, String>();
		
		Map<String, String> map = (Map<String, String>) new ExcelSupport() {
			
			@Override
			protected Object read(Workbook book) {
				Map<String, String> resultMap = new HashMap<String, String>();
				Sheet sheetOne = book.getSheetAt(0);
				int numberFirst = 1;
				int numberlast = sheetOne.getLastRowNum();
				for(int i=numberFirst;i<=numberlast;i++){
					Row row = sheetOne.getRow(i);
					if(null!=row){
						Cell name = row.getCell(1);
						Cell code = row.getCell(2);
						if (code == null || StringUtils.isBlank(code.toString())) code = row.getCell(3);
						
						resultMap.put(code.toString(), name.toString());
					}
				}
				return resultMap;
			}
		}.read(ServletActionContext.getRequest().getServletContext().getRealPath("/") + "/download/ICD10.xlsx");
//		}.read("E:/01.workspace/exchange/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/MDEPlatform/download/ICD10.xlsx");
		ICD10_MAP.putAll(map);
		
	}
	
	public static void main(String[] args) {
		File file = new File("E:\\01.workspace\\exchange\\MDEPlatform\\WebRoot\\pages\\examReport");
		File[] files = file.listFiles();
		
		for (File f : files) {
			System.out.println(f.getName());
		}
	}
}
