package com.ebmi.std.personal.dao.impl;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;

import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.common.util.ConvertUtils;
import com.ebmi.std.personal.dao.PersonalDao;
import com.ebmi.std.util.EncodingUtils;

@Repository("personalDao")
public class PersonalDaoImpl extends BaseDao implements PersonalDao {

	@Override
	public String getMIUserByUserIdNo(String id_no) throws Exception {
		Map<String, Object> map = getJdbcServiceMI()
				.queryForMap("select trim(grbh) grbh from view_grjbxx where sfzh=?", new Object[] { id_no });
		return ConvertUtils.mapHasKeyValue(map, "grbh");
	}

	@Override
	public String getMIUserByUserSSCard(String sb_no) throws Exception {
		Map<String, Object> map = getJdbcServiceMI()
				.queryForMap("select trim(grbh) grbh from view_grjbxx where ickh=?", new Object[] { sb_no });
		return ConvertUtils.mapHasKeyValue(map, "grbh");
	}

	@Override
	public String getMIUser(String p_mi_id) throws Exception {
		Map<String, Object> map = getJdbcServiceMI().queryForMap(
				"select trim(xm) p_name, trim(ickh) si_card_no, trim(sfzh) p_cert_no from view_grjbxx where grbh = ?",
				new Object[] { p_mi_id });
		StringBuffer sb = new StringBuffer();
		sb.append("{\"p_name\":\"").append(EncodingUtils.transCP1252ToGBK(ConvertUtils.mapHasKeyValue(map, "P_NAME")))
				.append("\",");
		sb.append("\"p_cert_no\":\"").append(ConvertUtils.mapHasKeyValue(map, "P_CERT_NO")).append("\",");
		sb.append("\"si_card_no\":\"").append(ConvertUtils.mapHasKeyValue(map, "SI_CARD_NO")).append("\"");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String validateSocialsecurityInfo(String name, String id_no, String sb_no, String sb_pass) throws Exception {

		String name1 = EncodingUtils.transGBKToCP1252(name);
		Map<String, Object> map = getJdbcServiceMI().queryForMap(
				"select grbh from view_grjbxx where xm=? and sfzh=? and ickh=? and ickmm=?",
				new String[] { name1, id_no, sb_no, sb_pass });
		if (MapUtils.isEmpty(map))
			return "";
		else
			return ConvertUtils.mapHasKeyValue(map, "grbh");
	}
}
