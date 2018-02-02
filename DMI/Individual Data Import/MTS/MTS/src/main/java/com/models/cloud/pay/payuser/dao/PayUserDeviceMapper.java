package com.models.cloud.pay.payuser.dao;

import com.models.cloud.pay.payuser.entity.PayUserDevice;

public interface PayUserDeviceMapper {
    /**
     * 
     * @Description: 保存终端设备标识
	 * @Title: savePayUserDevice 
	 * @param payUserDevice 
     * @return int
     * @throws Exception 
     */
    public int savePayUserDevice(PayUserDevice payUserDevice) throws Exception;

    /**
	 * 
	 * @Description: 终端设备标识查重
	 * @Title: findPayUserDevice 
	 * @param payUserDevice
	 * @return PayUserDevice
	 * @throws Exception 
	 */
	PayUserDevice findPayUserDevice(PayUserDevice payUserDevice) throws Exception;

	/**
	 * 
	 * @Description: 终端设备标识更新
	 * @Title: findPayUserDevice 
	 * @param payUserDevice
	 * @return int
	 * @throws Exception 
	 */
	public int updatePayUserDevice(PayUserDevice payUserDevice) throws Exception;
}