package com.ebmi.std.bankauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ebmi.std.common.controller.BaseController;
import com.ebmi.std.common.util.Propertie;

/**
 * @Description:   中国银行身份认证接口调用类
 * @ClassName:  身份认证
 * @author:  xsl
 * @date: 2017.12.12
 */
@Controller
@RequestMapping(value = "/bankAuth")
public class BankAuthController extends BaseController {
	private static final  int length = 5;//请求体的长度5位
	private static final  String encoding = "gbk";
//	<?xml version=\"1.0\" encoding=\""+encoding+"\"?> //暂时不加xml报文头
	private static final  String body =	"<UTILITY_PAYMENT><CONST_HEAD><AGENT_CODE>SBCX0000</AGENT_CODE><TRAN_CODE>b05033</TRAN_CODE >"+
			"<FRONT_TRACENO>FRONT_TRACENO_VALUE</FRONT_TRACENO><FRONT_DATE>FRONT_DATE_VALUE</FRONT_DATE><FRONT_TIME>FRONT_TIME_VALUE</FRONT_TIME>"+
			"</CONST_HEAD><DATA_AREA><CARDNO>CARDNO_VALUE</CARDNO><CERTYPE>CERTYPE_VALUE</CERTYPE ><PASSNO>PASSNO_VALUE</PASSNO>"+
			"<NAME>NAME_VALUE</NAME></DATA_AREA></UTILITY_PAYMENT>";
	/**
	 * 中国银行身份认证访问地址
	 */
	private static String IDENTITY_URL = null;
	private static Integer IDENTITY_PORT = null;
	
	@RequestMapping(value = "/chinaBankAuth", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> validateSocialsecurityInfo(
			String NAME, String CARDNO, String CERTYPE, String PASSNO, 
			String FRONT_TRACENO, String FRONT_DATE, String FRONT_TIME) {
		
		logger.info("bank auth start");
		
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			//查询配置文件
			if(StringUtils.isBlank(IDENTITY_URL)){
				IDENTITY_URL=Propertie.APPLICATION.value("identity_url"); 
			}
			if(IDENTITY_PORT==null){
				IDENTITY_PORT=Integer.valueOf(Propertie.APPLICATION.value("identity_port")); 
			}
			
			// 要对应服务器端的端口号
			Socket socket = new Socket(IDENTITY_URL, IDENTITY_PORT);
			logger.info("socket = " + socket);
			OutputStream output=null;
			InputStream input =null;
			String BACK_CODE = null;
			int len = 0;
			try {
				//将xml报文替换成正确的数据
				String authBody = BankAuthController.body.replaceAll("NAME_VALUE", NAME).replaceAll("CARDNO_VALUE", CARDNO)
					.replaceAll("CERTYPE_VALUE", CERTYPE).replaceAll("PASSNO_VALUE", PASSNO)
					.replaceAll("FRONT_TRACENO_VALUE", FRONT_TRACENO).replaceAll("FRONT_DATE_VALUE", FRONT_DATE)
					.replaceAll("FRONT_TIME_VALUE", FRONT_TIME);
				
				//设置IO句柄
				output = socket.getOutputStream();  
				//写数据发送报文  
		        output.write(getByteStream(authBody));  
		        
		        //获得服务端返回的数据  
		        input = socket.getInputStream();  
				//读取返回数据的5位请求体长度
				byte[] bodyLen = streamToBytes(input,length);  
				logger.info("return head length byte info："+convertByte(bodyLen));
				String lenString = new String(bodyLen,Charset.forName(encoding));  
			    len = Integer.valueOf(lenString); 
			    //根据长度读取返回报文
			    byte[] receivebody = streamToBytes(input, len);
			    logger.info("return head body byte info："+convertByte(receivebody));
			    BACK_CODE = new String(receivebody,Charset.forName(encoding));
				resultMap.put("result", BACK_CODE);
			} catch (Exception e) {
				resultMap.put("result", e.toString());
				e.printStackTrace();
			}finally{
				logger.info("接收到的报文长度："+len+"。返回信息体:"+BACK_CODE);
				logger.info("close the Client socket and the io.");
				try {
					if(output!=null){
						output.close();
					}
					if(input!=null){
						input.close();
					}
					socket.close();
				} catch (Exception e) {
					logger.info("close the Client socket error.");
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			resultMap.put("result", e.toString());
			e.printStackTrace();
		}
		logger.info("bank auth end");
		return resultMap;
	}
	
	private String convertByte(byte[] bs) {
		StringBuffer s = new StringBuffer();
		if(bs!=null) {
			for(byte b:bs) {
				s.append(b).append(";");
			}
		}
		return s.toString();
	}

	/**
	 * 报文长度 = 5位10进制长度（不包含本身的长度）+ 请求体body
	 * @return
	 * @throws Exception
	 */
	private byte[] getByteStream(String xml) throws Exception {
		//获得body的字节数组  
		byte[] bodyBytes = xml.getBytes(Charset.forName(encoding));
		//请求体长度
        int bodyLength = bodyBytes.length;
        //报文长度  = 5位10进制长度数字+请求体长度 
        int socketLength = length + bodyLength;  
        byte [] soc = new byte[socketLength];  
        //添加校验数据  
        int index = 0;  
        //添加5位报文长度
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(length);
        numberFormat.setGroupingUsed(false);
        byte [] num = numberFormat.format(bodyLength).getBytes(Charset.forName(encoding));
        for(;index<length;index++){
            soc[index]= num[index];
        }
        //添加body内容  
        for(int i = 0;i<bodyLength;i++){
            soc[index++] = bodyBytes[i];
        }
        return soc;
	}
	
	private byte[] streamToBytes(InputStream inputStream,int len){  
        /** 
         * inputStream.read(要复制到得字节数组,起始位置下标,要复制的长度) 
         * 该方法读取后input的下标会自动的后移，下次读取的时候还是从上次读取后移动到的下标开始读取 
         * 所以每次读取后就不需要在制定起始的下标了 
         */  
        byte [] bytes= new byte[len];  
        try {  
            inputStream.read(bytes, 0, len);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return bytes;  
    }  
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		byte[] bs = "01等".getBytes(Charset.forName(encoding));
		StringBuffer s = new StringBuffer();
		if(bs!=null) {
			for(byte b:bs) {
				s.append(b).append(";");
			}
		}
		System.out.println(s.toString());
	}
	
}
