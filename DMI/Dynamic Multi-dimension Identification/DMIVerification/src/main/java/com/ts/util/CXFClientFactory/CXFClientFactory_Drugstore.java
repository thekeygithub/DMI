package com.ts.util.CXFClientFactory;

import java.net.URL;

import com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceLocator;
import com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;

/**
 * 药店支付工厂类
 * @ClassName:CXFClientFactory_Drugstore
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月15日下午3:49:31
 */
public final class CXFClientFactory_Drugstore
{    
    
    private static String  webservice_url_drugstore_pay = "";
    
    static {
        /* 获取数据交换接口平台地址*/
    	webservice_url_drugstore_pay = ReadPropertiesFiles.getValue("webservice_url_drugstore_pay");
        try
        {
            CXFClientFactory_Drugstore.FactoryDrugstorePayment();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private CXFClientFactory_Drugstore() {}
    
    /**
     * 数据交换接口平台 
     * @return
     */
    private static DrugstorePaymentServiceSoapBindingStub stub=null;
    public static DrugstorePaymentServiceSoapBindingStub FactoryDrugstorePayment() throws Exception{
        if (stub != null) return stub;
        
        URL baseUrl = com.ts.CXFClient.DrugstorePayment.DrugstorePaymentService.class.getResource(".");
        URL url = new URL(baseUrl,webservice_url_drugstore_pay);
//        QName qName = new QName("http://www.ts.com/services/DrugstorePayment","DrugstorePaymentService");
        //调用接口
        DrugstorePaymentServiceLocator loca=new DrugstorePaymentServiceLocator();
        loca.setDrugstorePaymentPortEndpointAddress(url.toString());
        stub=new DrugstorePaymentServiceSoapBindingStub(url, loca);
        
        return stub;
    }

}