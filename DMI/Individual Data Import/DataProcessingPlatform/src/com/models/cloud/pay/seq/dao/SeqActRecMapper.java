package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqActRec;

public interface SeqActRecMapper {
	int replace();
    SeqActRec selectSeqActRec();
}