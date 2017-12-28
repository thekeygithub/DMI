package com.ts.service.nlp.data.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.nlp.NlpData;
import com.ts.service.nlp.data.NlpDataManger;
import com.ts.util.PageData;


@Service("NlpDataService")
public class NlpDataService implements NlpDataManger{
	
	
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
    /**
     * 根据条件获取NLP数据分页显示(前台使用)
     */
	@SuppressWarnings("unchecked")
	public List<PageData> findNlplistPage(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("NlpDataMapper.findNlplistPage", page);
	}

	 /**
     * 根据条件获取NLP数据（后台使用）
     */
	@SuppressWarnings("unchecked")
	public List<PageData> findByType() throws Exception {
		
		return (List<PageData>) dao.findForList("NlpDataMapper.findByType", null);
	}

	
	 /**
     * 添加NLP数据
     */
	public void addNLPData(NlpData ndata) throws Exception {

		dao.save("NlpDataMapper.addNLPData", ndata);
		
	}

	/**
	 * 批量删除NLP术语
	 */
	public void deleteAllU(int[] NLP_IDS) throws Exception {
		
		dao.delete("NlpDataMapper.deleteAllU", NLP_IDS);
	}

	/**
	 * 修改单条NLP术语
	 */
	public void updNLPData(NlpData ndata) throws Exception {
		dao.update("NlpDataMapper.updNLPData", ndata);
		
	}
    
	/**
	 * 删除单条NLP术语
	 */
	public void deleteNLPData(NlpData ndata) throws Exception {

		dao.delete("NlpDataMapper.deleteNLPData", ndata);
		
	}

	
	public int findByName(String nlpName) throws Exception {
		
		return (int) dao.findForObject("NlpDataMapper.findByName", nlpName);
	}

	
	

}
