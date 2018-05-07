package com.models.cloud.pay.seq.dao;

import com.models.cloud.pay.seq.entity.SeqTdRel;

public interface SeqTdRelMapper {
	int replace();
    SeqTdRel selectSeqTdRel();
}