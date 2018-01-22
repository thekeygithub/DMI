package com.pay.cloud.pay.seq.dao;

import com.pay.cloud.pay.seq.entity.SeqUserId;

public interface SeqUserIdMapper {
	int replace();
	SeqUserId selectSeqUserId();
}