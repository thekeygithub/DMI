package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i43OutCostSummaryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i43OutCostSummaryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i43_OUT_SUB03_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB03_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB03_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB03_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB03_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i43OutCostSummaryBean", propOrder = { "i43OUTSUB03NO01",
		"i43OUTSUB03NO02", "i43OUTSUB03NO03", "i43OUTSUB03NO04",
		"i43OUTSUB03NO05" })
public class I43OutCostSummaryBean {

	@XmlElement(name = "i43_OUT_SUB03_NO01")
	protected String i43OUTSUB03NO01;
	@XmlElement(name = "i43_OUT_SUB03_NO02")
	protected String i43OUTSUB03NO02;
	@XmlElement(name = "i43_OUT_SUB03_NO03")
	protected String i43OUTSUB03NO03;
	@XmlElement(name = "i43_OUT_SUB03_NO04")
	protected String i43OUTSUB03NO04;
	@XmlElement(name = "i43_OUT_SUB03_NO05")
	protected String i43OUTSUB03NO05;

	/**
	 * Gets the value of the i43OUTSUB03NO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTSUB03NO01() {
		return i43OUTSUB03NO01;
	}

	/**
	 * Sets the value of the i43OUTSUB03NO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTSUB03NO01(String value) {
		this.i43OUTSUB03NO01 = value;
	}

	/**
	 * Gets the value of the i43OUTSUB03NO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTSUB03NO02() {
		return i43OUTSUB03NO02;
	}

	/**
	 * Sets the value of the i43OUTSUB03NO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTSUB03NO02(String value) {
		this.i43OUTSUB03NO02 = value;
	}

	/**
	 * Gets the value of the i43OUTSUB03NO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTSUB03NO03() {
		return i43OUTSUB03NO03;
	}

	/**
	 * Sets the value of the i43OUTSUB03NO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTSUB03NO03(String value) {
		this.i43OUTSUB03NO03 = value;
	}

	/**
	 * Gets the value of the i43OUTSUB03NO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTSUB03NO04() {
		return i43OUTSUB03NO04;
	}

	/**
	 * Sets the value of the i43OUTSUB03NO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTSUB03NO04(String value) {
		this.i43OUTSUB03NO04 = value;
	}

	/**
	 * Gets the value of the i43OUTSUB03NO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTSUB03NO05() {
		return i43OUTSUB03NO05;
	}

	/**
	 * Sets the value of the i43OUTSUB03NO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTSUB03NO05(String value) {
		this.i43OUTSUB03NO05 = value;
	}

}
