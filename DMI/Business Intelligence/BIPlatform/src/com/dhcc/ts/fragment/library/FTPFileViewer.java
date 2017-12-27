package com.dhcc.ts.fragment.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
/**
 * 列出FTP服务器上指定目录下面的所有文件
 * @author BenZhou mailto:zhouzb@qq.com
 * 原文地址:http://zhouzaibao.iteye.com/blog/362866 
 */
public class FTPFileViewer {
	private static Logger logger = Logger.getLogger(FTPFileViewer.class);
	public FTPClient ftp;
	
	/**
	 * 重载构造函数
	 * @param isPrintCommmand 是否打印与FTPServer的交互命令
	 */
	public FTPFileViewer(boolean isPrintCommmand){
		ftp = new FTPClient();
		if(isPrintCommmand){
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
	}
	
	/**
	 * 登陆FTP服务器
	 * @param host FTPServer IP地址
	 * @param port FTPServer 端口
	 * @param username FTPServer 登陆用户名
	 * @param password FTPServer 登陆密码
	 * @return 是否登录成功
	 * @throws IOException
	 */
	public boolean login(String host,int port,String username,String password) throws IOException{
		this.ftp.connect(host,port);
		
		boolean flag = false;
		if(FTPReply.isPositiveCompletion(this.ftp.getReplyCode())){
			if(this.ftp.login(username, password)){
//				this.ftp.setControlEncoding("GBK");
				return true;
			}
		}
		if(this.ftp.isConnected()) this.ftp.disconnect();
		return flag;
	}
	
	/**
	 * 关闭数据链接
	 * @throws IOException
	 */
	public void disConnection() throws IOException{
		if(this.ftp.isConnected()){
			this.ftp.disconnect();
		}
	}
	
	/**
	 * 递归遍历出目录下面所有文件
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束
	 * @throws IOException
	 */
	public List<String> listFile(String pathName) throws IOException{
		List<String> result = new ArrayList<String>();
		if(pathName.startsWith("/")&&pathName.endsWith("/")){
			String directory = pathName;
			//更换目录到当前目录
			this.ftp.changeWorkingDirectory(directory);
			FTPFile[] files = this.ftp.listFiles();
			for(FTPFile file:files){
				if(file.isFile()){
//					arFiles.add(directory+file.getName());
					result.add(new String(file.getName().getBytes("ISO-8859-1"),"GB2312"));
				}else if(file.isDirectory()){
					listFile(directory+file.getName()+"/");
				}
			}
		}
		return result;
	}
	
	/**
	 * 递归遍历目录下面指定的文件名
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束
	 * @param ext 文件的扩展名
	 * @throws IOException 
	 */
	public List<String> listFile(String pathName,String ext) throws IOException{
		List<String> result = new ArrayList<String>();
		if(pathName.startsWith("/")&&pathName.endsWith("/")){
			String directory = pathName;
			//更换目录到当前目录
			this.ftp.changeWorkingDirectory(directory);
			FTPFile[] files = this.ftp.listFiles();
			for(FTPFile file:files){
				if(file.isFile()){
					if(file.getName().endsWith(ext)){
						result.add(directory+file.getName());
					}
				}else if(file.isDirectory()){
					listFile(directory+file.getName()+"/",ext);
				}
			}
		}
		return result;
	}
	public static void main(String[] args) throws IOException {
		FTPFileViewer f = new FTPFileViewer(true);
		
		if(f.login("10.10.40.220", 21, "ftper", "ftper")){
			List<String> list = f.listFile("/library/1/");
			f.disConnection();
			Iterator<String> it = list.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
		}
		
		
	}
}