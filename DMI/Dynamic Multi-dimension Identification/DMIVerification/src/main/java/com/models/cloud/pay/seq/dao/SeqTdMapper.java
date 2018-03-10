package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqTd;

public interface SeqTdMapper {
   
	int replace();
    SeqTd selectSeqTd();

}