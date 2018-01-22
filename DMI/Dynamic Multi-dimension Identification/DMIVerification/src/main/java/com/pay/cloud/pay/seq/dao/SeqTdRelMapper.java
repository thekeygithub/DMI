package com.pay.cloud.pay.seq.dao;

import com.pay.cloud.pay.seq.entity.SeqTdRel;

public interface SeqTdRelMapper {
	int replace();
    SeqTdRel selectSeqTdRel();
}