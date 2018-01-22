package com.pay.cloud.pay.seq.dao;

import com.pay.cloud.pay.seq.entity.SeqYeepayBatchNo;

public interface SeqYeepayBatchNoMapper {
    
    int replace();
    SeqYeepayBatchNo selectSeqYeepayBatchNo();
}