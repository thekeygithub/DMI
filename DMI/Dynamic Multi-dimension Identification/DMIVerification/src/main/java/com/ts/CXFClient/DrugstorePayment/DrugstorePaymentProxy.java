package com.ts.CXFClient.DrugstorePayment;

public class DrugstorePaymentProxy implements com.ts.CXFClient.DrugstorePayment.DrugstorePayment {
  private String _endpoint = null;
  private com.ts.CXFClient.DrugstorePayment.DrugstorePayment drugstorePayment = null;
  
  public DrugstorePaymentProxy() {
    _initDrugstorePaymentProxy();
  }
  
  public DrugstorePaymentProxy(String endpoint) {
    _endpoint = endpoint;
    _initDrugstorePaymentProxy();
  }
  
  private void _initDrugstorePaymentProxy() {
    try {
      drugstorePayment = (new com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceLocator()).getDrugstorePaymentPort();
      if (drugstorePayment != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)drugstorePayment)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)drugstorePayment)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (drugstorePayment != null)
      ((javax.xml.rpc.Stub)drugstorePayment)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ts.CXFClient.DrugstorePayment.DrugstorePayment getDrugstorePayment() {
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment;
  }
  
  public com.ts.CXFClient.DrugstorePayment.D01OutputPreSettlementBean getPreSettlement(com.ts.CXFClient.DrugstorePayment.D01InputPreSettlementBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getPreSettlement(arg0);
  }
  
  public com.ts.CXFClient.DrugstorePayment.D04OutputRefundBean getRefund(com.ts.CXFClient.DrugstorePayment.D04InputRefundBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getRefund(arg0);
  }
  
  public com.ts.CXFClient.DrugstorePayment.D02OutputSettlementBean getSettlement(com.ts.CXFClient.DrugstorePayment.D02InputSettlementBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getSettlement(arg0);
  }
  
  public com.ts.CXFClient.DrugstorePayment.D03OutputCheckRefundBean getCheckRefund(com.ts.CXFClient.DrugstorePayment.D03InputCheckRefundBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getCheckRefund(arg0);
  }
  
  public com.ts.CXFClient.DrugstorePayment.D05OutputReconciliationBean getReconciliation(com.ts.CXFClient.DrugstorePayment.D05InputReconciliationBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getReconciliation(arg0);
  }
  
  public com.ts.CXFClient.DrugstorePayment.D06OutputPreRefundBean getPreRefund(com.ts.CXFClient.DrugstorePayment.D06InputPreRefundBean arg0) throws java.rmi.RemoteException{
    if (drugstorePayment == null)
      _initDrugstorePaymentProxy();
    return drugstorePayment.getPreRefund(arg0);
  }
  
  
}