package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqUcBind;

public interface SeqUcBindMapper {
	int replace();
	SeqUcBind selectSeqUcBind();
}