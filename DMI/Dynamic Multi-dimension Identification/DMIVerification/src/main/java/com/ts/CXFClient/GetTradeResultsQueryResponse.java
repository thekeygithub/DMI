package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getTradeResultsQueryResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getTradeResultsQueryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.ts.com/services/MedicarePayment}i43OutputTradeResultsQueryBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTradeResultsQueryResponse", propOrder = { "_return" })
public class GetTradeResultsQueryResponse {

	@XmlElement(name = "return")
	protected I43OutputTradeResultsQueryBean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link I43OutputTradeResultsQueryBean }
	 * 
	 */
	public I43OutputTradeResultsQueryBean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link I43OutputTradeResultsQueryBean }
	 * 
	 */
	public void setReturn(I43OutputTradeResultsQueryBean value) {
		this._return = value;
	}

}
