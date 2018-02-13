package com.models.cloud.pay.payuser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.payuser.dao.UcBindMiMapper;
import com.models.cloud.pay.payuser.entity.UcBindMi;
import com.models.cloud.pay.payuser.service.UcBindMiService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/8/17.
 */
@Service("ucBindMiServiceImpl")
public class UcBindMiServiceImpl implements UcBindMiService {

    @Autowired
    private UcBindMiMapper ucBindMiMapper;

    public List<UcBindMi> findUcBindMiList(UcBindMi record) throws Exception {
        return ucBindMiMapper.selectByActId(record);
    }

    public UcBindMi findUcBindMi(Long ucBindId) throws Exception {
        return ucBindMiMapper.selectByPrimaryKey(ucBindId);
    }
}
