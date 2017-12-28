package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getPersonalInformation complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getPersonalInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.ts.com/services/MedicarePayment}i22InputGetInfoPersonBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPersonalInformation", propOrder = { "arg0" })
public class GetPersonalInformation {

	protected I22InputGetInfoPersonBean arg0;

	/**
	 * Gets the value of the arg0 property.
	 * 
	 * @return possible object is {@link I22InputGetInfoPersonBean }
	 * 
	 */
	public I22InputGetInfoPersonBean getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 * 
	 * @param value
	 *            allowed object is {@link I22InputGetInfoPersonBean }
	 * 
	 */
	public void setArg0(I22InputGetInfoPersonBean value) {
		this.arg0 = value;
	}

}
