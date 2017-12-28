package com.ts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.plexus.logging.Logger;

/**
 * 从EXCEL导入到数据库 创建人： 创建时间：2014年12月23日
 * 
 * @version
 */
public class ObjectExcelRead {

	/**
	 * 适用于没有标题行的excel，例如 张三 25岁 男 175cm 李四 22岁 女 160cm
	 * 每一行构成一个map，key值是列标题，value是列值。没有值的单元格其value值为null
	 * 返回结果最外层的list对应一个excel文件，第二层的list对应一个sheet页，第三层的map对应sheet页中的一行
	 * 
	 * @throws Exception
	 */
	public static List<Object> readExcelWithoutTitle(String filepath, String filename, int startrow, int startcol,
			int sheetnum) throws Exception {
		File target = new File(filepath, filename);
		String fileName = target.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		InputStream is = null;
		Workbook wb = null;
		List<Object> varList = new ArrayList<Object>();
		Sheet sheet = null;
		try {
			is = new FileInputStream(target);
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
				sheet = wb.getSheetAt(sheetnum);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
				sheet = wb.getSheetAt(sheetnum);
			} else {
				throw new Exception("读取的不是excel文件");
			}
//			List<List<List<String>>> result = new ArrayList<List<List<String>>>();// 对应excel文件
//			int sheetSize = wb.getNumberOfSheets();
//			List<List<String>> sheetList = new ArrayList<List<String>>();// 对应sheet页
			int rowSize = sheet.getLastRowNum() + 1;
			for (int j = startrow; j < rowSize; j++) {// 遍历行
				PageData varpd = new PageData();
				Row row = sheet.getRow(j);
				if (row == null) {// 略过空行
					continue;
				}
				int cellSize = row.getLastCellNum();// 行中有多少个单元格，也就是有多少列
				for (int k = startcol; k < cellSize; k++) {
					Cell cell = row.getCell(k);
					String cellValue = null; 
					if (cell != null) {
						 // 以下是判断数据的类型  
	                    switch (cell.getCellType()) {  
	                    case HSSFCell.CELL_TYPE_NUMERIC: // 数字  
	                        DecimalFormat df = new DecimalFormat("0");  
	                        cellValue = df.format(cell.getNumericCellValue());  
	                        break;  
	  
	                    case HSSFCell.CELL_TYPE_STRING: // 字符串  
	                        cellValue = cell.getStringCellValue();  
	                        break;  
	  
	                    case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean  
	                        cellValue = cell.getBooleanCellValue() + "";  
	                        break;  
	  
	                    case HSSFCell.CELL_TYPE_FORMULA: // 公式  
	                        cellValue = cell.getCellFormula() + "";  
	                        break;  
	  
	                    case HSSFCell.CELL_TYPE_BLANK: // 空值  
	                        cellValue = "";  
	                        break;  
	  
	                    case HSSFCell.CELL_TYPE_ERROR: // 故障  
	                        cellValue = "非法字符";  
	                        break;  
	  
	                    default:  
	                        cellValue = "未知类型";  
	                        break;  
	                    }  
						//cellValue = cell.toString();
					}
					varpd.put("var" + k, cellValue);
				}
				varList.add(varpd);
			}
			return varList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * @param filepath
	 *            //文件路径
	 * @param filename
	 *            //文件名
	 * @param startrow
	 *            //开始行号
	 * @param startcol
	 *            //开始列号
	 * @param sheetnum
	 *            //sheet
	 * @return list
	 */
	public static List<Object> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum) {
		List<Object> varList = new ArrayList<Object>();

		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
			HSSFWorkbook wb = new HSSFWorkbook(fi);
			HSSFSheet sheet = wb.getSheetAt(sheetnum); // sheet 从0开始
			int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号

			for (int i = startrow; i < rowNum; i++) { // 行循环开始

				PageData varpd = new PageData();
				HSSFRow row = sheet.getRow(i); // 行
				int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

				for (int j = startcol; j < cellNum; j++) { // 列循环开始

					HSSFCell cell = row.getCell(Short.parseShort(j + ""));
					String cellValue = null;
					if (null != cell) {
						switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
						case 0:
							cellValue = String.valueOf((int) cell.getNumericCellValue());
							break;
						case 1:
							cellValue = cell.getStringCellValue();
							break;
						case 2:
							cellValue = cell.getNumericCellValue() + "";
							// cellValue =
							// String.valueOf(cell.getDateCellValue());
							break;
						case 3:
							cellValue = "";
							break;
						case 4:
							cellValue = String.valueOf(cell.getBooleanCellValue());
							break;
						case 5:
							cellValue = String.valueOf(cell.getErrorCellValue());
							break;
						}
					} else {
						cellValue = "";
					}

					varpd.put("var" + j, cellValue);

				}
				varList.add(varpd);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return varList;
	}

	/**
	 * excel导出到输出流 谁调用谁负责关闭输出流
	 * 
	 * @param os
	 *            输出流
	 * @param excelExtName
	 *            excel文件的扩展名，支持xls和xlsx，不带点号
	 * @param data
	 * @throws IOException
	 */
	public static void writeExcel(OutputStream os, String excelExtName, Map<String, List<List<String>>> data)
			throws IOException {
		Workbook wb = null;
		try {
			if ("xls".equals(excelExtName)) {
				wb = new HSSFWorkbook();
			} else if ("xlsx".equals(excelExtName)) {
				wb = new XSSFWorkbook();
			} else {
				throw new Exception("当前文件不是excel文件");
			}
			CellStyle cs=wb.createCellStyle();
			cs.setWrapText(true);
			for (String sheetName : data.keySet()) {
				Sheet sheet = wb.createSheet(sheetName);
				List<List<String>> rowList = data.get(sheetName);
				for (int i = 0; i < rowList.size(); i++) {
					List<String> cellList = rowList.get(i);
					Row row = sheet.createRow(i);
					for (int j = 0; j < cellList.size(); j++) {
						Cell cell = row.createCell(j);
						cell.setCellValue(cellList.get(j));
					}
				}
			}
			wb.write(os);
			System.out.println(os.toString());
			System.out.println(1111);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}

	
	/**
	 * excel导出到输出流 谁调用谁负责关闭输出流
	 * 
	 * @param os
	 *            输出流
	 * @param excelExtName
	 *            excel文件的扩展名，支持xls和xlsx，不带点号
	 * @param data
	 * @throws IOException
	 */
	public static void writeCorpusExcel(OutputStream os, String excelExtName, Map<String, List<List<String>>> data)
			throws IOException {
		Workbook wb = null;
		try {
			if ("xls".equals(excelExtName)) {
				wb = new HSSFWorkbook();
			} else if ("xlsx".equals(excelExtName)) {
				wb = new XSSFWorkbook();
			} else {
				throw new Exception("当前文件不是excel文件");
			}
			for (String sheetName : data.keySet()) {
				Sheet sheet = wb.createSheet(sheetName);
				List<List<String>> rowList = data.get(sheetName);
				 //设置标题背景颜色
				CellStyle style  = wb.createCellStyle();
				style.setFillForegroundColor(IndexedColors.YELLOW.index);
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				Row row = sheet.createRow(1);
				Cell cell = row.createCell((short) 0);
				cell.setCellStyle(style);
				cell.setCellValue("原始诊断");
				
				Cell cell2 = row.createCell((short) 1);
				cell2.setCellStyle(style);
				cell2.setCellValue("预计识别的实体");
				
				for (int i = 0; i < rowList.size(); i++) {
					List<String> cellList = rowList.get(i);
					row = sheet.createRow(i+1);
					for (int j = 0; j < cellList.size(); j++) {
						cell = row.createCell(j);
						cell.setCellValue(cellList.get(j));
					}
				}
			}
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}
	/**
	 * excel导出到输出流 谁调用谁负责关闭输出流
	 * 
	 * @param os
	 *            输出流
	 * @param excelExtName
	 *            excel文件的扩展名，支持xls和xlsx，不带点号
	 * @param data
	 *            excel数据，map中的key是标签页的名称，value对应的list是标签页中的数据。
	 *            list中的子list是标签页中的一行，子list中的对象是一个单元格的数据，包括是否居中、跨几行几列以及存的值是多少
	 * @throws IOException
	 */
	public static void testWrite(OutputStream os, String excelExtName, Map<String, List<List<ExcelData>>> data)
			throws IOException {
		Workbook wb = null;
		CellStyle cellStyle = null;
		boolean isXls;
		try {
			if ("xls".equals(excelExtName)) {
				wb = new HSSFWorkbook();
				isXls = true;
			} else if ("xlsx".equals(excelExtName)) {
				wb = new XSSFWorkbook();
				isXls = false;
			} else {
				throw new Exception("当前文件不是excel文件");
			}
			cellStyle = wb.createCellStyle();
			if (isXls) {
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			} else {
				cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			}
			for (String sheetName : data.keySet()) {
				Sheet sheet = wb.createSheet(sheetName);
				List<List<ExcelData>> rowList = data.get(sheetName);
				// i 代表第几行 从0开始
				for (int i = 0; i < rowList.size(); i++) {
					List<ExcelData> cellList = rowList.get(i);
					Row row = sheet.createRow(i);
					int j = 0;// j 代表第几列 从0开始
					for (ExcelData excelData : cellList) {
						if (excelData != null) {
							if (excelData.getColSpan() > 1 || excelData.getRowSpan() > 1) {
								CellRangeAddress cra = new CellRangeAddress(i, i + excelData.getRowSpan() - 1, j,
										j + excelData.getColSpan() - 1);
								sheet.addMergedRegion(cra);
							}
							Cell cell = row.createCell(j);
							cell.setCellValue(excelData.getValue());
							if (excelData.isAlignCenter()) {
								cell.setCellStyle(cellStyle);
							}
							j = j + excelData.getColSpan();
						} else {
							j++;
						}
					}
				}
			}
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}
}
