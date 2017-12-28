package com.ts.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FaceUtils {

	public static String getInputStreamString(InputStream in) {
		StringBuffer stringBuffer = new StringBuffer();
		if(in != null){
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String line;
			    while ((line = bufferedReader.readLine()) != null) {
			    	stringBuffer.append(line + "\r\n");
			    }
			    in.close();
			    bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
		return stringBuffer.toString().trim();
	}
	
	//图片转化成base64字符串
	public static String GetImageStr(String imgFile) throws IOException {
		//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		BASE64Encoder encoder = null;
		String result = null;
		try{
			//读取图片字节数组
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			//对字节数组Base64编码，并返回Base64编码过的字节数组字符串
			encoder = new BASE64Encoder();
			result = encoder.encode(data);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        	if(in != null){
        		in.close();
        	}
        }
		return result;
	}
	
	//base64字符串转化成图片  
    public static boolean GenerateImage(String imgStr, String imgFilePath) throws IOException {
    	if(imgStr == null || "".equals(imgStr)){
    		return false;
    	}
    	OutputStream out = null;
    	try{
    		//对字节数组字符串进行Base64解码并生成图片
        	BASE64Decoder decoder = new BASE64Decoder();
    		byte[] b = decoder.decodeBuffer(imgStr);
    		//调整异常数据
    		for(int i=0; i<b.length; i++){
    			if(b[i] < 0){
    				b[i] += 256;
    			}
    		}
    		out = new FileOutputStream(imgFilePath);
            out.write(b);
            return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally{
        	if(out != null){
        		out.flush();
                out.close();
        	}
        
        }
    
    }
    
    //删除文件夹目录及文件
    public static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++){
				deleteFile(files[i]);
			}
		}
		file.delete();
	}
    
    //创建文件夹
    public static void createFilePath(String filePath){
    	File dir = new File(filePath);
	    if(!dir.exists()){
	    	dir.mkdirs();
	    }
    }
	
    //文件下载
    public static void fileDown(String filePath, String realName, String fileType, HttpServletRequest request, HttpServletResponse response){
  		try{
  			String line = "/";
  			String OS = System.getProperty("os.name");
  			if(OS!=null && "Windows XP".equals(OS)){
  				line = "\\";
  			}
  			//设置表头
  			response.setContentType("text/plain; charset=UTF-8");
  			request.setCharacterEncoding("UTF-8");
  			response.setBufferSize(100 * 1024);
  			//转码
  			//String realfilename = filePath.substring(filePath.lastIndexOf(line) + 1);
  			String realfilename = realName + "." + fileType;
  			// MS IE5.5 有要作特别处理
  			realfilename = URLDecoder.decode(realfilename, "utf-8");
  			realfilename = java.net.URLEncoder.encode(realfilename, "utf-8");
  			if(request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
  				response.setHeader("Content-Disposition", "filename=\"" + realfilename + "\"");
  			}else{ // 非 IE5.5 的 Header 设定方式,IE 5.5 不能加attachment
  				response.setHeader("Content-Disposition", "attachment; filename=\"" + new String((realfilename).getBytes(), "UTF-8") + "\"");
  			}
  			String file = filePath + realfilename;
  			/*开始输出*/
  			int len;
  			byte[] b = new byte[1024];
  			FileInputStream fin = new FileInputStream(file);
  			ServletOutputStream out = response.getOutputStream();
  			while((len = fin.read(b, 0, 1024)) > 0){
  				out.write(b, 0, len);
  			}
  			out.flush();
  			out.close();
  			fin.close();
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  	
  	}
    
    public static void main(String[] args) {
		try{
			//System.out.println(toCast(null, String.class));
			//System.out.println(Integer.parseInt(toCast(null, Integer.class)));
			//System.out.println(Double.parseDouble(toCast(null, Double.class)));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}