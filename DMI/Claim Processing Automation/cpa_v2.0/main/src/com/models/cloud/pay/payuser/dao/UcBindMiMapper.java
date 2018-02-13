package com.models.cloud.pay.payuser.dao;

import java.util.List;

import com.models.cloud.pay.payuser.entity.UcBindMi;

public interface UcBindMiMapper {
    int deleteByPrimaryKey(Long ucBindId);

    int insert(UcBindMi record);

    int insertSelective(UcBindMi record);

    UcBindMi selectByPrimaryKey(Long ucBindId);

    int updateByPrimaryKeySelective(UcBindMi record);

    int updateByPrimaryKey(UcBindMi record);
    
    List<UcBindMi> selectByActId(UcBindMi record);
    
    List<UcBindMi> selectByUcBindMi(UcBindMi record);
}