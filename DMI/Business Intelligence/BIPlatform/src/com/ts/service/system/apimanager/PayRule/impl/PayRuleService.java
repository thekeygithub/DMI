package com.ts.service.system.apimanager.PayRule.impl;

import java.util.List;

import javax.annotation.Resource;






import org.springframework.stereotype.Service;




import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.AccessibleField.AccessibleFieldManager;
import com.ts.service.system.apimanager.PayRule.PayRuleManager;
import com.ts.util.PageData;


/**类名称：InterfaceService
 * @author 
 * 修改时间：2016年10月11日
 */
@Service("payRuleService")
public class PayRuleService implements PayRuleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="accessibleFieldService")
	private AccessibleFieldManager accessibleFieldService;
	/**接口列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPagePay(Page page)throws Exception{
		return (List<PageData>) dao.findForList("PayRuleMapper.payrulelistPage", page);
	}
		
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void savePay(PageData pd)throws Exception{
		dao.save("PayRuleMapper.savePay", pd);
	}
	/**通过IN_ID(查找)
	 * @param pd
	 * @throws Exception
	 */
	public boolean findByAll(String[] IN_IDS)throws Exception{
		Integer count = (Integer)dao.findForObject("PayRuleMapper.chekAllId", IN_IDS);
		if(count!=null && count >=1 ){
			return true;
		}
		return false;
	}
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deletePay(PageData pd)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("PayRuleMapper.findByCheckId", pd);
	    if (list != null && list.size() > 0) {  
	    	StringBuffer sb = new StringBuffer();
	        for (PageData obj:list) { 
	           sb.append(obj.getString("ID")+",");
	        }  
	        String str =  sb.substring(0, sb.length() -1);
	        String[] IDS = str.split(",");
	        dao.delete("PayRuleMapper.deleteAllCheck", IDS);
	    } 
		dao.delete("PayRuleMapper.deletePay", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editPay(PageData pd)throws Exception{
		dao.update("PayRuleMapper.editI", pd);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllPay(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("PayRuleMapper.listAllPay", pd);
	}
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] IN_IDS)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("PayRuleMapper.findByAllCheckId", IN_IDS);
	    if (list != null && list.size() > 0) {  
	    	StringBuffer sb = new StringBuffer();
	        for (PageData obj:list) { 
	           sb.append(obj.getString("ID")+",");
	        }  
	        String str =  sb.substring(0, sb.length() -1);
	        String[] IDS = str.split(",");
	        dao.delete("PayRuleMapper.deleteAllCheck", IDS);
	    } 
		dao.delete("PayRuleMapper.deleteAll", IN_IDS);
	}
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PayRuleMapper.findById", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getCount(String value)throws Exception{
		return (PageData)dao.findForObject("PayRuleMapper.getCount", value);
	}

	/* (non-Javadoc)
	 * @see com.ts.service.system.apimanager.PayRule.PayRuleManager#getDataRuleAll()
	 */
	public List<PageData> getDataRuleAll() throws Exception {
		return (List<PageData>)dao.findForList("PayRuleMapper.getDataRuleAll", null);
	}

	@Override
	public List<PageData> ParamCheckIdList(String drId) throws Exception {
		return (List<PageData>)dao.findForList("PayRuleMapper.getRuleBYDrId", drId);
	}

	@Override
	public List<PageData> findRelationByDrCheckId(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PayRuleMapper.findRelationByDrCheckId", pd);
	}

	@Override
	public boolean saveRelation(PageData pd) throws Exception {
		boolean  temp = false;
		try {
			Object obj = dao.save("PayRuleMapper.saveRelation", pd);
			if("1".equals(obj.toString())){
				temp = true;
			}	
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}
	@Override
	public boolean deleteRelation(PageData pd) throws Exception {
		boolean  temp = false;
		try {
			Object obj = dao.save("PayRuleMapper.deleteRelation", pd);
			if("1".equals(obj.toString())){
				temp = true;
			}	
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}
	
}

