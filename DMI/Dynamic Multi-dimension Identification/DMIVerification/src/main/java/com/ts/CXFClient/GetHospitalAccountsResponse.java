package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getHospitalAccountsResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getHospitalAccountsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.ts.com/services/MedicarePayment}i36OutputHospitalAccountsBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getHospitalAccountsResponse", propOrder = { "_return" })
public class GetHospitalAccountsResponse {

	@XmlElement(name = "return")
	protected I36OutputHospitalAccountsBean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link I36OutputHospitalAccountsBean }
	 * 
	 */
	public I36OutputHospitalAccountsBean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link I36OutputHospitalAccountsBean }
	 * 
	 */
	public void setReturn(I36OutputHospitalAccountsBean value) {
		this._return = value;
	}

}
