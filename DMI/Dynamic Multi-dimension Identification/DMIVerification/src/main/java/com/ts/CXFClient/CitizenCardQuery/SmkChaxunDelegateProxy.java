package com.ts.CXFClient.CitizenCardQuery;

public class SmkChaxunDelegateProxy implements SmkChaxunDelegate {
  private String _endpoint = null;
  private SmkChaxunDelegate smkChaxunDelegate = null;
  
  public SmkChaxunDelegateProxy() {
    _initSmkChaxunDelegateProxy();
  }
  
  public SmkChaxunDelegateProxy(String endpoint) {
    _endpoint = endpoint;
    _initSmkChaxunDelegateProxy();
  }
  
  private void _initSmkChaxunDelegateProxy() {
    try {
      smkChaxunDelegate = (new SmkChaxunServiceLocator()).getSmkChaxunPort();
      if (smkChaxunDelegate != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)smkChaxunDelegate)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)smkChaxunDelegate)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (smkChaxunDelegate != null)
      ((javax.xml.rpc.Stub)smkChaxunDelegate)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SmkChaxunDelegate getSmkChaxunDelegate() {
    if (smkChaxunDelegate == null)
      _initSmkChaxunDelegateProxy();
    return smkChaxunDelegate;
  }
  
  public java.lang.String findPerInFo(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (smkChaxunDelegate == null)
      _initSmkChaxunDelegateProxy();
    return smkChaxunDelegate.findPerInFo(arg0, arg1);
  }
  
  
}