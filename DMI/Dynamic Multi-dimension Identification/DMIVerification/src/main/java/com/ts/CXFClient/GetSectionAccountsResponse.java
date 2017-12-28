package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getSectionAccountsResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getSectionAccountsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.ts.com/services/MedicarePayment}i28OutputSectionAccountsbean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSectionAccountsResponse", propOrder = { "_return" })
public class GetSectionAccountsResponse {

	@XmlElement(name = "return")
	protected I28OutputSectionAccountsbean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link I28OutputSectionAccountsbean }
	 * 
	 */
	public I28OutputSectionAccountsbean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link I28OutputSectionAccountsbean }
	 * 
	 */
	public void setReturn(I28OutputSectionAccountsbean value) {
		this._return = value;
	}

}
