package com.dhcc.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;

import com.dhcc.modal.system.Tsconfig;

/**
 * 导出数据Excel工具类
 * @author GYR
 * @date 2015-09-16
 */
public class ExcelExportUtil {
	
	/**
	 * 导出Excel报表
	 * @param <T> 泛型
	 * @param excelName 报表名称
	 * @param headTtile 报表头部标题
	 * @param titleMap 报表名称数组对应属性名称的map 使用LinkedHashMap
	 * @param list 数据集合
	 * @param c class
	 * return  文件名称(带路径){如果成功返回文件路径，如果不成功返回空字符串}
	 */
	@SuppressWarnings("unchecked")
	public static <T> String exportExcel(String excelName,String headTitle,Map<String,String> titleMap,List list,Class<T> c) {
		 // 声明一个工作薄
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     // 生成一个表格
	     String excelTitle = excelName;
	     HSSFSheet sheet = workbook.createSheet("sheet1");
	     // 设置表格默认列宽度为15个字节
	     sheet.setDefaultColumnWidth(15);
	     // 生成一个样式
	     HSSFCellStyle style = workbook.createCellStyle();
	     // 设置这些样式
	     style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     // 生成一个字体
	     HSSFFont font = workbook.createFont();
	     font.setFontHeightInPoints((short)10);
	     font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	     // 把字体应用到当前的样式
	     style.setFont(font);
	     //产生表格标题行
	     String[] title = new String[titleMap.size()];
	     int titleIndex = 0;
	     for(Map.Entry<String, String> entry : titleMap.entrySet()){
	    	 title[titleIndex] = entry.getKey();
	    	 titleIndex++;
	     }
	     int startIndex = 0;//开始的行
	     if(!StringUtil.isNullOrEmpty(headTitle)){
	    	 //生成一个样式
		     HSSFCellStyle styleT = workbook.createCellStyle();
		     // 设置这些样式
		     styleT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		     styleT.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		     // 生成一个字体
		     HSSFFont fontT = workbook.createFont();
		     fontT.setFontHeightInPoints((short)14);
		     fontT.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		     // 把字体应用到当前的样式
		     styleT.setFont(fontT);
		     
	    	sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, title.length-1));
    	 	HSSFRow rowT = sheet.createRow(0);
			HSSFCell cellT = rowT.createCell(0);
			cellT.setCellStyle(styleT);
			cellT.setCellValue(headTitle);
			startIndex = 2;
	     }
	     HSSFRow row = sheet.createRow(startIndex);
	     startIndex = startIndex + 1;
	     for (int i = 0; i < title.length; i++) {
	    	 HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	         HSSFRichTextString text = new HSSFRichTextString(title[i]);
	         cell.setCellValue(text);
	     }
	     
	     //数据行样式
	     //生成一个样式
	     HSSFCellStyle style1 = workbook.createCellStyle();
	     // 设置这些样式
	     style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     // 生成一个字体
	     HSSFFont font1 = workbook.createFont();
	     font1.setFontHeightInPoints((short)10);
	     font1.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	     // 把字体应用到当前的样式
	     style1.setFont(font1);
	     
		 T obj = null;
		 try {
			if(c!=null){
				for(int i=0;i<list.size();i++){
					obj = (T) list.get(i);
					row = sheet.createRow(i+startIndex);
						for(int j=0;j<title.length;j++){
							String t = title[j];
							String method = filedToMethod(titleMap.get(t));
							//System.out.println(c.getMethod(method, new Class[]{}).getReturnType().getName());
							String value = String.valueOf(c.getMethod(method, new Class[]{}).invoke(obj, new Object[]{}));
							HSSFCell cellj = row.createCell(j);
							cellj.setCellType(HSSFCell.CELL_TYPE_STRING);
							cellj.setCellStyle(style1);
							HSSFRichTextString textj = new HSSFRichTextString(removeNull(value));
							cellj.setCellValue(textj);
							method = null;
						}
				}
		    }else{
		    	
		    	for(int i=0;i<list.size();i++){
		    		Map<String,String> map =  (Map<String, String>) list.get(i);
					row = sheet.createRow(i+startIndex);
						for(int j=0;j<title.length;j++){
							String t = title[j];
							String method = titleMap.get(t);
							//System.out.println(c.getMethod(method, new Class[]{}).getReturnType().getName());
							String value = map.get(method);
							
							HSSFCell cellj = row.createCell(j);
							cellj.setCellType(HSSFCell.CELL_TYPE_STRING);
							cellj.setCellStyle(style1);
							HSSFRichTextString textj = new HSSFRichTextString(removeNull(value));
							cellj.setCellValue(textj);
							method = null;
						}
				}
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OutputStream out = null;
		String name = excelTitle + ".xls";
		String path = null;
		try{ 
			
			path = ServletActionContext.getServletContext().getRealPath("/download");
			
			File fileCheck = new File(path);
			if(!fileCheck.exists()){
				fileCheck.mkdirs();
			}
			path = path + File.separator + name;
			out = new FileOutputStream(path);
			workbook.write(out);  
			out.flush();
			out.close();
		}catch (IOException e){  
			System.out.println("文件正在使用，不能重新写入！");
		} 
		File excelFile = new File(path);
		if(excelFile.exists()){
			System.out.println("*************导出" + excelTitle + "报表成功！************");
			return path;
		}else{
			System.out.println("*************导出" + excelTitle + "报表失败！************");
			return "";
		}
	}
	
	/**
	 * 根据字段名称拼成获取值的get方法
	 * @param name
	 * eg:username --> getUsername
	 */
	private static String filedToMethod(String name){
		
		return "get" + name.substring(0,1).toUpperCase() + name.substring(1);
		
	}
	
	private static String removeNull(String value){
		if(StringUtil.isNullOrEmpty(value) || value.equals("null")){
			return "";
		}
		return value;
	}

}
