package com.ebmi.std.share.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ebmi.jms.entity.ViewYyjbxx;
import com.ebmi.std.share.dao.ShareDao;
import com.ebmi.std.share.service.ShareService;

/**
 * 共享信息
 * 
 * @author xiangfeng.guan
 */
@Service
public class ShareServiceImpl implements ShareService {
	protected final Logger logger = Logger.getLogger(ShareService.class);

	@Resource
	private ShareDao shareDao;

	public List<ViewYyjbxx> queryYyjbxx() throws Exception {
		return shareDao.queryYyjbxx();
	}

	public ViewYyjbxx getYyjbxx(String yybh) throws Exception {
		return shareDao.getYyjbxx(yybh);
	}
}
