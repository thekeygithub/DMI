package com.pay.cloud.util.hint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @Description: 生成错误提示中文信息Properties文件
 * @ClassName: PrintComments 
 * @author: zhengping.hu
 * @date: 2016年3月14日 下午2:49:55
 */
public class PrintComments {

    public static void main(String[] args) {
    	
//    	String enumPath="E:/ebao/PaymentProd/code/ebaonet-cloud/ebaonet-pay/src/main/java/com/ebaonet/cloud/util/hint/Hint.java";
//    	String propertiesfilePath="E:/ebao/PaymentProd/code/ebaonet-cloud/ebaonet-pay/src/main/resources/Hint.properties";
//    	String txtfilePath="E:/ebao/PaymentProd/doc/V1.0/02 软件开发/0202 设计文档/020207 返回码对应表/返回码信息.txt";
//    	PrintComments.writeProperties(enumPath, propertiesfilePath);
//    	PrintComments.writeTxt(enumPath, txtfilePath);

        String enumPath="E:\\IdeaProjects\\ebaonet-cloud-trunk\\ebaonet-pay\\src\\main\\java\\com\\ebaonet\\cloud\\util\\hint/Hint.java";
        String propertiesfilePath="E:\\IdeaProjects\\ebaonet-cloud-trunk\\ebaonet-pay\\src\\main\\resources/Hint.properties";
        String txtfilePath="F:\\SvnCode\\docs\\EbaopaymentPlatform\\V1.0\\02 软件开发\\0202 设计文档\\020207 返回码对应表/返回码信息.txt";
        PrintComments.writeProperties(enumPath, propertiesfilePath);
        PrintComments.writeTxt(enumPath, txtfilePath);
    }
    
	private static void writeProperties(String enumPath,String filePath){
		System.out.println("生成Hint.Properties开始");
		
		try {
			LinkedProperties pps = new LinkedProperties();
			InputStream in = new FileInputStream(filePath);
			pps.load(in);
			pps.clear();
			
            FileReader freader = new FileReader(enumPath);
            BufferedReader breader = new BufferedReader(freader);
            StringBuilder sb = new StringBuilder();
            try {
                String temp = "";
                /**
                 * 读取文件内容，并将读取的每一行后都不加\n
                 * 其目的是为了在解析双反斜杠（//）注释时做注释中止符
                 */
                while((temp=breader.readLine())!= null)
                {
                    sb.append(temp);
                    sb.append('\n');
                }
                String src = sb.toString();
                /**
                 * 1、做/* 注释的正则匹配通过渐进法做注释的正则匹配，因为/*注释总是成对出现  * 当匹配到一个/*时总会在接下来的内容中会首先匹配到"*\\/",
                 * 因此在获取对应的"*\\/"注释时只需要从当前匹配的/*开始即可，
                 * 下一次匹配时只需要从上一次匹配的结尾开始即可（这样对于大文本可以节省匹配效率）—— 这就是渐进匹配法
                 * */
                Pattern leftpattern0 = Pattern.compile("\\([0-9]");
                Matcher leftmatcher0 = leftpattern0.matcher(src);
                Pattern rightpattern0 = Pattern.compile("[0-9]\\)");
                Matcher rightmatcher0 = rightpattern0.matcher(src);
                
                Pattern leftpattern = Pattern.compile("/\\*");
                Matcher leftmatcher = leftpattern.matcher(src);
                Pattern rightpattern = Pattern.compile("\\*/");
                Matcher rightmatcher = rightpattern.matcher(src);
                
                int begin0 = 0,begin1 = 0;
                String key="",value="";
                while(leftmatcher0.find(begin0)&&leftmatcher.find(begin1))
                {
                    rightmatcher0.find(leftmatcher0.start());
                    rightmatcher.find(leftmatcher.start());
                    
                    key=src.substring(leftmatcher0.start()+1, rightmatcher0.end()-1).replace(" ", "");
                    value=src.substring(leftmatcher.start()+3, rightmatcher.end()-2).replaceAll("(\\s|[*])", "");
                    pps.setProperty(key,value);
                    
                    begin0 = rightmatcher0.end();
                    begin1 = rightmatcher.end();
                }
                pps.store(new FileOutputStream(filePath), "错误提示中文信息");
//                /**
//                 * 2、对//注释进行匹配（渐进匹配法）
//                 * 匹配方法是 // 总是与 \n 成对出现
//                 * */
//                begin = 0;
//                Pattern leftpattern1 = Pattern.compile("//");
//                Matcher leftmatcher1 = leftpattern1.matcher(src);
//                Pattern rightpattern1 = Pattern.compile("\n");
//                Matcher rightmatcher1 = rightpattern1.matcher(src);
//                sb = new StringBuilder();
//                while(leftmatcher1.find(begin))
//                {
//                    rightmatcher1.find(leftmatcher1.start());
//                    sb.append(src.substring(leftmatcher1.start(), rightmatcher1.end()));
//                    begin = rightmatcher1.end();
//                }
//                System.out.println(sb.toString());
                System.out.println("生成Hint.Properties结束");
            } catch (IOException e) {
                System.out.println("文件读取失败");
            }finally
            {
                breader.close();
                freader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        }catch(IOException e)
        {
            System.out.println("文件读取失败");
        }
	}

	private static void writeTxt(String enumPath,String filePath){
		System.out.println("生成返回码信息.txt开始");
		
		try {
			File f = new File(filePath);
            if (f.exists()) {
            	//文件存在，删除
                f.delete();
            }
            f.createNewFile();//新创建
            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            
            FileReader freader = new FileReader(enumPath);
            BufferedReader breader = new BufferedReader(freader);
            StringBuilder sb = new StringBuilder();
            try {
                String temp = "";
                /**
                 * 读取文件内容，并将读取的每一行后都不加\n
                 * 其目的是为了在解析双反斜杠（//）注释时做注释中止符
                 */
                while((temp=breader.readLine())!= null)
                {
                    sb.append(temp);
                    sb.append('\n');
                }
                String src = sb.toString();
                /**
                 * 1、做/* 注释的正则匹配通过渐进法做注释的正则匹配，因为/*注释总是成对出现  * 当匹配到一个/*时总会在接下来的内容中会首先匹配到"*\\/",
                 * 因此在获取对应的"*\\/"注释时只需要从当前匹配的/*开始即可，
                 * 下一次匹配时只需要从上一次匹配的结尾开始即可（这样对于大文本可以节省匹配效率）—— 这就是渐进匹配法
                 * */
                Pattern leftpattern0 = Pattern.compile("\\([0-9]");
                Matcher leftmatcher0 = leftpattern0.matcher(src);
                Pattern rightpattern0 = Pattern.compile("[0-9]\\)");
                Matcher rightmatcher0 = rightpattern0.matcher(src);
                
                Pattern leftpattern = Pattern.compile("/\\*");
                Matcher leftmatcher = leftpattern.matcher(src);
                Pattern rightpattern = Pattern.compile("\\*/");
                Matcher rightmatcher = rightpattern.matcher(src);
                
                int begin0 = 0,begin1 = 0;
                String key="",value="";
                while(leftmatcher0.find(begin0)&&leftmatcher.find(begin1))
                {
                    rightmatcher0.find(leftmatcher0.start());
                    rightmatcher.find(leftmatcher.start());
                    
//                    pps.setProperty(src.substring(leftmatcher0.start()+1, rightmatcher0.end()-1).replace(" ", ""),src.substring(leftmatcher.start()+3, rightmatcher.end()-2).replace(" ", "").replaceAll("(\r\n|\r|\n|\n\r|[*])", ""));
                    key=src.substring(leftmatcher0.start()+1, rightmatcher0.end()-1).replace(" ", "");
                    value=src.substring(leftmatcher.start()+3, rightmatcher.end()-2).replaceAll("(\\s|[*])", "");
                    output.write(key+" = "+value+"\r\n");
                    
                    begin0 = rightmatcher0.end();
                    begin1 = rightmatcher.end();
                }
//                /**
//                 * 2、对//注释进行匹配（渐进匹配法）
//                 * 匹配方法是 // 总是与 \n 成对出现
//                 * */
//                begin = 0;
//                Pattern leftpattern1 = Pattern.compile("//");
//                Matcher leftmatcher1 = leftpattern1.matcher(src);
//                Pattern rightpattern1 = Pattern.compile("\n");
//                Matcher rightmatcher1 = rightpattern1.matcher(src);
//                sb = new StringBuilder();
//                while(leftmatcher1.find(begin))
//                {
//                    rightmatcher1.find(leftmatcher1.start());
//                    sb.append(src.substring(leftmatcher1.start(), rightmatcher1.end()));
//                    begin = rightmatcher1.end();
//                }
//                System.out.println(sb.toString());
                System.out.println("生成返回码信息.txt结束");
            } catch (IOException e) {
                System.out.println("文件读取失败");
            }finally
            {
                breader.close();
                freader.close();
                output.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        }catch(IOException e)
        {
            System.out.println("文件读取失败");
        }
	}
	
}
