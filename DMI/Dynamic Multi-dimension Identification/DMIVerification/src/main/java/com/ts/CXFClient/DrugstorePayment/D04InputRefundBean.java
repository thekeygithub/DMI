/**
 * D04InputRefundBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ts.CXFClient.DrugstorePayment;

public class D04InputRefundBean  implements java.io.Serializable {
    private java.lang.String d04_INP_NO01;

    private java.lang.String d04_INP_NO02;

    private java.lang.String d04_INP_NO03;

    private java.lang.String d04_INP_NO04;

    private java.lang.String d04_INP_NO05;

    private java.lang.String d04_INP_NO06;

    public D04InputRefundBean() {
    }

    public D04InputRefundBean(
           java.lang.String d04_INP_NO01,
           java.lang.String d04_INP_NO02,
           java.lang.String d04_INP_NO03,
           java.lang.String d04_INP_NO04,
           java.lang.String d04_INP_NO05,
           java.lang.String d04_INP_NO06) {
           this.d04_INP_NO01 = d04_INP_NO01;
           this.d04_INP_NO02 = d04_INP_NO02;
           this.d04_INP_NO03 = d04_INP_NO03;
           this.d04_INP_NO04 = d04_INP_NO04;
           this.d04_INP_NO05 = d04_INP_NO05;
           this.d04_INP_NO06 = d04_INP_NO06;
    }


    /**
     * Gets the d04_INP_NO01 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO01
     */
    public java.lang.String getD04_INP_NO01() {
        return d04_INP_NO01;
    }


    /**
     * Sets the d04_INP_NO01 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO01
     */
    public void setD04_INP_NO01(java.lang.String d04_INP_NO01) {
        this.d04_INP_NO01 = d04_INP_NO01;
    }


    /**
     * Gets the d04_INP_NO02 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO02
     */
    public java.lang.String getD04_INP_NO02() {
        return d04_INP_NO02;
    }


    /**
     * Sets the d04_INP_NO02 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO02
     */
    public void setD04_INP_NO02(java.lang.String d04_INP_NO02) {
        this.d04_INP_NO02 = d04_INP_NO02;
    }


    /**
     * Gets the d04_INP_NO03 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO03
     */
    public java.lang.String getD04_INP_NO03() {
        return d04_INP_NO03;
    }


    /**
     * Sets the d04_INP_NO03 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO03
     */
    public void setD04_INP_NO03(java.lang.String d04_INP_NO03) {
        this.d04_INP_NO03 = d04_INP_NO03;
    }


    /**
     * Gets the d04_INP_NO04 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO04
     */
    public java.lang.String getD04_INP_NO04() {
        return d04_INP_NO04;
    }


    /**
     * Sets the d04_INP_NO04 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO04
     */
    public void setD04_INP_NO04(java.lang.String d04_INP_NO04) {
        this.d04_INP_NO04 = d04_INP_NO04;
    }


    /**
     * Gets the d04_INP_NO05 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO05
     */
    public java.lang.String getD04_INP_NO05() {
        return d04_INP_NO05;
    }


    /**
     * Sets the d04_INP_NO05 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO05
     */
    public void setD04_INP_NO05(java.lang.String d04_INP_NO05) {
        this.d04_INP_NO05 = d04_INP_NO05;
    }


    /**
     * Gets the d04_INP_NO06 value for this D04InputRefundBean.
     * 
     * @return d04_INP_NO06
     */
    public java.lang.String getD04_INP_NO06() {
        return d04_INP_NO06;
    }


    /**
     * Sets the d04_INP_NO06 value for this D04InputRefundBean.
     * 
     * @param d04_INP_NO06
     */
    public void setD04_INP_NO06(java.lang.String d04_INP_NO06) {
        this.d04_INP_NO06 = d04_INP_NO06;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof D04InputRefundBean)) return false;
        D04InputRefundBean other = (D04InputRefundBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.d04_INP_NO01==null && other.getD04_INP_NO01()==null) || 
             (this.d04_INP_NO01!=null &&
              this.d04_INP_NO01.equals(other.getD04_INP_NO01()))) &&
            ((this.d04_INP_NO02==null && other.getD04_INP_NO02()==null) || 
             (this.d04_INP_NO02!=null &&
              this.d04_INP_NO02.equals(other.getD04_INP_NO02()))) &&
            ((this.d04_INP_NO03==null && other.getD04_INP_NO03()==null) || 
             (this.d04_INP_NO03!=null &&
              this.d04_INP_NO03.equals(other.getD04_INP_NO03()))) &&
            ((this.d04_INP_NO04==null && other.getD04_INP_NO04()==null) || 
             (this.d04_INP_NO04!=null &&
              this.d04_INP_NO04.equals(other.getD04_INP_NO04()))) &&
            ((this.d04_INP_NO05==null && other.getD04_INP_NO05()==null) || 
             (this.d04_INP_NO05!=null &&
              this.d04_INP_NO05.equals(other.getD04_INP_NO05()))) &&
            ((this.d04_INP_NO06==null && other.getD04_INP_NO06()==null) || 
             (this.d04_INP_NO06!=null &&
              this.d04_INP_NO06.equals(other.getD04_INP_NO06())));
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
        if (getD04_INP_NO01() != null) {
            _hashCode += getD04_INP_NO01().hashCode();
        }
        if (getD04_INP_NO02() != null) {
            _hashCode += getD04_INP_NO02().hashCode();
        }
        if (getD04_INP_NO03() != null) {
            _hashCode += getD04_INP_NO03().hashCode();
        }
        if (getD04_INP_NO04() != null) {
            _hashCode += getD04_INP_NO04().hashCode();
        }
        if (getD04_INP_NO05() != null) {
            _hashCode += getD04_INP_NO05().hashCode();
        }
        if (getD04_INP_NO06() != null) {
            _hashCode += getD04_INP_NO06().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(D04InputRefundBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ts.com/services/DrugstorePayment", "d04InputRefundBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO01");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO01"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO02");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO02"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO03");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO03"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO04");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO04"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO05");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO05"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("d04_INP_NO06");
        elemField.setXmlName(new javax.xml.namespace.QName("", "d04_INP_NO06"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
