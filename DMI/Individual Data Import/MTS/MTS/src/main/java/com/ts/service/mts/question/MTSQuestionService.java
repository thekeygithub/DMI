package com.ts.service.mts.question;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsQuestion;
import com.ts.util.PageData;

public interface MTSQuestionService {

	public void addMTSQuestion(MtsQuestion mtsQuestion) throws Exception;

	public void editMTSQuestion(MtsQuestion mtsQuestion) throws Exception;

	public void deleteMTSQuestion(MtsQuestion mtsQuestion) throws Exception;

	public List<MtsQuestion> findMTSQuestion(MtsQuestion mtsQuestion) throws Exception;
	
	public List<PageData> mtsQuestionlistPage(Page page)throws Exception;
	
	public void editMtsQuestionExportStatus(MtsQuestion mtsQuestion) throws Exception;
}
