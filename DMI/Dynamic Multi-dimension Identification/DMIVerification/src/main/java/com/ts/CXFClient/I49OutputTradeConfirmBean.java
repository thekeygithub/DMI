package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i49OutputTradeConfirmBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i49OutputTradeConfirmBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i49_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i49_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i49_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i49_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i49_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i49_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i49OutputTradeConfirmBean", propOrder = { "i49OUTNO01",
		"i49OUTNO02", "i49OUTNO03", "i49OUTNO04", "i49OUTNO05", "i49OUTNO06" })
public class I49OutputTradeConfirmBean {

	@XmlElement(name = "i49_OUT_NO01")
	protected String i49OUTNO01;
	@XmlElement(name = "i49_OUT_NO02")
	protected String i49OUTNO02;
	@XmlElement(name = "i49_OUT_NO03")
	protected String i49OUTNO03;
	@XmlElement(name = "i49_OUT_NO04")
	protected String i49OUTNO04;
	@XmlElement(name = "i49_OUT_NO05")
	protected String i49OUTNO05;
	@XmlElement(name = "i49_OUT_NO06")
	protected String i49OUTNO06;

	/**
	 * Gets the value of the i49OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO01() {
		return i49OUTNO01;
	}

	/**
	 * Sets the value of the i49OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO01(String value) {
		this.i49OUTNO01 = value;
	}

	/**
	 * Gets the value of the i49OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO02() {
		return i49OUTNO02;
	}

	/**
	 * Sets the value of the i49OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO02(String value) {
		this.i49OUTNO02 = value;
	}

	/**
	 * Gets the value of the i49OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO03() {
		return i49OUTNO03;
	}

	/**
	 * Sets the value of the i49OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO03(String value) {
		this.i49OUTNO03 = value;
	}

	/**
	 * Gets the value of the i49OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO04() {
		return i49OUTNO04;
	}

	/**
	 * Sets the value of the i49OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO04(String value) {
		this.i49OUTNO04 = value;
	}

	/**
	 * Gets the value of the i49OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO05() {
		return i49OUTNO05;
	}

	/**
	 * Sets the value of the i49OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO05(String value) {
		this.i49OUTNO05 = value;
	}

	/**
	 * Gets the value of the i49OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI49OUTNO06() {
		return i49OUTNO06;
	}

	/**
	 * Sets the value of the i49OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI49OUTNO06(String value) {
		this.i49OUTNO06 = value;
	}

}
