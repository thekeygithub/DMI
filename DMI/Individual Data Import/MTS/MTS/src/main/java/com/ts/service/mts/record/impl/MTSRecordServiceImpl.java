package com.ts.service.mts.record.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsRecord;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.util.PageData;
import com.ts.vo.mts.RecordInfoVO;

@Service("MTSRecordService")
public class MTSRecordServiceImpl implements MTSRecordService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	@Override
	public void addMTSRecord(MtsRecord mtsRecord) throws Exception{
		dao.save("MtsRecordMapper.addMtsRecord", mtsRecord);
	}

	@Override
	public void editMTSRecord(MtsRecord mtsRecord) throws Exception{
		dao.update("MtsRecordMapper.editMtsRecord", mtsRecord);
	}

	@Override
	public void deleteMTSRecord(MtsRecord mtsRecord) throws Exception{
		dao.delete("MtsRecordMapper.deleteMtsRecord", mtsRecord);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsRecord> findMTSRecord(MtsRecord mtsRecord) throws Exception{
		return (List<MtsRecord>) dao.findForList("MtsRecordMapper.findMTSRecord", mtsRecord);
	}

	@Override
	/**mts标准化比对记录
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> mtsRecordlistPage(Page page)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("MtsRecordMapper.mtsRecordlistPage", page);
		return list;
	}

	@Override
	public void addMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception {
		dao.save("MtsRecordInfoMapper.addMtsRecordInfo", mtsRecordInfo);
		
	}

	@Override
	public void editMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception {
		dao.update("MtsRecordInfoMapper.editMtsRecordInfo", mtsRecordInfo);
	}

	@Override
	public void deleteMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception {
		dao.delete("MtsRecordInfoMapper.deleteMtsRecordInfo", mtsRecordInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsRecordInfo> findMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception {
		return (List<MtsRecordInfo>) dao.findForList("MtsRecordInfoMapper.findMtsRecordInfo", mtsRecordInfo);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MtsRecordInfo> findMtsRecordInfoByMark(RecordInfoVO recordInfoVO) throws Exception {
		return (List<MtsRecordInfo>) dao.findForList("MtsRecordInfoMapper.findMtsRecordInfoByMark", recordInfoVO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsRecordInfolistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsRecordInfoMapper.mtsRecordInfolistPage", page);
		return list;
	}

	@Override
	public void addMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception {
		dao.save("MtsRecordDetailMapper.addMtsRecordDetail", mtsRecordDetail);
	}

	@Override
	public void editMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception {
		dao.update("MtsRecordDetailMapper.editMtsRecordDetail", mtsRecordDetail);
	}

	@Override
	public void deleteMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception {
		dao.delete("MtsRecordDetailMapper.deleteMtsRecordDetail", mtsRecordDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsRecordDetail> findMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception {
		return (List<MtsRecordDetail>) dao.findForList("MtsRecordDetailMapper.findMtsRecordDetail", mtsRecordDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsRecordDetaillistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsRecordDetailMapper.mtsRecordDetaillistPage", page);
		return list;
	}

	@Override
	public void addMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception {
		dao.save("MtsRecordParametersMapper.addMtsRecordParameters", mtsRecordParameters);
	}

	@Override
	public void editMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception {
		dao.update("MtsRecordParametersMapper.editMtsRecordParameters", mtsRecordParameters);
		
	}

	@Override
	public void deleteMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception {
		dao.delete("MtsRecordParametersMapper.deleteMtsRecordParameters", mtsRecordParameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsRecordParameters> findMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception {
		return (List<MtsRecordParameters>) dao.findForList("MtsRecordParametersMapper.findMtsRecordParameters", mtsRecordParameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsRecordParameterslistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsRecordParametersMapper.mtsRecordParameterslistPage", page);
		return list;
	}

	@Override
	public int findMtsRecordInfoCount(MtsRecordInfo mtsRecordInfo) throws Exception {
		return (int) dao.findForObject("MtsRecordInfoMapper.findMtsRecordInfoCount", mtsRecordInfo);
	}
}
