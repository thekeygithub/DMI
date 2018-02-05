package com.models.cloud.pay.proplat.dao;

import java.util.List;

import com.models.cloud.pay.proplat.entity.PpMiProject;

public interface PpMiProjectMapper {
    int deleteByPrimaryKey(String micId);

    int insert(PpMiProject record);

    int insertSelective(PpMiProject record);

    PpMiProject selectByPrimaryKey(String micId);

    int updateByPrimaryKeySelective(PpMiProject record);

    int updateByPrimaryKey(PpMiProject record);
    
    List<PpMiProject> PpMiProjectAll();
    /**
     * 级联查询
     * @return
     */
    List<PpMiProject> PpMiProjectAllJoinUcBindAuthCol();
}