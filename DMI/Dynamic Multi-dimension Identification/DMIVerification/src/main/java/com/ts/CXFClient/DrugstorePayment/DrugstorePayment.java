/**
 * DrugstorePayment.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ts.CXFClient.DrugstorePayment;

public interface DrugstorePayment extends java.rmi.Remote {
    public com.ts.CXFClient.DrugstorePayment.D01OutputPreSettlementBean getPreSettlement(com.ts.CXFClient.DrugstorePayment.D01InputPreSettlementBean arg0) throws java.rmi.RemoteException;
    public com.ts.CXFClient.DrugstorePayment.D04OutputRefundBean getRefund(com.ts.CXFClient.DrugstorePayment.D04InputRefundBean arg0) throws java.rmi.RemoteException;
    public com.ts.CXFClient.DrugstorePayment.D02OutputSettlementBean getSettlement(com.ts.CXFClient.DrugstorePayment.D02InputSettlementBean arg0) throws java.rmi.RemoteException;
    public com.ts.CXFClient.DrugstorePayment.D03OutputCheckRefundBean getCheckRefund(com.ts.CXFClient.DrugstorePayment.D03InputCheckRefundBean arg0) throws java.rmi.RemoteException;
    public com.ts.CXFClient.DrugstorePayment.D05OutputReconciliationBean getReconciliation(com.ts.CXFClient.DrugstorePayment.D05InputReconciliationBean arg0) throws java.rmi.RemoteException;
    public com.ts.CXFClient.DrugstorePayment.D06OutputPreRefundBean getPreRefund(com.ts.CXFClient.DrugstorePayment.D06InputPreRefundBean arg0) throws java.rmi.RemoteException;
}
