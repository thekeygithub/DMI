package com.models.cloud.pay.supplier.dao;

import java.util.List;

public interface SpServIntfMapper {

    List<String> selectActIdByIntfCode(String interfaceCode);
}