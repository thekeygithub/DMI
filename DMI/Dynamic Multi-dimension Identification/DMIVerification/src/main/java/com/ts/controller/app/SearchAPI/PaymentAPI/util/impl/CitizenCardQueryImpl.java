package com.ts.controller.app.SearchAPI.PaymentAPI.util.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ts.CXFClient.CitizenCardQuery.SmkChaxunDelegate;
import com.ts.controller.app.SearchAPI.PaymentAPI.util.CitizenCardQueryWsdl;
import com.ts.entity.CitizenCardQuery.V01InputCitizenCardQueryBean;
import com.ts.entity.CitizenCardQuery.V01OutCitizenCardQueryBean;
import com.ts.util.Logger;
import com.ts.util.CXFClientFactory.CXFClientFactory;

/**
 * 市民卡状态信息接口 - 市民卡中心
 * @author autumn
 *
 */
public class CitizenCardQueryImpl implements CitizenCardQueryWsdl{

    Logger logger = Logger.getLogger("CitizenCardQueryWsdl");
    @Override
    public V01OutCitizenCardQueryBean getCitizenCardQuery(V01InputCitizenCardQueryBean bean) {
        // TODO Auto-generated method stub
        V01OutCitizenCardQueryBean cardQuery = new V01OutCitizenCardQueryBean();
        String sfzh = bean.getV01_INP_NO01();
        String smkh = bean.getV01_INP_NO02();
        try {
//          SmkChaxunDelegate smkxx = new SmkChaxunServiceLocator().getSmkChaxunPort();
            SmkChaxunDelegate smkxx = CXFClientFactory.FactorySmkChaxunDelegate();
            String smk = smkxx.findPerInFo(sfzh, smkh);
//          String smk="[{\"wlkh\":\"330499D1560000050011F103B0E4BD78\",\"kzt\":\"3\",\"zjhm\":\"33040219620415182X\",\"xm\":\"张美珍\",\"ywlkh\":\"330400D1560000050005D0535E705C03\"}]";
            logger.info("市民卡中心内容:" + smk);
            JSONArray ja = JSONArray.fromObject(smk);
            JSONObject jo = new JSONObject();
            for(Object obj: ja){
                jo = (JSONObject) obj;
            }
            String wlkh = jo.getString("wlkh");
            String kzt = jo.getString("kzt");
            String xm = jo.getString("xm");
            
            cardQuery.setV01_OUT_NO01(kzt); 
            cardQuery.setV01_OUT_NO02(xm);
            cardQuery.setV01_OUT_NO03(wlkh);
        } 
        catch (Exception e) 
        {
            // 处理市民卡中心 返回 空信息的情况. 返回空时则表示 查询不到此人的信息 
            cardQuery.setV01_OUT_NO01("-100");  
            cardQuery.setV01_OUT_NO02("");
            cardQuery.setV01_OUT_NO03(""); 
            logger.error(e.getMessage());
            e.printStackTrace();
            return cardQuery;
        }
        return cardQuery;
    }
}
