package com.ts.CXFClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i36OutCostSummaryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i36OutCostSummaryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i36_OUT_NO09_SUB01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO09_SUB02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO09_SUB03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO09_SUB04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO09_SUB05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i36OutCostSummaryBean", propOrder = { "i36OUTNO09SUB01",
		"i36OUTNO09SUB02", "i36OUTNO09SUB03", "i36OUTNO09SUB04",
		"i36OUTNO09SUB05" })
public class I36OutCostSummaryBean {

	@XmlElement(name = "i36_OUT_NO09_SUB01")
	protected String i36OUTNO09SUB01;
	@XmlElement(name = "i36_OUT_NO09_SUB02")
	protected String i36OUTNO09SUB02;
	@XmlElement(name = "i36_OUT_NO09_SUB03")
	protected String i36OUTNO09SUB03;
	@XmlElement(name = "i36_OUT_NO09_SUB04")
	protected String i36OUTNO09SUB04;
	@XmlElement(name = "i36_OUT_NO09_SUB05")
	protected String i36OUTNO09SUB05;

	/**
	 * Gets the value of the i36OUTNO09SUB01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO09SUB01() {
		return i36OUTNO09SUB01;
	}

	/**
	 * Sets the value of the i36OUTNO09SUB01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO09SUB01(String value) {
		this.i36OUTNO09SUB01 = value;
	}

	/**
	 * Gets the value of the i36OUTNO09SUB02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO09SUB02() {
		return i36OUTNO09SUB02;
	}

	/**
	 * Sets the value of the i36OUTNO09SUB02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO09SUB02(String value) {
		this.i36OUTNO09SUB02 = value;
	}

	/**
	 * Gets the value of the i36OUTNO09SUB03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO09SUB03() {
		return i36OUTNO09SUB03;
	}

	/**
	 * Sets the value of the i36OUTNO09SUB03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO09SUB03(String value) {
		this.i36OUTNO09SUB03 = value;
	}

	/**
	 * Gets the value of the i36OUTNO09SUB04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO09SUB04() {
		return i36OUTNO09SUB04;
	}

	/**
	 * Sets the value of the i36OUTNO09SUB04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO09SUB04(String value) {
		this.i36OUTNO09SUB04 = value;
	}

	/**
	 * Gets the value of the i36OUTNO09SUB05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO09SUB05() {
		return i36OUTNO09SUB05;
	}

	/**
	 * Sets the value of the i36OUTNO09SUB05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO09SUB05(String value) {
		this.i36OUTNO09SUB05 = value;
	}

}
