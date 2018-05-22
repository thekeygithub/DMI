package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqUserId;

public interface SeqUserIdMapper {
	int replace();
	SeqUserId selectSeqUserId();
}