package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i28InListDocumentsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i28InListDocumentsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i28_INP_LIST01_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_LIST01_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_LIST01_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_LIST01_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_LIST01_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i28InListDocumentsBean", propOrder = { "i28INPLIST01NO01",
		"i28INPLIST01NO02", "i28INPLIST01NO03", "i28INPLIST01NO04",
		"i28INPLIST01NO05" })
public class I28InListDocumentsBean {

	@XmlElement(name = "i28_INP_LIST01_NO01")
	protected String i28INPLIST01NO01;
	@XmlElement(name = "i28_INP_LIST01_NO02")
	protected String i28INPLIST01NO02;
	@XmlElement(name = "i28_INP_LIST01_NO03")
	protected String i28INPLIST01NO03;
	@XmlElement(name = "i28_INP_LIST01_NO04")
	protected String i28INPLIST01NO04;
	@XmlElement(name = "i28_INP_LIST01_NO05")
	protected String i28INPLIST01NO05;

	/**
	 * Gets the value of the i28INPLIST01NO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPLIST01NO01() {
		return i28INPLIST01NO01;
	}

	/**
	 * Sets the value of the i28INPLIST01NO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPLIST01NO01(String value) {
		this.i28INPLIST01NO01 = value;
	}

	/**
	 * Gets the value of the i28INPLIST01NO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPLIST01NO02() {
		return i28INPLIST01NO02;
	}

	/**
	 * Sets the value of the i28INPLIST01NO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPLIST01NO02(String value) {
		this.i28INPLIST01NO02 = value;
	}

	/**
	 * Gets the value of the i28INPLIST01NO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPLIST01NO03() {
		return i28INPLIST01NO03;
	}

	/**
	 * Sets the value of the i28INPLIST01NO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPLIST01NO03(String value) {
		this.i28INPLIST01NO03 = value;
	}

	/**
	 * Gets the value of the i28INPLIST01NO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPLIST01NO04() {
		return i28INPLIST01NO04;
	}

	/**
	 * Sets the value of the i28INPLIST01NO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPLIST01NO04(String value) {
		this.i28INPLIST01NO04 = value;
	}

	/**
	 * Gets the value of the i28INPLIST01NO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPLIST01NO05() {
		return i28INPLIST01NO05;
	}

	/**
	 * Sets the value of the i28INPLIST01NO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPLIST01NO05(String value) {
		this.i28INPLIST01NO05 = value;
	}

}
