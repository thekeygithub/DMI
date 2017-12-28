/**
 * DrugstorePaymentServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ts.CXFClient.DrugstorePayment;

public class DrugstorePaymentServiceLocator extends org.apache.axis.client.Service implements com.ts.CXFClient.DrugstorePayment.DrugstorePaymentService {

    public DrugstorePaymentServiceLocator() {
    }


    public DrugstorePaymentServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DrugstorePaymentServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DrugstorePaymentPort
    private java.lang.String DrugstorePaymentPort_address = "http://10.10.40.223:8880/PAY/services/drugstorePayment";

    public java.lang.String getDrugstorePaymentPortAddress() {
        return DrugstorePaymentPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DrugstorePaymentPortWSDDServiceName = "DrugstorePaymentPort";

    public java.lang.String getDrugstorePaymentPortWSDDServiceName() {
        return DrugstorePaymentPortWSDDServiceName;
    }

    public void setDrugstorePaymentPortWSDDServiceName(java.lang.String name) {
        DrugstorePaymentPortWSDDServiceName = name;
    }

    public com.ts.CXFClient.DrugstorePayment.DrugstorePayment getDrugstorePaymentPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DrugstorePaymentPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDrugstorePaymentPort(endpoint);
    }

    public com.ts.CXFClient.DrugstorePayment.DrugstorePayment getDrugstorePaymentPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub _stub = new com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDrugstorePaymentPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDrugstorePaymentPortEndpointAddress(java.lang.String address) {
        DrugstorePaymentPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ts.CXFClient.DrugstorePayment.DrugstorePayment.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub _stub = new com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub(new java.net.URL(DrugstorePaymentPort_address), this);
                _stub.setPortName(getDrugstorePaymentPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DrugstorePaymentPort".equals(inputPortName)) {
            return getDrugstorePaymentPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ts.com/services/DrugstorePayment", "DrugstorePaymentService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.ts.com/services/DrugstorePayment", "DrugstorePaymentPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DrugstorePaymentPort".equals(portName)) {
            setDrugstorePaymentPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
