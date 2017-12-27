package com.dhcc.ts.fragment.library;

import org.springframework.jdbc.core.JdbcTemplate;

import com.dhcc.ts.database.DBConnectionManager_LIBRARY;

public class JdbcTemplateTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		JdbcTemplate template = new JdbcTemplate();
//		template.setDataSource(DBConnectionManager_LIBRARY.getDataSource());
		String str = "ab''c";
		System.out.println(str.replaceAll("\\'", "\\\\'"));
	}

}
