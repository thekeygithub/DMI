package com.aghit.task.manager;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class TreeMgr extends Manager {

	private Logger log = Logger.getLogger(TreeMgr.class);
	Map<Long, List<Long>> tree = new HashMap<Long, List<Long>>();

	private String tableName;
	private String idName;
	private String fatherIdName;
	private long treeId;
	private String condition;

	/**
	 * 根据一个表构建其树结构
	 * @param tableName 表名
	 * @param idName  id字段名
	 * @param fatherIdName  父级字段名
	 * @param treeId  treeId = -1 代表不加入此条件
	 * @param condition  条件
	 */
	public TreeMgr(String tableName, String idName, String fatherIdName,
			long treeId,String condition) {
		this.tableName = tableName;
		this.idName = idName;
		this.fatherIdName = fatherIdName;
		this.treeId = treeId;
		this.condition = condition;
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

		tree.clear();

		String sql = "select * from " + this.tableName+" where 1=1 ";
		if (this.treeId != -1) {
			sql = sql + "and tree_id=" + this.treeId;
		}
		if(this.condition!=null){
			sql = sql + " and "+ this.condition;
		}
		SqlRowSet s = this.getJdbcService().findByQuery(sql, null);

		while (s.next()) {
			long father = s.getLong(fatherIdName);
			long id = s.getLong(idName);
			List<Long> temp = tree.get(father);
			if (temp == null) {
				temp = new ArrayList<Long>();
			}
			temp.add(id);
			this.tree.put(father, temp);
			
			List<Long> id_temp = tree.get(id);
			if(id_temp==null){
				id_temp=new ArrayList<Long>(0);
				this.tree.put(id, id_temp);
			}
		}
		log.info("search "+this.tableName+" tree size = " + tree.size());
	}

	/**
	 * 根据传入的父id得到其下所有的子id
	 * 
	 * @param fatherId
	 *            父ID
	 * @return 如无子id，返回一个空list
	 */
	private List<Long> getChild(long fatherId) {

		List<Long> list = new ArrayList<Long>(1);

		List<Long> temp = tree.get(fatherId);
		if (temp != null) {
			list.addAll(temp);
			for (int i = 0; i < temp.size(); i++) {
				long id = temp.get(i);
				list.addAll(this.getChild(id));
			}
		}
		return list;
	}

	/**
	 * 批量找到其子id
	 * 
	 * @param cs
	 * @return
	 */
	public String[] getChild(String cs) {

		return this.getChild(new String[] { cs });
	}

	/**
	 * 批量找到其子id
	 * 
	 * @param cs
	 * @return
	 */
	public String[] getChild(String[] cs) {

		List<String> list = new ArrayList<String>(cs.length);

		for (int i = 0; i < cs.length; i++) {
			
			long fatherId = Long.valueOf(cs[i].trim()).longValue();
			List<Long> temp = this.getChild(fatherId);
			if(temp != null){
				list.add(cs[i].trim());
				if (temp.size() > 0) {
					for (int j = 0; j < temp.size(); j++) {
						long v = temp.get(j);
						list.add(v + "");
					}
				}
			}
		}

		return list.toArray(new String[list.size()]);
	}
}
