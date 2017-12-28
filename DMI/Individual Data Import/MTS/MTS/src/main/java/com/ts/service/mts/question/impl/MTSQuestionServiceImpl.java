package com.ts.service.mts.question.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsQuestion;
import com.ts.service.mts.question.MTSQuestionService;
import com.ts.util.PageData;

@Service("MTSQuestionService")
public class MTSQuestionServiceImpl implements MTSQuestionService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	@Override
	public void addMTSQuestion(MtsQuestion mtsQuestion) throws Exception{
		dao.save("MtsQuestionMapper.addMtsQuestion", mtsQuestion);
	}

	@Override
	public void editMTSQuestion(MtsQuestion mtsQuestion) throws Exception{
		dao.update("MtsQuestionMapper.editMtsQuestion", mtsQuestion);
	}

	@Override
	public void deleteMTSQuestion(MtsQuestion mtsQuestion) throws Exception{
		dao.delete("MtsQuestionMapper.deleteMtsQuestion", mtsQuestion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsQuestion> findMTSQuestion(MtsQuestion mtsQuestion) throws Exception{
		return (List<MtsQuestion>) dao.findForList("MtsQuestionMapper.findMtsQuestion", mtsQuestion);
	}

	
	@Override
	/**mts问题单列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> mtsQuestionlistPage(Page page)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("MtsQuestionMapper.mtsQuestionlistPage", page);
		return list;
	}

	@Override
	public void editMtsQuestionExportStatus(MtsQuestion mtsQuestion) throws Exception {
		dao.update("MtsQuestionMapper.editMtsQuestionExportStatus", mtsQuestion);
	}
}
