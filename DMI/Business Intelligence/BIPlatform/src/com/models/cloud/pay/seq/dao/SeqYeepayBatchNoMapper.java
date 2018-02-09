package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqYeepayBatchNo;

public interface SeqYeepayBatchNoMapper {
    
    int replace();
    SeqYeepayBatchNo selectSeqYeepayBatchNo();
}