package com.pay.cloud.pay.seq.dao;

import com.pay.cloud.pay.seq.entity.SeqActId;

public interface SeqActIdMapper {
	int replace();
    SeqActId selectSeqActId();
}