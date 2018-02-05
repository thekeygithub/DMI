package com.models.cloud.controller;

import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.models.cloud.common.dict.entity.DimImageWithBLOBs;
import com.models.cloud.common.dict.service.DimImageService;

/**
 * 静态资源入口
 * @author qingsong.li
 *
 */
@Controller
@RequestMapping(value = "/static")
public class StaticResourceControler {

	private static final Logger logger = Logger.getLogger(StaticResourceControler.class);
	 
	@Resource(name="dimImageServiceImpl")
	private DimImageService dimImageServiceImpl;
	
	@RequestMapping(value = "/image/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	public void image(@PathVariable String id, HttpServletResponse response) {
		try {
			OutputStream os = response.getOutputStream();
			DimImageWithBLOBs dimImage = dimImageServiceImpl.selectDimImage(id);
			if (dimImage != null && dimImage.getContent() != null) {
				if (StringUtils.isNotBlank(dimImage.getFileType())) {
					response.setContentType("image/" + dimImage.getFileType());
				} else {
					response.setContentType("image/jpeg");
				}
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setIntHeader("Expires", -1);
				os.write(dimImage.getContent());
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/imageThumb/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	public void imageThumb(@PathVariable String id, HttpServletResponse response) {
		try {
			OutputStream os = response.getOutputStream();
			DimImageWithBLOBs dimImage = dimImageServiceImpl.selectDimImage(id);
			if (dimImage != null && dimImage.getThumb() != null) {
				if (StringUtils.isNotBlank(dimImage.getFileType())) {
					response.setContentType("image/" + dimImage.getFileType());
				} else {
					response.setContentType("image/jpeg");
				}
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setIntHeader("Expires", -1);
				os.write(dimImage.getThumb());
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
