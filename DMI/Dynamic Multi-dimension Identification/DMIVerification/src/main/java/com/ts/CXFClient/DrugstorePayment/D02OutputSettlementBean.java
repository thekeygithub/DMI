/**
 * D02OutputSettlementBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ts.CXFClient.DrugstorePayment;

public class D02OutputSettlementBean  implements java.io.Serializable {
    private java.lang.String d02_OUT_NO01;

    private java.lang.String d02_OUT_NO02;

    private java.lang.String d02_OUT_NO03;

    private java.lang.String d02_OUT_NO04;

    private com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean[] d02_OUT_SUB01;

    public D02OutputSettlementBean() {
    }

    public D02OutputSettlementBean(
           java.lang.String d02_OUT_NO01,
           java.lang.String d02_OUT_NO02,
           java.lang.String d02_OUT_NO03,
           java.lang.String d02_OUT_NO04,
           com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean[] d02_OUT_SUB01) {
           this.d02_OUT_NO01 = d02_OUT_NO01;
           this.d02_OUT_NO02 = d02_OUT_NO02;
           this.d02_OUT_NO03 = d02_OUT_NO03;
           this.d02_OUT_NO04 = d02_OUT_NO04;
           this.d02_OUT_SUB01 = d02_OUT_SUB01;
    }


    /**
     * Gets the d02_OUT_NO01 value for this D02OutputSettlementBean.
     * 
     * @return d02_OUT_NO01
     */
    public java.lang.String getD02_OUT_NO01() {
        return d02_OUT_NO01;
    }


    /**
     * Sets the d02_OUT_NO01 value for this D02OutputSettlementBean.
     * 
     * @param d02_OUT_NO01
     */
    public void setD02_OUT_NO01(java.lang.String d02_OUT_NO01) {
        this.d02_OUT_NO01 = d02_OUT_NO01;
    }


    /**
     * Gets the d02_OUT_NO02 value for this D02OutputSettlementBean.
     * 
     * @return d02_OUT_NO02
     */
    public java.lang.String getD02_OUT_NO02() {
        return d02_OUT_NO02;
    }


    /**
     * Sets the d02_OUT_NO02 value for this D02OutputSettlementBean.
     * 
     * @param d02_OUT_NO02
     */
    public void setD02_OUT_NO02(java.lang.String d02_OUT_NO02) {
        this.d02_OUT_NO02 = d02_OUT_NO02;
    }


    /**
     * Gets the d02_OUT_NO03 value for this D02OutputSettlementBean.
     * 
     * @return d02_OUT_NO03
     */
    public java.lang.String getD02_OUT_NO03() {
        return d02_OUT_NO03;
    }


    /**
     * Sets the d02_OUT_NO03 value for this D02OutputSettlementBean.
     * 
     * @param d02_OUT_NO03
     */
    public void setD02_OUT_NO03(java.lang.String d02_OUT_NO03) {
        this.d02_OUT_NO03 = d02_OUT_NO03;
    }


    /**
     * Gets the d02_OUT_NO04 value for this D02OutputSettlementBean.
     * 
     * @return d02_OUT_NO04
     */
    public java.lang.String getD02_OUT_NO04() {
        return d02_OUT_NO04;
    }


    /**
     * Sets the d02_OUT_NO04 value for this D02OutputSettlementBean.
     * 
     * @param d02_OUT_NO04
     */
    public void setD02_OUT_NO04(java.lang.String d02_OUT_NO04) {
        this.d02_OUT_NO04 = d02_OUT_NO04;
    }


    /**
     * Gets the d02_OUT_SUB01 value for this D02OutputSettlementBean.
     * 
     * @return d02_OUT_SUB01
     */
    public com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean[] getD02_OUT_SUB01() {
        return d02_OUT_SUB01;
    }


    /**
     * Sets the d02_OUT_SUB01 value for this D02OutputSettlementBean.
     * 
     * @param d02_OUT_SUB01
     */
    public void setD02_OUT_SUB01(com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean[] d02_OUT_SUB01) {
        this.d02_OUT_SUB01 = d02_OUT_SUB01;
    }

    public com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean getD02_OUT_SUB01(int i) {
        return this.d02_OUT_SUB01[i];
    }

    public void setD02_OUT_SUB01(int i, com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean _value) {
        this.d02_OUT_SUB01[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof D02OutputSettlementBean)) return false;
        D02OutputSettlementBean other = (D02OutputSettlementBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.d02_OUT_NO01==null && other.getD02_OUT_NO01()==null) || 
             (this.d02_OUT_NO01!=null &&
              this.d02_OUT_NO01.equals(other.getD02_OUT_NO01()))) &&
            ((this.d02_OUT_NO02==null && other.getD02_OUT_NO02()==null) || 
             (this.d02_OUT_NO02!=null &&
              this.d02_OUT_NO02.equals(other.getD02_OUT_NO02()))) &&
            ((this.d02_OUT_NO03==null && other.getD02_OUT_NO03()==null) || 
             (this.d02_OUT_NO03!=null &&
              this.d02_OUT_NO03.equals(other.getD02_OUT_NO03()))) &&
            ((this.d02_OUT_NO04==null && other.getD02_OUT_NO04()==null) || 
             (this.d02_OUT_NO04!=null &&
              this.d02_OUT_NO04.equals(other.getD02_OUT_NO04()))) &&
            ((this.d02_OUT_SUB01==null && other.getD02_OUT_SUB01()==null) || 
             (this.d02_OUT_SUB01!=null &&
              java.util.Arrays.equals(this.d02_OUT_SUB01, other.getD02_OUT_SUB01())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getD02_OUT_NO01() != null) {
            _hashCode += getD02_OUT_NO01().hashCode();
        }
        if (getD02_OUT_NO02() != null) {
            _hashCode += getD02_OUT_NO02().hashCode();
        }
        if (getD02_OUT_NO03() != null) {
            _hashCode += getD02_OUT_NO03().hashCode();
        }
        if (getD02_OUT_NO04() != null) {
            _hashCode += getD02_OUT_NO04().hashCode();
        }
        if (getD02_OUT_SUB01() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getD02_OUT_SUB01());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getD02_OUT_SUB01(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(D02OutputSettlementBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ts.com/services/DrugstorePayment", "d02OutputSettlementBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d02_OUT_NO01");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d02_OUT_NO01"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d02_OUT_NO02");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d02_OUT_NO02"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d02_OUT_NO03");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d02_OUT_NO03"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d02_OUT_NO04");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d02_OUT_NO04"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d02_OUT_SUB01");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d02_OUT_SUB01"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ts.com/services/DrugstorePayment", "d02OutResultInfoBean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
