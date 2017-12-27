package com.framework.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

//import org.dom4j.Attribute;
//import org.dom4j.Document;
//import org.dom4j.io.SAXReader;

public class CommMethod {
	public static final String NULL = "null";
	public static final String BLANK = "";
  private String code;
  DecimalFormat df ;
  public CommMethod() {
    df = new DecimalFormat("0.00");    
  }

  public CommMethod(String code) {
    this.code = code;
     df = new DecimalFormat("0.00");
    
  }
  
  
  public static String fmByte2KbMbGb(long size) {
	  	 String sizestr = "";
	  	 DecimalFormat dft = new DecimalFormat("0.00");    
	     float f;
	     if ( size < 1024 * 1024 ) {
	    	 f = (float) ((float) size / (float) 1024);
	    	 sizestr = (dft.format(new Float(f).doubleValue())+"KB");
	     } else {
	    	 f = (float) ((float) size / (float) (1024 * 1024));
	    	 sizestr = (dft.format(new Float(f).doubleValue())+"M");
	     }  
	  return sizestr;
  }

  //保留两位小数， 输入double返回String
  public String fmDoubleToStr(double dl, int flag) {
    return df.format(dl);
  }
  
  //保留 flag位小数， 输入String返回double  str必须是double格式
  public static String fmStrTo(String str, int flag) {
	if ( str == null || "".equals(str) )  return "";
	 
	String fmStr = "0.";
	for ( int i = 0; i < flag; i++ ) {
		fmStr += "0";
	}
	double dl = Double.parseDouble(str);
	DecimalFormat dff  = new DecimalFormat(fmStr);
	
    return dff.format(dl);
  }

  //保留 flag位小数， 输入String返回double  str必须是double格式
  public static String fmStrToDecimal(String str, int flag) {
	if ( str == null || "".equals(str) )  return "";
	 
	String fmStr = "0.";
	for ( int i = 0; i < flag; i++ ) {
		fmStr += "#";
	}
	double dl = Double.parseDouble(str);
	DecimalFormat dff  = new DecimalFormat(fmStr);
	
    return dff.format(dl);
  }

  
  /*
   * 从request对象获取参数map对象
   */
  public static Map getRequestMap(HttpServletRequest request) {  

	    // 参数Map  

	    Map properties = request.getParameterMap();  

	    // 返回值Map  

	    Map returnMap = new HashMap();  

	    Iterator entries = properties.entrySet().iterator();  

	    Map.Entry entry;  

	    String name = "";  

	    String value = "";  

	    while (entries.hasNext()) {  
	    		
	        entry = (Map.Entry) entries.next();  

	        name = (String) entry.getKey();  

	        Object valueObj = entry.getValue();  
	        value = "";  

	        if(null == valueObj){  

	            value = "";  

	        }else if(valueObj instanceof String[]){  

	            String[] values = (String[])valueObj;  

	            for(int i=0;i<values.length;i++){  

	                value += values[i] + ",";  

	            }  

	            value = value.substring(0, value.length()-1);  

	        }else{  

	            value = valueObj.toString();  

	        }  

	        returnMap.put(name, value);  

	    }  

	    return returnMap;  

	} 
  
  public static String getValThenOpMap(Map map, String key , String op_type) {
	  String  ret = String.valueOf(map.get(key));
	  if ( "del_key".equals(op_type) ) {
		  map.remove(key);
	  }
	  return ret;
  }
  
  
  //
  public static String getObject2Str(Object str) {
	  String ret = "";
	  if ( str == null )  ret = "";
	  else ret = String.valueOf(str);
	  return ret;
  }


    /*
	 * 判断是否是  数字
	 */
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
 
  /**
   * 将特殊字符分隔的字符串分解为数组，如"1^2233^23^22"或者"1^2233^23^22^"
   * @param  val 源字符串
   * @param  c_b 特殊字符
   * @return 转换后的字符串数组
   */
  public static String[] strToArray(String strSource, String strSpecial) {
    if (strSource == null || strSource.equalsIgnoreCase("")) {
      return null;
    }
    //确定符号strSpecial在strSource中最后出现的位置，第一个字符为零
    int iLastIndex = strSource.lastIndexOf(strSpecial);
    //如果符号strSpecial不在strSource的最后，则将其加在最后
    if (iLastIndex != (strSource.length() - strSpecial.length())) {
      strSource = strSource + strSpecial;
    }

    String[] ReturnArray;
    int Counter = 0;
    int StartPos = 0, EndPos = 0;
    //确定 strSpecial在strSource中出现的开始位置。
    StartPos = strSource.indexOf(strSpecial);
    //确定strSpecial在strSource中出现的个数
    while (StartPos != -1) {
      Counter++;
      StartPos = strSource.indexOf(strSpecial, StartPos + 1);
    }

    if (Counter == 0) {
      return null;
    }
    else { //根据strSpecial在strSource中出现的个数确定数组的维数
      ReturnArray = new String[Counter];
      StartPos = 0;
      //strSpecial在strSource中出现的开始位置
      EndPos = strSource.indexOf(strSpecial);
      Counter = 0;

      while (EndPos != -1) {
        //截取0--符号之间的字符串，并付给个个维变量,完成字符串到数组的转换。
        ReturnArray[Counter] = strSource.substring(StartPos, EndPos);
        Counter++;
        StartPos = EndPos + strSpecial.length();
        EndPos = strSource.indexOf(strSpecial, StartPos);
      }
    }

    return ReturnArray;
  }
  
  /**   
   * 删除单个文件   
   * @param   fileName    被删除文件的文件名   
   * @return 单个文件删除成功返回1,  否则返回-1  -2,  -1是删除异常，  -2 是不存在文件  
   */   
  public static int deleteFile(String fileName){    	  
      File file = new File(fileName);   
    try{      
      if(file.isFile() && file.exists()){
          file.delete();    
          System.out.println("删除成功"+fileName);    
          return 1;    
      }else{    
          System.out.println("删除失败,不存在文件!"+fileName);    
          return -2;    
      }    
    } catch(Exception e){
    	System.out.println("删除失败"+fileName);  
    	return -1;  
    }   
 
  }    
      
  /**   
   * 创建目录  
   */ 
  
  public static boolean newFileDir(String folderPath) {
	    try {
	      String filePath = folderPath;
	      filePath = filePath.toString();
	      java.io.File dirFile = new java.io.File(filePath);
	      if(!dirFile.exists() || !dirFile.isDirectory()){  // 目录不存在, 则新建
	    	  dirFile.mkdir();
	    	  System.out.println("新建目录:"+folderPath);
	    	  return true;
	      } else {	    	  
	    	  return true;
	      }
	    }
	    catch (Exception e) {
	      System.out.println("新建目录操作出错:"+folderPath);
	      e.printStackTrace();
	      return false;
	    }
	  }

  /**   
   * 删除目录（文件夹）以及目录下的文件   
   * @param   dir 被删除目录的文件路径   
   * @return  目录删除成功返回true,否则返回false   
   */   
  public static boolean deleteFileDir(String dir){    
      //如果dir不以文件分隔符结尾，自动添加文件分隔符    
      if(!dir.endsWith(File.separator)){    
          dir = dir+File.separator;    
      }    
      File dirFile = new File(dir);    
      //如果dir对应的文件不存在，或者不是一个目录，则退出    
      if(!dirFile.exists() || !dirFile.isDirectory()){    
          System.out.println("删除目录失败"+dir+"目录不存在！");    
          return false;    
      }    
      boolean flag = true;    
      //删除文件夹下的所有文件(包括子目录)    
      File[] files = dirFile.listFiles();    
      for(int i=0;i<files.length;i++){    
          //删除子文件    
          if(files[i].isFile()){    
              flag = ( deleteFile(files[i].getAbsolutePath()) == 1 );    
              if(!flag){    
                  break;    
              }    
          }    
          //删除子目录    
          else{    
              flag = deleteFileDir(files[i].getAbsolutePath());    
              if(!flag){    
                  break;    
              }    
          }    
      }    
          
      if(!flag){    
          System.out.println("删除目录失败");    
          return false;    
      }    
          
      //删除当前目录    
      if(dirFile.delete()){    
          System.out.println("删除目录"+dir+"成功！");    
          return true;    
      }else{    
          System.out.println("删除目录"+dir+"失败！");    
          return false;    
      }    
  }    

  
  
  public static boolean flagMatchInRules(String flag, String[] rules) {
	  if ( rules == null || rules.length == 0 )  return false;
	  
	  flag = flag.trim();
	  for ( int i = 0 ; i < rules.length; i ++ )  {
	    if ( flag.equals(rules[i].trim()) )  
	       return true;
	  }	  
	  return false;
  }
  
//  public String getXmlProp(String fDir, String name) {
//		String prop = "";
//		try {
//			// 创建读入对象
//			SAXReader reader = new SAXReader();
//			// 创建document实例
//			Document doc = reader.read(fDir);
//			// 查找节点emp下的id属性
//			List listAttr = doc.selectNodes(name); // /Server/Service/Engine/Host/Context/@docBase"
//			Iterator itAttr = listAttr.iterator();
//			while (itAttr.hasNext()) {
//				Attribute attr = (Attribute) itAttr.next();
//				prop = attr.getValue();
//			}
//			return prop;
//
//		} catch (Exception ez) {
//			// TODO Auto-generated catch block
//			ez.printStackTrace();
//			return "";
//		}
//	}

   public String getParentDir(String dir, int pNum) {
		if (dir == null || dir.equals(""))
			return "";

		for (int i = 0; i < pNum; i++)
			dir = dir.substring(0, dir.lastIndexOf(java.io.File.separator));

		return dir;
	}
   
 
   //获取两个数组的交集
   public static String[] getArryRetainAll(String[] srcArry, String[] descArry) {
	      List list = new ArrayList(Arrays.asList(srcArry));   
		  list.retainAll(Arrays.asList(descArry));   //   list   中的就是交集了.	
//		  list.removeAll(Arrays.asList(b));
		  if ( list !=null && list.size() >0) {
			  String[] retArry = new String[list.size()];
			  int i =0;
			  for (Iterator iter = list.iterator(); iter.hasNext(); i++) {	
				  String ele = (String) iter.next();
				  retArry[i] = ele;
			  }
			  return retArry;
		  }		  
		  return null;		  
   }
   
   
// 获取两个数组的非交集,  srcArry 数组中不包含 descArry
   public static String[] getArryRemoveAll(String[] srcArry, String[] descArry) {
	      List list = new ArrayList(Arrays.asList(srcArry));   
//		  list.retainAll(Arrays.asList(descArry));   //   list   中的就是交集了.	
		  list.removeAll(Arrays.asList(descArry));
		  if ( list !=null && list.size() >0) {
			  String[] retArry = new String[list.size()];
			  int i =0;
			  for (Iterator iter = list.iterator(); iter.hasNext(); i++) {	
				  String ele = (String) iter.next();
				  retArry[i] = ele;
			  }
			  return retArry;
		  }		  
		  return null;		  
   }
  

  
  
  private static String valueOfZero(String str) { // Strng.valueOf(null)  结果是 "null"
	    String retStr = String.valueOf(str);
	    if ( retStr == null ||  retStr.trim().equals("") || retStr.equals("null") ) {
	      return "0";
	    } else {
	      return retStr;
	    }
   }  
  
  
  private static void getRetHtml(StringBuffer reHtml) {
	  reHtml.append("222");
  }
  
  
  /*
   * 两个时间段的相差值和 天数比较 小于 返回true
   */
  public boolean compareTime(Date sd, Date ed, int days){
		long subTimes = ed.getTime()-sd.getTime() ;
		
		long sourTime = days*3600*24*1000l;
		if (subTimes < sourTime) {
			return true;
		}
		return false;
  }

  
//	 字符拼串， spe为间隔符
	public static String getS(String msg, String str, String spe) {
		if ( msg == null || "".equals(msg) )  
			return str;
		else 
			return msg+ spe + str;		
	}
  
  
  public static String socketToServer(String server, int port, String body ) {
		PrintStream streamToServer = null;
		BufferedReader streamFromServer = null;
		Socket client = null;
		String retStr = null;
		try {
			client = new Socket(server, port);
			
			
			streamFromServer = new BufferedReader(new InputStreamReader((client
					.getInputStream())));
			streamToServer = new PrintStream(client.getOutputStream());
			streamToServer.println(body);
			
			retStr = streamFromServer.readLine();
//			for ( int i = 0 ; i < 10; i++ ) {
//				retStr = streamFromServer.readLine();
//				
//				System.out.println(i+" ------:" + retStr);
//			}
//			int i =0;
//			for ( String body : bodyList ) {
//				
//				System.out.println(i+":" + retStr);
//				i++;
//			}
			
			
//			char[] head=new char[1200]; // 头信息
//			streamFromServer.read(head);
//			String tmp = new String(head);
//			tmp=tmp.substring(6,5+6);
//			int i=Integer.parseInt(tmp);
//			char[] content=new char[10];
//			streamFromServer.read(content);
//			retStr=new String(content);
			
//			retStr = tmp;
		//	retStr = streamFromServer.readLine();
			
			return retStr;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connect to socket server ip:" + server
					+ " port:" + port + " cause Exception" + e);
			return null;
		} finally {
			if (streamToServer != null) {
				streamToServer.close();
			}
			if (streamFromServer != null) {
				try {
					streamFromServer.close();
				} catch (IOException ex) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException ex1) {
				}
			}
		}
	}
  
  
  
  
  
 
   /**
    * 将cookie封装到Map里面
    * @param request
    * @return
    */
   public static String getCookieByName(HttpServletRequest request, String name){  
       Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
       Cookie[] cookies = request.getCookies();
       if(null!=cookies){
           for(Cookie cookie : cookies){
        	   if ( name.equals(cookie.getName())) {
        		   return cookie.getValue();
        	   }
           }
       }
       return "";
   }
   
   public static void delCookieByName(HttpServletRequest request,HttpServletResponse response, String name){  
       Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
       Cookie[] cookies = request.getCookies();
       if(null!=cookies){
    	   try {
    		   for(Cookie cookie : cookies){
            	   if ( name.equals(cookie.getName())) {
            		   Cookie cookie_del = new Cookie(cookie.getName(), null);  
            		   cookie_del.setMaxAge(0);  
            		   cookie_del.setPath("/");//根据你创建cookie的路径进行填写      
                       response.addCookie(cookie_del);  
            		  
            		   return;
            	   }
               }
    	   } catch ( Exception e) {
    		   System.out.println("清空Cookies发生异常！");  
    	   }
           
       }
       
   }
   
 
   
   /**
    * 设置cookie
    * @param response
    * @param name  cookie名字
    * @param value cookie值
    * @param maxAge cookie生命周期  以秒为单位
    */
   public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
       Cookie cookie = new Cookie(name,value);
       cookie.setPath("/");
       if(maxAge>0)  cookie.setMaxAge(maxAge);
   
       response.addCookie(cookie);
       
   }
   
   
   /*
    * 对数据进行大小排序、大-->小  
    * mapKey 按照哪个字段排序    keyType排序字段的类型    sortType :ASC还是desc
    */
   private String __mapKey = "";
   private String __keyType = "";
   private String __sortType = "";
   public List<Map<String, String>> sortListData(List<Map<String, String>> list, String mapKey, String keyType, String sortType) {  
       System.out.println("对数据进行排序"); 
       __mapKey = mapKey;
       __keyType = keyType;
       __sortType = sortType;
       if ( list!= null && list.size() > 0 ) {  
    	   String key = mapKey;
           Comparator<Map<String, String>> mapComprator = new Comparator<Map<String, String>>() {        	   
               @Override  
               public int compare(Map<String, String> o1,  Map<String, String> o2) {  
                   // do compare.  
            	   int yesVal = 1;
            	   int noVal = -1;
            	   if ( "desc".equals(StringUtils.lowerCase(__sortType))) {
            		   yesVal = 1;
            		   noVal = -1;
            	   } else {
            		   yesVal = -1;
            		   noVal = 1;
            	   }
            	   
            	   int retVal = noVal;
            	   String o1_key = String.valueOf(o1.get(__mapKey));
            	   String o2_key = String.valueOf(o2.get(__mapKey));
            	   if ( "int".equals(__keyType) ) {
            		   if (Integer.valueOf(o1_key) < Integer.valueOf(o2_key)) {  
            			   retVal = yesVal;  
                       } 
            	   } else if ( "String".equals(__keyType) ) {
            		   if (String.valueOf(o1_key) .equals(String.valueOf(o2_key)) ) {  
            			   retVal = yesVal;  
                       } 
            	   } else if ( "float".equals(__keyType) ) {
            		   if (Float.valueOf(o1_key) < Float.valueOf(o2_key)) {  
            			   retVal = yesVal;  
                       } 
            	   }
            	   return retVal;
               }  
           };  
           Collections.sort(list, mapComprator);  
       } else {  
           new Exception("排序没有取到数据");  
       }  
       return list;  
   }  
   
   
   /*
    * 对数据进行大小排序、大-->小  
    * mapKey 按照哪个字段排序    keyType排序字段的类型    sortType :ASC还是desc
    */
   
   public List<Map> sortTheListData(List<Map> list, String mapKey, String keyType, String sortType) {  
       System.out.println("对数据进行排序"); 
       __mapKey = mapKey;
       __keyType = keyType;
       __sortType = sortType;
       if ( list!= null && list.size() > 0 ) {  
    	   String key = mapKey;
           Comparator<Map> mapComprator = new Comparator<Map>() {        	   
               @Override  
               public int compare(Map o1,  Map o2) {  
                   // do compare.  
            	   int yesVal = 1;
            	   int noVal = -1;
            	   if ( "desc".equals(StringUtils.lowerCase(__sortType))) {
            		   yesVal = 1;
            		   noVal = -1;
            	   } else {
            		   yesVal = -1;
            		   noVal = 1;
            	   }
            	   
            	   int retVal = noVal;
            	   String o1_key = String.valueOf(o1.get(__mapKey));
            	   String o2_key = String.valueOf(o2.get(__mapKey));
            	   if ( "int".equals(__keyType) ) {
            		   if (Integer.valueOf(o1_key) < Integer.valueOf(o2_key)) {  
            			   retVal = yesVal;  
                       } 
            	   } else if ( "String".equals(__keyType) ) {
            		   if (String.valueOf(o1_key) .equals(String.valueOf(o2_key)) ) {  
            			   retVal = yesVal;  
                       } 
            	   } else if ( "float".equals(__keyType) ) {
            		   if (Float.valueOf(o1_key) < Float.valueOf(o2_key)) {  
            			   retVal = yesVal;  
                       } 
            	   }
            	   return retVal;
               }  
           };  
           Collections.sort(list, mapComprator);  
       } else {  
           new Exception("排序没有取到数据");  
       }  
       return list;  
   }  
   

   
   
   //list排序测试
   public static void main(String[] args) {

		List list = new ArrayList();
	
		
		for(int i = 0; i<5; i++){
			Map map = new HashMap();
			String val = String.valueOf(i);
		//	list2.add(Integer.parseInt(val));
			map.put("name", val);
			list.add(map);
		}
		
		CommMethod  comm = new CommMethod();
		list = comm.sortTheListData(list, "name", "int", "desc");
		//map.put("data", list2);
		//List list = (List)map.get("data");

		//创建一个逆序的比较器
		Comparator r = Collections.reverseOrder();
		//通过逆序的比较器进行排序
//		Collections.sort(list,r);
//
//		for(int i = 0; i<list.size(); i++){
//			System.out.println(list.get(i));
//		}
//
//		//打乱顺序
//		Collections.shuffle(list);
		for(int i = 0; i<list.size(); i++){
			System.out.println(list.get(i));
		}
		
		
		//输出最大和最小的数
		System.out.println(Collections.max(list) + ":" + Collections.min(list));
	}


}
