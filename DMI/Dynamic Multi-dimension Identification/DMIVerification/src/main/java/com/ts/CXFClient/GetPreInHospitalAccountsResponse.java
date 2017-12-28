package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getPreInHospitalAccountsResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getPreInHospitalAccountsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.ts.com/services/MedicarePayment}i34OutputPreInHospitalAccountsBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPreInHospitalAccountsResponse", propOrder = { "_return" })
public class GetPreInHospitalAccountsResponse {

	@XmlElement(name = "return")
	protected I34OutputPreInHospitalAccountsBean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link I34OutputPreInHospitalAccountsBean }
	 * 
	 */
	public I34OutputPreInHospitalAccountsBean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link I34OutputPreInHospitalAccountsBean }
	 * 
	 */
	public void setReturn(I34OutputPreInHospitalAccountsBean value) {
		this._return = value;
	}

}
