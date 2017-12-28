/**
 * SmkChaxunServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ts.CXFClient.CitizenCardQuery;

public class SmkChaxunServiceLocator extends org.apache.axis.client.Service implements SmkChaxunService {

    public SmkChaxunServiceLocator() {
    }


    public SmkChaxunServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SmkChaxunServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SmkChaxunPort
    private java.lang.String SmkChaxunPort_address = "http://10.82.21.36:8080/Smkkxx/SmkChaxunPort";

    public java.lang.String getSmkChaxunPortAddress() {
        return SmkChaxunPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SmkChaxunPortWSDDServiceName = "SmkChaxunPort";

    public java.lang.String getSmkChaxunPortWSDDServiceName() {
        return SmkChaxunPortWSDDServiceName;
    }

    public void setSmkChaxunPortWSDDServiceName(java.lang.String name) {
        SmkChaxunPortWSDDServiceName = name;
    }

    public SmkChaxunDelegate getSmkChaxunPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SmkChaxunPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSmkChaxunPort(endpoint);
    }

    public SmkChaxunDelegate getSmkChaxunPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SmkChaxunPortBindingStub _stub = new SmkChaxunPortBindingStub(portAddress, this);
            _stub.setPortName(getSmkChaxunPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSmkChaxunPortEndpointAddress(java.lang.String address) {
        SmkChaxunPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SmkChaxunDelegate.class.isAssignableFrom(serviceEndpointInterface)) {
                SmkChaxunPortBindingStub _stub = new SmkChaxunPortBindingStub(new java.net.URL(SmkChaxunPort_address), this);
                _stub.setPortName(getSmkChaxunPortWSDDServiceName());
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
        if ("SmkChaxunPort".equals(inputPortName)) {
            return getSmkChaxunPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service/", "SmkChaxunService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service/", "SmkChaxunPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SmkChaxunPort".equals(portName)) {
            setSmkChaxunPortEndpointAddress(address);
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
