package com.dhcc.ts.gh;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dhcc.modal.system.Opcode;
import com.dhcc.ts.database.DBManager_HIS;

public class CreateNum {
	private static final Logger logger = Logger.getLogger(CreateNum.class);
	/**
	 * @描述：根据tsconfig表中的CODETYPE类型下的key值生成编码
	 * @作者：SZ
	 * @时间：2014-12-3 下午05:09:53
	 * @return
	 */
	public static synchronized String getNum(String codetype) {
		try{
			Opcode model = queryOpcodeModel(codetype);
			String sequence = "";
			if(null!=model){
				if("0".equals(model.getType())){//固定编码
					if("1".equals(model.getDisplayvalue())){//固定编码前缀
						sequence += model.getCodevalue();
					}
					if("1".equals(model.getDisplayformat())){//日期编码
						String itemformat = model.getCodeformat();//model获得日期
						SimpleDateFormat sf = new SimpleDateFormat(itemformat);
						Date date = new Date();
						sequence += sf.format(date.getTime());
					}
					if("1".equals(model.getDisplayseq())){//序列号后缀
						if("1".equals(model.getDisplayformat())){//显示日期编码时
							String itemformat = model.getCodeformat();//model获得日期
							SimpleDateFormat sf = new SimpleDateFormat(itemformat);
							Date date = new Date();
							String today = sf.format(date.getTime());
							if(today.equals(model.getDisplaydate())){//和今天日期相同
								sequence += getLastFiveNum(model.getSeqvalue(), model.getSeqlength(), model.getId());
								updeptOpcodeModel(model.getSeqvalue(), model.getId(),today);//跟新字段
							}else{
								sequence += getLastFiveNum(1, model.getSeqlength(), model.getId());
								updeptOpcodeModel(1, model.getId(),today);//跟新字段
							}
						}else{
							sequence += getLastFiveNum(model.getSeqvalue(), model.getSeqlength(), model.getId());
							updeptOpcodeModel(model.getSeqvalue(), model.getId(),"");//跟新字段
						}
					}
				}else if("1".equals(model.getType())){//程序编码
					//待扩展
				}
			}else{
				logger.info("流水号方案：" + codetype + "没有配置方案项！");
			}
			return sequence;
		}catch (Exception e) {
			logger.error("获取序列号异常！", e);
		}
		return null;
	}
	
	/**
	 * @描述：根据tsconfig表中的CODETYPE类型下的key值获取其编码配置规则
	 * @作者：SZ
	 * @时间：2014-12-5 上午10:43:09
	 * @param codetype
	 * @return
	 */
	public static Opcode queryOpcodeModel(String codetype){
		DBManager_HIS dbm = new DBManager_HIS();
		Opcode model = null;
		try{
			String sql = "SELECT op.* " +
					" FROM TSOPCODE op " +
					" LEFT JOIN tsconfig tcf ON tcf.id = op.codetype " +
					" WHERE tcf.dtype='CODETYPE' AND tcf.dkey='"+codetype+"'";
			model = (Opcode) dbm.getObject(Opcode.class, sql);
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.close();
		}
		return model;
	}
	/**
	 * @描述：跟新下一次所取出来的初始值，当前值加一
	 * @作者：SZ
	 * @时间：2014-12-5 上午10:26:52
	 * @param seqvalue
	 * @param id
	 * @return
	 */
	public static boolean updeptOpcodeModel(int seqvalue,String id,String today){
		DBManager_HIS dbm = new DBManager_HIS();
		int i = -1;
		int value = seqvalue+1;
		try{
			String sql = "UPDATE TSOPCODE SET SEQVALUE='"+value+"',DISPLAYDATE='"+today+"' WHERE ID = '"+id+"'";
			dbm.addBatch(sql);
			dbm.executeBatch();
		}catch (Exception e) {
			// TODO: handle exception
			i=0;
		}finally{
			dbm.close();
			if(i==-1){
				return false;
			}
		}
		return true;
	}
	public static String getLastFiveNum(int num,int number,String id) {
		String codeNum = num + "";
		if(codeNum.length()>number){//如果序列值超过了最大长度，则从0开始
			number+=1;
			DBManager_HIS dbm = new DBManager_HIS();
			String sql = "UPDATE TSOPCODE SET seqlength='"+number+"' WHERE id = '"+id+"'";
			dbm.executeUpdate(sql);
			dbm.close();
		}
		for (int j = codeNum.length(); j < number; j++) {
			codeNum = "0" + codeNum;
		}
		return codeNum;
	}
	public static void main(String[] args) {
		System.out.println(getNum("SYS"));
	}
}
