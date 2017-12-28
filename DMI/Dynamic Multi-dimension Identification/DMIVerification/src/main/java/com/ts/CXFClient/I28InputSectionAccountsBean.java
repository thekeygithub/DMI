package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i28InputSectionAccountsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i28InputSectionAccountsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i28_INP_LIST01" type="{http://www.ts.com/services/MedicarePayment}i28InListDocumentsBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i28_INP_LIST02" type="{http://www.ts.com/services/MedicarePayment}i28InListChargesBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_INP_NO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i28InputSectionAccountsBean", propOrder = { "i28INPLIST01",
		"i28INPLIST02", "i28INPNO01", "i28INPNO02", "i28INPNO03", "i28INPNO04",
		"i28INPNO05", "i28INPNO06", "i28INPNO07", "i28INPNO08", "i28INPNO09",
		"i28INPNO10", "i28INPNO11", "i28INPNO12", "i28INPNO13", "i28INPNO14" })
public class I28InputSectionAccountsBean {

	@XmlElement(name = "i28_INP_LIST01", nillable = true)
	protected List<I28InListDocumentsBean> i28INPLIST01;
	@XmlElement(name = "i28_INP_LIST02", nillable = true)
	protected List<I28InListChargesBean> i28INPLIST02;
	@XmlElement(name = "i28_INP_NO01")
	protected String i28INPNO01;
	@XmlElement(name = "i28_INP_NO02")
	protected String i28INPNO02;
	@XmlElement(name = "i28_INP_NO03")
	protected String i28INPNO03;
	@XmlElement(name = "i28_INP_NO04")
	protected String i28INPNO04;
	@XmlElement(name = "i28_INP_NO05")
	protected String i28INPNO05;
	@XmlElement(name = "i28_INP_NO06")
	protected String i28INPNO06;
	@XmlElement(name = "i28_INP_NO07")
	protected String i28INPNO07;
	@XmlElement(name = "i28_INP_NO08")
	protected String i28INPNO08;
	@XmlElement(name = "i28_INP_NO09")
	protected String i28INPNO09;
	@XmlElement(name = "i28_INP_NO10")
	protected String i28INPNO10;
	@XmlElement(name = "i28_INP_NO11")
	protected String i28INPNO11;
	@XmlElement(name = "i28_INP_NO12")
	protected String i28INPNO12;
	@XmlElement(name = "i28_INP_NO13")
	protected String i28INPNO13;
	@XmlElement(name = "i28_INP_NO14")
	protected String i28INPNO14;

	/**
	 * Gets the value of the i28INPLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i28INPLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI28INPLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I28InListDocumentsBean }
	 * 
	 * 
	 */
	public List<I28InListDocumentsBean> getI28INPLIST01() {
		if (i28INPLIST01 == null) {
			i28INPLIST01 = new ArrayList<I28InListDocumentsBean>();
		}
		return this.i28INPLIST01;
	}

	/**
	 * Gets the value of the i28INPLIST02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i28INPLIST02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI28INPLIST02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I28InListChargesBean }
	 * 
	 * 
	 */
	public List<I28InListChargesBean> getI28INPLIST02() {
		if (i28INPLIST02 == null) {
			i28INPLIST02 = new ArrayList<I28InListChargesBean>();
		}
		return this.i28INPLIST02;
	}

	/**
	 * Gets the value of the i28INPNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO01() {
		return i28INPNO01;
	}

	/**
	 * Sets the value of the i28INPNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO01(String value) {
		this.i28INPNO01 = value;
	}

	/**
	 * Gets the value of the i28INPNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO02() {
		return i28INPNO02;
	}

	/**
	 * Sets the value of the i28INPNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO02(String value) {
		this.i28INPNO02 = value;
	}

	/**
	 * Gets the value of the i28INPNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO03() {
		return i28INPNO03;
	}

	/**
	 * Sets the value of the i28INPNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO03(String value) {
		this.i28INPNO03 = value;
	}

	/**
	 * Gets the value of the i28INPNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO04() {
		return i28INPNO04;
	}

	/**
	 * Sets the value of the i28INPNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO04(String value) {
		this.i28INPNO04 = value;
	}

	/**
	 * Gets the value of the i28INPNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO05() {
		return i28INPNO05;
	}

	/**
	 * Sets the value of the i28INPNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO05(String value) {
		this.i28INPNO05 = value;
	}

	/**
	 * Gets the value of the i28INPNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO06() {
		return i28INPNO06;
	}

	/**
	 * Sets the value of the i28INPNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO06(String value) {
		this.i28INPNO06 = value;
	}

	/**
	 * Gets the value of the i28INPNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO07() {
		return i28INPNO07;
	}

	/**
	 * Sets the value of the i28INPNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO07(String value) {
		this.i28INPNO07 = value;
	}

	/**
	 * Gets the value of the i28INPNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO08() {
		return i28INPNO08;
	}

	/**
	 * Sets the value of the i28INPNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO08(String value) {
		this.i28INPNO08 = value;
	}

	/**
	 * Gets the value of the i28INPNO09 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO09() {
		return i28INPNO09;
	}

	/**
	 * Sets the value of the i28INPNO09 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO09(String value) {
		this.i28INPNO09 = value;
	}

	/**
	 * Gets the value of the i28INPNO10 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO10() {
		return i28INPNO10;
	}

	/**
	 * Sets the value of the i28INPNO10 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO10(String value) {
		this.i28INPNO10 = value;
	}

	/**
	 * Gets the value of the i28INPNO11 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO11() {
		return i28INPNO11;
	}

	/**
	 * Sets the value of the i28INPNO11 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO11(String value) {
		this.i28INPNO11 = value;
	}

	/**
	 * Gets the value of the i28INPNO12 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO12() {
		return i28INPNO12;
	}

	/**
	 * Sets the value of the i28INPNO12 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO12(String value) {
		this.i28INPNO12 = value;
	}

	/**
	 * Gets the value of the i28INPNO13 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO13() {
		return i28INPNO13;
	}

	/**
	 * Sets the value of the i28INPNO13 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO13(String value) {
		this.i28INPNO13 = value;
	}

	/**
	 * Gets the value of the i28INPNO14 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28INPNO14() {
		return i28INPNO14;
	}

	/**
	 * Sets the value of the i28INPNO14 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28INPNO14(String value) {
		this.i28INPNO14 = value;
	}

}
