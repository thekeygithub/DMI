package com.ts.util.CXFClientFactory;

import java.net.URL;
import javax.xml.namespace.QName;

import com.ts.CXFClient.MedicarePayment;
import com.ts.CXFClient.MedicarePaymentService;
import com.ts.CXFClient.CitizenCardQuery.SmkChaxunDelegate;
import com.ts.CXFClient.CitizenCardQuery.SmkChaxunServiceLocator;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;

/**
 *  webservice 工厂 类 
 * @author autumn
 *
 */
public final class CXFClientFactory
{    
    
    private static String  webservice_url_pay = "";
    
    private static String  webservice_url_Smkkxx ="";
    
    static {
        /* 获取数据交换接口平台地址*/
        webservice_url_pay = ReadPropertiesFiles.getValue("webservice_url_pay");
        /* 获取市民卡中心信息 */
        webservice_url_Smkkxx = ReadPropertiesFiles.getValue("webservice_url_Smkkxx");
        try
        {
            CXFClientFactory.FactoryMedicarePayment();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private CXFClientFactory() {}
    
    /**
     * 数据交换接口平台 
     * @return
     */
    private static MedicarePaymentService service =  null;
    private static MedicarePayment medicarepayment = null;
    public static MedicarePayment FactoryMedicarePayment() throws Exception{
        if (medicarepayment != null) return medicarepayment;
        
        URL baseUrl = com.ts.CXFClient.MedicarePaymentService.class.getResource(".");
        URL url = new URL(baseUrl,webservice_url_pay);
        QName qName = new QName("http://www.ts.com/services/MedicarePayment","MedicarePaymentService");
        //调用接口
        MedicarePaymentService service = new MedicarePaymentService(url,qName);
        medicarepayment = service.getMedicarePaymentPort();
        return medicarepayment;
    }
    
    /**
     * 市民卡中心 
     * @return
     * @throws Exception
     */
    public static SmkChaxunDelegate FactorySmkChaxunDelegate() throws Exception{
        URL endpoint = new java.net.URL(webservice_url_Smkkxx);
        return new SmkChaxunServiceLocator().getSmkChaxunPort(endpoint);
    }

}