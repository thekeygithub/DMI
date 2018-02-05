package com.models.cloud.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.models.cloud.web.controller.impl.GetWebVerifyCodeListImpl;

/***
 * 登录获取验证码
 * @author enjuan.ren
 *
 */
@Controller
@RequestMapping("webCaptcha")
public class WebCaptchaController {
public static final String CAPTCHA_KEY = "_CAPTCHA_KEY_";
	
	@Resource(name="getWebVerifyCodeListImpl")
	private GetWebVerifyCodeListImpl verifyCodeListImpl;
	
	 private int width = 140;//定义图片的width
	  private int height = 40;//定义图片的height
	  private int codeCount = 4;//定义图片上显示验证码的个数
	  private int xx = 30;
	  private int fontHeight = 36;
	  private int codeY = 30;
	  @RequestMapping("webCode")
	  public void getCode(HttpServletRequest req, HttpServletResponse resp)
	      throws IOException {
		  
		  Map<String, Object> map=new HashMap<String,Object>();
		 String phone=req.getParameter("userCode");
		 String hardwareId=req.getSession().getId();
		 map.put("phone", phone);
		 map.put("hardwareId", hardwareId);
		 List<String> verCodeList = (List<String>) req.getSession().getAttribute("verCodeList");
		 int verCodeNo = (int) (req.getSession().getAttribute("verCodeNo")==null?0:req.getSession().getAttribute("verCodeNo"));
		 String code = "";
		 if(verCodeList!=null && verCodeList.size()>0 && verCodeNo<verCodeList.size()){
			 code = verCodeList.get(verCodeNo);
			 req.getSession().setAttribute("verCodeNo", verCodeNo+1);
			
			 
		 }else{
			 Map<String, Object> returnMap=verifyCodeListImpl.returnDataByMap(map, req);
			 List<String> verCodeList2 = (List<String>) req.getSession().getAttribute("verCodeList");
			 if (verCodeList2!=null && verCodeList2.size()>0) {
				
			 code = verCodeList2.get(0);
			 req.getSession().setAttribute("verCodeNo", 1);
			}
		 }
		
		 
	    // 定义图像buffer
	    BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
//		Graphics2D gd = buffImg.createGraphics();
	    //Graphics2D gd = (Graphics2D) buffImg.getGraphics();
	    Graphics gd = buffImg.getGraphics();
	    // 创建一个随机数生成器类
	    Random random = new Random();
	    // 将图像填充为白色
	    gd.setColor(Color.WHITE);
	    gd.fillRect(0, 0, width, height);

	    // 创建字体，字体的大小应该根据图片的高度来定。
	    Font font = new Font("Times New Roman", Font.PLAIN, fontHeight);
	    // 设置字体。
	    gd.setFont(font);

	    // 画边框。
	    gd.setColor(Color.BLACK);
	    gd.drawRect(0, 0, width - 1, height - 1);

	    // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
	    gd.setColor(Color.BLACK);
	    for (int i = 0; i < 10; i++) {
	      int x = random.nextInt(width);
	      int y = random.nextInt(height);
	      int xl = random.nextInt(12);
	      int yl = random.nextInt(12);
	      gd.drawLine(x, y, x + xl, y + yl);
	    }

	    // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
	    StringBuffer randomCode = new StringBuffer();
	    int red = 0, green = 0, blue = 0;

	    // 随机产生codeCount数字的验证码。
//	    for (int i = 0; i < codeCount; i++) {
	      // 得到随机产生的验证码数字。
//	      String code = String.valueOf(codeSequence[random.nextInt(36)]);
	      // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
//	      red = random.nextInt(255);
//	      green = random.nextInt(255);
//	      blue = random.nextInt(255);

	      // 用随机产生的颜色将验证码绘制到图像中。
//	      gd.setColor(new Color(red, green, blue));
//	      gd.drawString(code, (i + 1) * xx, codeY);

	      // 将产生的四个随机数组合在一起。
//	      randomCode.append(code);
//	    }
	    
	   // for(int x = 0; x <codeListCount ; x++){
	    	// 得到随机产生的验证码数字。
		     // String code = String.valueOf(code);
		      // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
		      red = random.nextInt(255);
		      green = random.nextInt(255);
		      blue = random.nextInt(255);

		      // 用随机产生的颜色将验证码绘制到图像中。
		      gd.setColor(new Color(red, green, blue));
		      gd.drawString(code, (0 + 1) * xx, codeY);

		      // 将产生的四个随机数组合在一起。
		      randomCode.append(code);
	   // }
	    
	    
	    // 将四位数字的验证码保存到Session中。
	    HttpSession session = req.getSession();
	    System.out.print(randomCode);
	    session.setAttribute(CAPTCHA_KEY, randomCode.toString());

	    // 禁止图像缓存。
	    resp.setHeader("Pragma", "no-cache");
	    resp.setHeader("Cache-Control", "no-cache");
	    resp.setDateHeader("Expires", 0);

	    resp.setContentType("image/jpeg");

	    // 将图像输出到Servlet输出流中。
	    ServletOutputStream sos = resp.getOutputStream();
	    ImageIO.write(buffImg, "jpeg", sos);
	    sos.close();
	  }
}
