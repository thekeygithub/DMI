package com.pay.cloud.pay.payuser.service;

import java.util.List;

import com.pay.cloud.pay.payuser.entity.UcBindMi;

/**
 * Created by yacheng.ji on 2016/8/17.
 */
public interface UcBindMiService {

    List<UcBindMi> findUcBindMiList(UcBindMi record) throws Exception;

    UcBindMi findUcBindMi(Long ucBindId) throws Exception;
}
