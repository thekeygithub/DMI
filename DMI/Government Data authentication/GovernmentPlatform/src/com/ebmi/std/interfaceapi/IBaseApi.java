package com.ebmi.std.interfaceapi;

import java.util.Map;

public interface IBaseApi {
    public TheKeyResultEntity getDataInfo(Map<String, String>  map,String methodName) throws Exception;
}
