package com.models.cloud.pay.seq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.seq.dao.SeqActIdMapper;
import com.models.cloud.pay.seq.dao.SeqActRecMapper;
import com.models.cloud.pay.seq.dao.SeqTdMapper;
import com.models.cloud.pay.seq.dao.SeqTdRelMapper;
import com.models.cloud.pay.seq.dao.SeqUcBindMapper;
import com.models.cloud.pay.seq.dao.SeqUserIdMapper;
import com.models.cloud.pay.seq.dao.SeqYeepayBatchNoMapper;
import com.models.cloud.pay.seq.entity.SeqActId;
import com.models.cloud.pay.seq.entity.SeqActRec;
import com.models.cloud.pay.seq.entity.SeqTd;
import com.models.cloud.pay.seq.entity.SeqTdRel;
import com.models.cloud.pay.seq.entity.SeqUcBind;
import com.models.cloud.pay.seq.entity.SeqUserId;
import com.models.cloud.pay.seq.entity.SeqYeepayBatchNo;
import com.models.cloud.pay.seq.service.SeqService;

@Service("seqServiceImpl")
public class SeqServiceImpl implements SeqService {

	@Autowired
	private SeqActIdMapper seqActIdMapper;
	@Autowired
	private SeqTdMapper seqTdMapper;
	@Autowired
	private SeqActRecMapper seqActRecMapper;
	@Autowired
	private SeqTdRelMapper seqTdRelMapper;
	@Autowired
	private SeqUserIdMapper seqUserIdMapper;
	@Autowired
	private SeqYeepayBatchNoMapper seqYeepayBatchNoMapper;
	@Autowired
	private SeqUcBindMapper seqUcBindMapper;
	
	@Override
	public synchronized long updateSeqActId() {
		seqActIdMapper.replace();
		SeqActId seqActId = seqActIdMapper.selectSeqActId();
		if(seqActId!=null && seqActId.getId()!=null){
			return seqActId.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqTd() {
		seqTdMapper.replace();
		SeqTd seqTd = seqTdMapper.selectSeqTd();
		if(seqTd!=null && seqTd.getId()!=null){
			return seqTd.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqActRec() {
		seqActRecMapper.replace();
		SeqActRec seqActRec = seqActRecMapper.selectSeqActRec();
		if(seqActRec!=null && seqActRec.getId()!=null){
			return seqActRec.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqTdRel() {
		seqTdRelMapper.replace();
		SeqTdRel seqTdRel = seqTdRelMapper.selectSeqTdRel();
		if(seqTdRel!=null && seqTdRel.getId()!=null){
			return seqTdRel.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqUserId() {
		seqUserIdMapper.replace();
		SeqUserId seqUserId = seqUserIdMapper.selectSeqUserId();
		if(seqUserId!=null && seqUserId.getId()!=null){
			return seqUserId.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqYeepayBatchNo() {
		seqYeepayBatchNoMapper.replace();
		SeqYeepayBatchNo seqYeepayBatchNo = seqYeepayBatchNoMapper.selectSeqYeepayBatchNo();
		if(seqYeepayBatchNo!=null && seqYeepayBatchNo.getId()!=null){
			return seqYeepayBatchNo.getId();
		}
		return 0;
	}

	@Override
	public synchronized long updateSeqUcBindMi() {
		seqUcBindMapper.replace();
		SeqUcBind seqUcBind = seqUcBindMapper.selectSeqUcBind();
		if(seqUcBind!=null && seqUcBind.getId()!=null){
			return seqUcBind.getId();
		}
		return 0;
	}

	
}
