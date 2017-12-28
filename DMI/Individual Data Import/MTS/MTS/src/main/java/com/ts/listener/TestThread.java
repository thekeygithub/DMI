
package com.ts.listener;

import java.sql.ResultSet;

public class TestThread implements Runnable {

	private ResultSet rs = null;

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.rs=null;
	}

	public static void main(String[] args) {
		
	}

}
