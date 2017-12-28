package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getHospitalDetailQuery complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getHospitalDetailQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.ts.com/services/MedicarePayment}i41InputHospitalDetailQueryBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getHospitalDetailQuery", propOrder = { "arg0" })
public class GetHospitalDetailQuery {

	protected I41InputHospitalDetailQueryBean arg0;

	/**
	 * Gets the value of the arg0 property.
	 * 
	 * @return possible object is {@link I41InputHospitalDetailQueryBean }
	 * 
	 */
	public I41InputHospitalDetailQueryBean getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 * 
	 * @param value
	 *            allowed object is {@link I41InputHospitalDetailQueryBean }
	 * 
	 */
	public void setArg0(I41InputHospitalDetailQueryBean value) {
		this.arg0 = value;
	}

}
