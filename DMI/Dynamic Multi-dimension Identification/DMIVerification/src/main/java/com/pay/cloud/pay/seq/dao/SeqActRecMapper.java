package com.pay.cloud.pay.seq.dao;

import com.pay.cloud.pay.seq.entity.SeqActRec;

public interface SeqActRecMapper {
	int replace();
    SeqActRec selectSeqActRec();
}