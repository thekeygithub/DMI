package com.ebmi.std.share.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.jms.entity.ViewYyjbxx;
import com.ebmi.std.share.dao.ShareDao;

@Repository
public class ShareDaoImpl extends BaseDao implements ShareDao {

	@Override
	public List<ViewYyjbxx> queryYyjbxx() throws Exception {
		return getJdbcServiceMI().query(
				"select trim(yybh) yybh, trim(yymc) yymc, trim(yyjb) yyjb from VIEW_YYJBXX order by yybh", null,
				ViewYyjbxx.class);
	}

	@Override
	public ViewYyjbxx getYyjbxx(String yybh) throws Exception {
		return getJdbcServiceMI().queryForObject(
				"select trim(yybh) yybh, trim(yymc) yymc, trim(yyjb) yyjb from VIEW_YYJBXX where yybh=?",
				new Object[] { yybh }, ViewYyjbxx.class);
	}

}
