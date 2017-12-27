package com.dhcc.ts.yyxg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class ExcelSupport {

	public Object read(String filePath) {
		
		Object obj = null;
		Workbook book = null;
		long start = System.currentTimeMillis();
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			try {
				book = new XSSFWorkbook(fileInputStream);
			} catch (Exception ex) {
				book = new HSSFWorkbook(fileInputStream);
			}
			
			obj = read(book);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("====>> 读取ICD10.xlsx耗时："+(System.currentTimeMillis()-start)/1000+"s.");
		
		return obj;
	}

	protected abstract Object read(Workbook book);
	
}
