package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqActId;

public interface SeqActIdMapper {
	int replace();
    SeqActId selectSeqActId();
}