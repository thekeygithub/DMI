package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i27InputPreSectionAccountsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i27InputPreSectionAccountsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i27_INP_LIST01" type="{http://www.ts.com/services/MedicarePayment}i27InListDocumentsBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i27_INP_LIST02" type="{http://www.ts.com/services/MedicarePayment}i27InListChargesBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_INP_NO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i27InputPreSectionAccountsBean", propOrder = { "i27INPLIST01",
		"i27INPLIST02", "i27INPNO01", "i27INPNO02", "i27INPNO03", "i27INPNO04",
		"i27INPNO05", "i27INPNO06", "i27INPNO07", "i27INPNO08", "i27INPNO09",
		"i27INPNO10", "i27INPNO11", "i27INPNO12", "i27INPNO13" })
public class I27InputPreSectionAccountsBean {

	@XmlElement(name = "i27_INP_LIST01", nillable = true)
	protected List<I27InListDocumentsBean> i27INPLIST01;
	@XmlElement(name = "i27_INP_LIST02", nillable = true)
	protected List<I27InListChargesBean> i27INPLIST02;
	@XmlElement(name = "i27_INP_NO01")
	protected String i27INPNO01;
	@XmlElement(name = "i27_INP_NO02")
	protected String i27INPNO02;
	@XmlElement(name = "i27_INP_NO03")
	protected String i27INPNO03;
	@XmlElement(name = "i27_INP_NO04")
	protected String i27INPNO04;
	@XmlElement(name = "i27_INP_NO05")
	protected String i27INPNO05;
	@XmlElement(name = "i27_INP_NO06")
	protected String i27INPNO06;
	@XmlElement(name = "i27_INP_NO07")
	protected String i27INPNO07;
	@XmlElement(name = "i27_INP_NO08")
	protected String i27INPNO08;
	@XmlElement(name = "i27_INP_NO09")
	protected String i27INPNO09;
	@XmlElement(name = "i27_INP_NO10")
	protected String i27INPNO10;
	@XmlElement(name = "i27_INP_NO11")
	protected String i27INPNO11;
	@XmlElement(name = "i27_INP_NO12")
	protected String i27INPNO12;
	@XmlElement(name = "i27_INP_NO13")
	protected String i27INPNO13;

	/**
	 * Gets the value of the i27INPLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i27INPLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI27INPLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I27InListDocumentsBean }
	 * 
	 * 
	 */
	public List<I27InListDocumentsBean> getI27INPLIST01() {
		if (i27INPLIST01 == null) {
			i27INPLIST01 = new ArrayList<I27InListDocumentsBean>();
		}
		return this.i27INPLIST01;
	}

	/**
	 * Gets the value of the i27INPLIST02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i27INPLIST02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI27INPLIST02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I27InListChargesBean }
	 * 
	 * 
	 */
	public List<I27InListChargesBean> getI27INPLIST02() {
		if (i27INPLIST02 == null) {
			i27INPLIST02 = new ArrayList<I27InListChargesBean>();
		}
		return this.i27INPLIST02;
	}

	/**
	 * Gets the value of the i27INPNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO01() {
		return i27INPNO01;
	}

	/**
	 * Sets the value of the i27INPNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO01(String value) {
		this.i27INPNO01 = value;
	}

	/**
	 * Gets the value of the i27INPNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO02() {
		return i27INPNO02;
	}

	/**
	 * Sets the value of the i27INPNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO02(String value) {
		this.i27INPNO02 = value;
	}

	/**
	 * Gets the value of the i27INPNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO03() {
		return i27INPNO03;
	}

	/**
	 * Sets the value of the i27INPNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO03(String value) {
		this.i27INPNO03 = value;
	}

	/**
	 * Gets the value of the i27INPNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO04() {
		return i27INPNO04;
	}

	/**
	 * Sets the value of the i27INPNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO04(String value) {
		this.i27INPNO04 = value;
	}

	/**
	 * Gets the value of the i27INPNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO05() {
		return i27INPNO05;
	}

	/**
	 * Sets the value of the i27INPNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO05(String value) {
		this.i27INPNO05 = value;
	}

	/**
	 * Gets the value of the i27INPNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO06() {
		return i27INPNO06;
	}

	/**
	 * Sets the value of the i27INPNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO06(String value) {
		this.i27INPNO06 = value;
	}

	/**
	 * Gets the value of the i27INPNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO07() {
		return i27INPNO07;
	}

	/**
	 * Sets the value of the i27INPNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO07(String value) {
		this.i27INPNO07 = value;
	}

	/**
	 * Gets the value of the i27INPNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO08() {
		return i27INPNO08;
	}

	/**
	 * Sets the value of the i27INPNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO08(String value) {
		this.i27INPNO08 = value;
	}

	/**
	 * Gets the value of the i27INPNO09 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO09() {
		return i27INPNO09;
	}

	/**
	 * Sets the value of the i27INPNO09 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO09(String value) {
		this.i27INPNO09 = value;
	}

	/**
	 * Gets the value of the i27INPNO10 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO10() {
		return i27INPNO10;
	}

	/**
	 * Sets the value of the i27INPNO10 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO10(String value) {
		this.i27INPNO10 = value;
	}

	/**
	 * Gets the value of the i27INPNO11 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO11() {
		return i27INPNO11;
	}

	/**
	 * Sets the value of the i27INPNO11 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO11(String value) {
		this.i27INPNO11 = value;
	}

	/**
	 * Gets the value of the i27INPNO12 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO12() {
		return i27INPNO12;
	}

	/**
	 * Sets the value of the i27INPNO12 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO12(String value) {
		this.i27INPNO12 = value;
	}

	/**
	 * Gets the value of the i27INPNO13 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27INPNO13() {
		return i27INPNO13;
	}

	/**
	 * Sets the value of the i27INPNO13 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27INPNO13(String value) {
		this.i27INPNO13 = value;
	}

}
