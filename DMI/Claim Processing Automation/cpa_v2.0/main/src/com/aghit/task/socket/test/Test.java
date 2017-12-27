package com.aghit.task.socket.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.common.entity.CpaTask;
import com.aghit.task.deal.thread.CPATaskThread;
import com.aghit.task.deal.thread.HostStatusThread;
import com.aghit.task.manager.DrugAtcMgr;
import com.aghit.task.manager.DrugRelaMgr;
import com.aghit.task.manager.PatientMgr;
import com.aghit.task.manager.ProjectMgr;
import com.aghit.task.manager.TreeMgr;
import com.aghit.task.util.Constant;

class aa implements Runnable{

	public int a=123;
	@Override
	public void run() {
		
		System.out.println("start");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class bb extends Thread{
	public bb(ThreadGroup g,Runnable r){
		super(g,r);
		this.target = r;
	}
	
	public Runnable target;
}

public class Test {

	
	public static String getCodesString(String codes[]){
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < codes.length; i++) {
			sb.append(codes[i]+",");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {

		// new ProcessThread("1005","taskNo=58",null).run();

		// CpaTask task=new CpaTask();
		// task.setScenarioId(1);
		// task.setTaskStart(new Date());
		//		
		// new ProjectMgr(task);
		Set s=new HashSet();
		s.add("sdf");
		String[] sss=(String[]) s.toArray(new String[0]);
		System.out.println(sss);
		DrugRelaMgr mgr2=new DrugRelaMgr();
		mgr2.init();
		TreeMgr drugTree = new TreeMgr("S_DRUG_ATC_WEST", "ATC_ID", "FATHER_ID",-1,null);
		drugTree.init();
		// 诊断树
		TreeMgr diagTree = new TreeMgr("o_tree_diag t, o_diag d", "ID", "FATHER_ID", 25201,
				" t.id = d.diag_id and d.stop_sign = '0' ");
		diagTree.init();
//		String[] ss=drugTree.getChild(new String[]{"15877"});
//		System.out.println(getCodesString(ss));
//		String[] dl=mgr2.getDrugList(ss);
//		System.out.println(getCodesString(dl));
		
		DrugAtcMgr mgr=new DrugAtcMgr();
		mgr.init(diagTree, mgr2);
		System.out.println(mgr.findDiag(new String[]{"12396"}, 1).size());
		System.out.println(drugTree.getChild("12396").length);
		
		System.out.println(mgr.atc_drug.get("12396"));
		System.out.println(mgr.father_tree.get(12396));
		if(1==1){
			System.exit(0);
		}

		List<Patient> patients = new ArrayList<Patient>();
		for (int i = 2; i < 5; i++) {
			Patient p = new Patient();
			p.setSeqId(i);
			patients.add(p);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		CpaTask task = new CpaTask();
		task.setStartTimeSearch(sdf.parse("20120101"));
		task.setTaskStart(sdf.parse("20120101"));
		task.setTaskEnd(sdf.parse("20141201"));

		// Socket s=new Socket();
		// s.setKeepAlive(true);
		// s.setSoTimeout(100*1000);
		// s.connect(new InetSocketAddress("127.0.0.1",5566));
		//		
		// String data="1001"+"type=CPA|thread=24";
		// InputStream in=s.getInputStream();

		// s.getOutputStream().write(String.format("%08d",
		// data.getBytes().length).getBytes());
		// s.getOutputStream().write(data.getBytes());
		// s.getOutputStream().flush();
		// System.out.println("send:"+data);
		//		

		// int len= Integer.parseInt(new String(readByte(in,8)));
		// String rs=new String(readByte(in,len));
		// System.out.println(rs);
		//		
		// Thread.sleep(1000);
		// in=s.getInputStream();
		// while(true){
		// len= Integer.parseInt(new String(readByte(in,8)));
		// rs=new String(readByte(in,len));
		// System.out.println(rs);
		// }

		// System.out.println("send2!");
		// data="1004cpaTaskId=12648|taskType=CPA";
		// s.getOutputStream().write(String.format("%08d",
		// data.getBytes().length).getBytes());
		// s.getOutputStream().write(data.getBytes());
		// s.getOutputStream().flush();
		// System.out.println("send:"+data);
		//		
		// int len= Integer.parseInt(new String(readByte(in,8)));
		// String rs=new String(readByte(in,len));
		// System.out.println(rs);
		//		
		// System.out.println("read2!");
		// len= Integer.parseInt(new String(readByte(in,8)));
		// rs=new String(readByte(in,len));
		// System.out.println(rs);
		//		
		// System.out.println("read3!");
		// len= Integer.parseInt(new String(readByte(in,8)));
		// rs=new String(readByte(in,len));
		// System.out.println(rs);
		//		
		// System.exit(0);

	}

	public static byte[] readByte(InputStream is, int len) throws IOException,
			InterruptedException {
		byte[] t = new byte[len];
		int offset = 0;
		while (offset != len) {
			offset = offset + is.read(t, offset, len - offset);
			Thread.sleep(10);
		}
		return t;
	}
}
