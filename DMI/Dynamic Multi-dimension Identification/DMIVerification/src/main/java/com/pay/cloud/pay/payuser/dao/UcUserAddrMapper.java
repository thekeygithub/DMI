package com.pay.cloud.pay.payuser.dao;

import java.util.List;

import com.pay.cloud.pay.payuser.entity.UcUserAddr;

public interface UcUserAddrMapper {
    int deleteByPrimaryKey(String ucAddrId);

    int insert(UcUserAddr record);
    /***
     * 添加地址
     * @param record
     * @return
     */
    int insertSelective(UcUserAddr record);

    UcUserAddr selectByPrimaryKey(String ucAddrId);

    int updateByPrimaryKeySelective(UcUserAddr record);

    int updateByPrimaryKey(UcUserAddr record);
    /***
     * 通过账户id查询地址信息
     * @param actId
     * @return
     */
    public UcUserAddr selectByAddr(String ucAddrId);
    /***
     * 修改用户中收货地址
     * @param addr
     */
    public void updateAddr(UcUserAddr addr);
    /***
     * 通过账户Id查询，返回list集合
     * @param actId
     * @return
     */
    public List<UcUserAddr> selectByUcAddrId(Long actId);
}