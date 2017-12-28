package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getTradeResultsQuery complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getTradeResultsQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.ts.com/services/MedicarePayment}i43InputTradeResultsQueryBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTradeResultsQuery", propOrder = { "arg0" })
public class GetTradeResultsQuery {

	protected I43InputTradeResultsQueryBean arg0;

	/**
	 * Gets the value of the arg0 property.
	 * 
	 * @return possible object is {@link I43InputTradeResultsQueryBean }
	 * 
	 */
	public I43InputTradeResultsQueryBean getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 * 
	 * @param value
	 *            allowed object is {@link I43InputTradeResultsQueryBean }
	 * 
	 */
	public void setArg0(I43InputTradeResultsQueryBean value) {
		this.arg0 = value;
	}

}
