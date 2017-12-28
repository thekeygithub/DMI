package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i22InputGetInfoPersonBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i22InputGetInfoPersonBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i22_INP_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_INP_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i22InputGetInfoPersonBean", propOrder = { "i22INPNO01",
		"i22INPNO02", "i22INPNO03", "i22INPNO04", "i22INPNO05", "i22INPNO06",
		"i22INPNO07" })
public class I22InputGetInfoPersonBean {

	@XmlElement(name = "i22_INP_NO01")
	protected String i22INPNO01;
	@XmlElement(name = "i22_INP_NO02")
	protected String i22INPNO02;
	@XmlElement(name = "i22_INP_NO03")
	protected String i22INPNO03;
	@XmlElement(name = "i22_INP_NO04")
	protected String i22INPNO04;
	@XmlElement(name = "i22_INP_NO05")
	protected String i22INPNO05;
	@XmlElement(name = "i22_INP_NO06")
	protected String i22INPNO06;
	@XmlElement(name = "i22_INP_NO07")
	protected String i22INPNO07;

	/**
	 * Gets the value of the i22INPNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO01() {
		return i22INPNO01;
	}

	/**
	 * Sets the value of the i22INPNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO01(String value) {
		this.i22INPNO01 = value;
	}

	/**
	 * Gets the value of the i22INPNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO02() {
		return i22INPNO02;
	}

	/**
	 * Sets the value of the i22INPNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO02(String value) {
		this.i22INPNO02 = value;
	}

	/**
	 * Gets the value of the i22INPNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO03() {
		return i22INPNO03;
	}

	/**
	 * Sets the value of the i22INPNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO03(String value) {
		this.i22INPNO03 = value;
	}

	/**
	 * Gets the value of the i22INPNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO04() {
		return i22INPNO04;
	}

	/**
	 * Sets the value of the i22INPNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO04(String value) {
		this.i22INPNO04 = value;
	}

	/**
	 * Gets the value of the i22INPNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO05() {
		return i22INPNO05;
	}

	/**
	 * Sets the value of the i22INPNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO05(String value) {
		this.i22INPNO05 = value;
	}

	/**
	 * Gets the value of the i22INPNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO06() {
		return i22INPNO06;
	}

	/**
	 * Sets the value of the i22INPNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO06(String value) {
		this.i22INPNO06 = value;
	}

	/**
	 * Gets the value of the i22INPNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22INPNO07() {
		return i22INPNO07;
	}

	/**
	 * Sets the value of the i22INPNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22INPNO07(String value) {
		this.i22INPNO07 = value;
	}

}
