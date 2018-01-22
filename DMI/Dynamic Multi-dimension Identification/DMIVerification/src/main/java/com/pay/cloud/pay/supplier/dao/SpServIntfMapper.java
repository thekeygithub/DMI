package com.pay.cloud.pay.supplier.dao;

import java.util.List;

public interface SpServIntfMapper {

    List<String> selectActIdByIntfCode(String interfaceCode);
}