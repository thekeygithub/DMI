package com.aghit.task.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.aghit.task.common.entity.CpaSubTask;
import com.aghit.task.deal.thread.CPATaskThread;
import com.aghit.task.deal.thread.HostStatusThread;
import com.aghit.task.manager.TaskMgr;

public class DealService implements IService,Runnable{

	private static DealService instance = new DealService(); // 交易实例

	private Socket s = null;

	public String ip = "127.0.0.1";

	public int port = 5566;

	private int thread = 4;

	private OutputStream out = null;

	public CopyOnWriteArrayList<CPATaskThread> threadList = new CopyOnWriteArrayList<CPATaskThread>();

	private Logger log = Logger.getLogger(DealService.class);

	@Override
	public void start() {

//		 ip="192.168.2.232";
//		 port = 15566;
//		 thread =10;
		ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(thread + 5);
	 
		InputStream in = null;

		while (true) {
			try {
				s = new Socket();
				s.setKeepAlive(true);
				s.setSoTimeout(Integer.MAX_VALUE);
				log.info("正在连接到" + ip + ":" + port + "...");
				s.connect(new InetSocketAddress(ip, port));
				log.info("连接到" + ip + ":" + port + "成功!");
				in = s.getInputStream();
				out = s.getOutputStream();

				String data = "1001" + "type=CPA|thread=" + thread;
				this.write(0, data);

				byte[] len_bs = readByte(in, 8);
				int len = Integer.parseInt(new String(len_bs));
				String rs = new String(readByte(in, len));
				log.info("向调度中心(" + s.getRemoteSocketAddress() + ")注册成功!,本机:"
						+ s.getLocalSocketAddress());

				String dealId;
				String msg;
				int threadNo = 0;
				boolean first = true;
				while ((len_bs = this.readByte(in, 8)) != null) {
					len = Integer.parseInt(new String(len_bs));
					rs = new String(readByte(in, len));
					dealId = rs.substring(0, 4);
					msg = rs.substring(4, rs.length());
					log.info("收到交易ID:" + dealId + ",报文:" + msg);

					// 心跳报文的处理,直接返回
					if ("1003".equals(dealId)) {
						data = "1003" + "test=" + System.currentTimeMillis();
						this.write(0, data);
					}
					// 任务下发接口的处理
					else if ("1005".equals(dealId)) {
//						if (first) {
//							first = false;
//						this.initProject(msg);
//						}else{
//							//新的任务第一次发起时会有此参数
//							if(msg.indexOf("first=true")>-1){
//								this.initProject(msg);
//							}
//						}
//						CPATaskThread thread=new CPATaskThread(dealId, msg);
//						this.initProject(msg,thread);
//						service.execute(thread);
//						this.threadList.add(thread);
//						for(CPATaskThread pt:this.threadList){
//							if(pt.over){
//								this.threadList.remove(pt);
//							}
//						}
					}
					//强制中止所有的任务
					else if ("1006".equals(dealId)) {
						service.shutdownNow();
						service = (ThreadPoolExecutor) Executors.newFixedThreadPool(thread + 5);
					}
					// 主机状态查询
					else if ("1002".equals(dealId)) {
						service.execute(new HostStatusThread());
//						this.write(0, "1002cpu=0.0|usedMemory=4333|totalMemory=6038|osName=LIANGJL-PC(Windows 7)");
					}
					
					//任务进度查询
					if ("1011".equals(dealId)) {
						
						StringBuffer buf=new StringBuffer();
						buf.append("1011type=CPA|result=");
						
						for(CPATaskThread pt:this.threadList){
							if(!pt.over){
								if(pt!=null && pt.task!=null){
									buf.append(pt.task.getCpaAtId()+":"+pt.count.get()+";");
								}
							}else{
								this.threadList.remove(pt);
							}
						}
						
						this.write(0, buf.toString());
					}
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				if (s != null) {
					try {
						s.close();
					} catch (IOException e1) {
						log.error("关闭socket,"+e1.getMessage());
					}
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.error("sleep失败,"+e.getMessage());
			}
		}
	}

	/**
	 * 初始化方案
	 * 
	 * @param msg
	 */
	private void initProject(String msg,CPATaskThread thread) {
		log.info("正在初始化方案,加载规则!");
		// 1.查询task任务
		String[] ms;
		if(msg.contains("|"))
			ms = msg.split("\\|")[0].split("=");
		else
			ms = msg.split("=");
		String taskNo = ms[1].trim();
		CpaSubTask task = null;
		try {
			task = TaskMgr.getInstance().getCpaTask(Long.parseLong(taskNo));
		} catch (Exception e1) {
			log.error("查询CPA任务失败," + e1.getMessage());
			return;
		}

		long logId = task.getLogId();
		try {
			thread.init(logId);
		} catch (Exception e) {
			log.error("初始化规则失败," + e.getMessage());
			return;
		}
	}

	@Override
	public boolean init(Object param) {

		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			log.error("打开配置文件config.properties失败,"+e.getMessage());
		}
		ip = p.getProperty("ip");
		port = Integer.parseInt(p.getProperty("port"));
		thread = Integer.parseInt(p.getProperty("maxThread"));

		return true;
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	private DealService() {
		// 初始化通信IP及端口
		this.init(null);
	}

	public static DealService getInstance() {
		return instance;
	}

	/**
	 * 统一写方法
	 * @param threadNo  线程序列号，0表示主线程
	 * @param rs 数据
	 * @throws IOException
	 */
	public synchronized void write(int threadNo,String rs) throws IOException {
		log.info(threadNo + ",返回报文:" + rs);
		out.write(String.format("%08d", rs.getBytes().length).getBytes());
		out.write(rs.getBytes());
		out.flush();
	}

	public static byte[] readByte(InputStream is, int len) throws IOException,
			InterruptedException {
		byte[] t = new byte[len];
		int offset = 0;
		while (offset != len) {
			offset = offset + is.read(t, offset, len - offset);
			if(offset < 0 ){
				throw new IOException("读取数据超时,offset="+offset+",期望读取取len="+len);
			}
		}
		return t;
	}

	@Override
	public void run() {
		this.start();
	}

}
