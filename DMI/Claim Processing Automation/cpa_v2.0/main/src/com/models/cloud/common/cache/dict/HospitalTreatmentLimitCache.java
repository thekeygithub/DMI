package com.models.cloud.common.cache.dict;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 医院诊疗限制
 * Created by yacheng.ji on 2017/3/30.
 */
@Component
public class HospitalTreatmentLimitCache {

    private static final Logger logger = Logger.getLogger(HospitalTreatmentLimitCache.class);

    public static Map<String, String> getSexLimitDataMap() {
        return sexLimitDataMap;
    }

    public static Map<String, String> getChildLimitDataMap() {
        return childLimitDataMap;
    }

    private static Map<String, String> sexLimitDataMap = new HashMap<>();
    private static Map<String, String> childLimitDataMap = new HashMap<>();

    /**
     * 初始化性别限制数据(中心医院)
     */
    @PostConstruct
    public void initSexLimitData() {
        InputStreamReader read = null;
        try {
            File file = new File(this.getClass().getClassLoader().getResource("").getPath().concat("/miLimit/KF_ZXYY_LIMIT_SEX.txt"));
            read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            String[] txtArr;
            sexLimitDataMap.clear();
            while((lineTxt = bufferedReader.readLine()) != null){
                txtArr = lineTxt.trim().split("\\|");
                sexLimitDataMap.put(txtArr[0].concat("_").concat(txtArr[2]).toUpperCase(), txtArr[1]);
            }
            logger.info("加载KF_ZXYY_LIMIT_SEX完成 sexLimitDataMap.size=" + sexLimitDataMap.size());
        }catch (Exception e) {
            logger.info("读取KF_ZXYY_LIMIT_SEX文件出现异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(null != read){
                try {
                    read.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化儿童限制数据(<=18岁 中心医院)
     */
    @PostConstruct
    public void initChildLimitData() throws Exception {
        InputStreamReader read = null;
        try {
            File file = new File(this.getClass().getClassLoader().getResource("").getPath().concat("/miLimit/KF_ZXYY_LIMIT_CHILD.txt"));
            read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            String[] txtArr;
            childLimitDataMap.clear();
            while((lineTxt = bufferedReader.readLine()) != null){
                txtArr = lineTxt.trim().split("\\|");
                childLimitDataMap.put(txtArr[0].toUpperCase(), txtArr[1]);
            }
            logger.info("加载KF_ZXYY_LIMIT_CHILD完成 childLimitDataMap.size=" + childLimitDataMap.size());
        }catch (Exception e) {
            logger.info("读取KF_ZXYY_LIMIT_CHILD文件出现异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(null != read){
                try {
                    read.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
