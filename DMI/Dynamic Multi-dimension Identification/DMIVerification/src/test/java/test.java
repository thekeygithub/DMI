import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import com.ts.CXFClient.I31InputSectionRefundBean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.CXFClient.MedicarePaymentService;
import com.ts.util.CXFClientFactory.CXFClientFactory;

public class test
{

    public static void main(String[] args)
    {
//        URL url = null;
//        try {
//            URL baseUrl;
//            baseUrl = com.ts.CXFClient.MedicarePaymentService.class
//                    .getResource(".");
//            url = new URL(baseUrl,
//                    "http://127.0.0.1:8080/PAY/services/medicarePayment?wsdl");
//        } catch (MalformedURLException e) {
////            logger.warning("Failed to create URL for the wsdl Location: 'http://10.10.40.49:8081/services/medicarePayment?wsdl', retrying as a local file");
////            logger.warning(e.getMessage());
//            e.printStackTrace();
//        }
//        
//        QName qName = new QName(
//                "http://www.ts.com/services/MedicarePayment",
//                "MedicarePaymentService");
//        
//        //调用接口
//        MedicarePaymentService service = new MedicarePaymentService(url,qName);
//        System.out.println(service.getServiceName().getPrefix());
        
        try
        {
            MedicarePayment function = CXFClientFactory.FactoryMedicarePayment();

            System.out.println(function.getSectionRefund(new  I31InputSectionRefundBean()));;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

}
