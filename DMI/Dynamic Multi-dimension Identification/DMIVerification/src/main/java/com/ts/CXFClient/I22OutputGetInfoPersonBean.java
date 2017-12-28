package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i22OutputGetInfoPersonBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i22OutputGetInfoPersonBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i22_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_NO09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i22_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i22OutPersonalInformationBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i22OutputGetInfoPersonBean", propOrder = { "i22OUTNO01",
		"i22OUTNO02", "i22OUTNO03", "i22OUTNO04", "i22OUTNO05", "i22OUTNO06",
		"i22OUTNO07", "i22OUTNO08", "i22OUTNO09", "i22OUTSUB01" })
public class I22OutputGetInfoPersonBean {

	@XmlElement(name = "i22_OUT_NO01")
	protected String i22OUTNO01;
	@XmlElement(name = "i22_OUT_NO02")
	protected String i22OUTNO02;
	@XmlElement(name = "i22_OUT_NO03")
	protected String i22OUTNO03;
	@XmlElement(name = "i22_OUT_NO04")
	protected String i22OUTNO04;
	@XmlElement(name = "i22_OUT_NO05")
	protected String i22OUTNO05;
	@XmlElement(name = "i22_OUT_NO06")
	protected String i22OUTNO06;
	@XmlElement(name = "i22_OUT_NO07")
	protected String i22OUTNO07;
	@XmlElement(name = "i22_OUT_NO08")
	protected String i22OUTNO08;
	@XmlElement(name = "i22_OUT_NO09")
	protected String i22OUTNO09;
	@XmlElement(name = "i22_OUT_SUB01", nillable = true)
	protected List<I22OutPersonalInformationBean> i22OUTSUB01;

	/**
	 * Gets the value of the i22OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO01() {
		return i22OUTNO01;
	}

	/**
	 * Sets the value of the i22OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO01(String value) {
		this.i22OUTNO01 = value;
	}

	/**
	 * Gets the value of the i22OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO02() {
		return i22OUTNO02;
	}

	/**
	 * Sets the value of the i22OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO02(String value) {
		this.i22OUTNO02 = value;
	}

	/**
	 * Gets the value of the i22OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO03() {
		return i22OUTNO03;
	}

	/**
	 * Sets the value of the i22OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO03(String value) {
		this.i22OUTNO03 = value;
	}

	/**
	 * Gets the value of the i22OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO04() {
		return i22OUTNO04;
	}

	/**
	 * Sets the value of the i22OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO04(String value) {
		this.i22OUTNO04 = value;
	}

	/**
	 * Gets the value of the i22OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO05() {
		return i22OUTNO05;
	}

	/**
	 * Sets the value of the i22OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO05(String value) {
		this.i22OUTNO05 = value;
	}

	/**
	 * Gets the value of the i22OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO06() {
		return i22OUTNO06;
	}

	/**
	 * Sets the value of the i22OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO06(String value) {
		this.i22OUTNO06 = value;
	}

	/**
	 * Gets the value of the i22OUTNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO07() {
		return i22OUTNO07;
	}

	/**
	 * Sets the value of the i22OUTNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO07(String value) {
		this.i22OUTNO07 = value;
	}

	/**
	 * Gets the value of the i22OUTNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO08() {
		return i22OUTNO08;
	}

	/**
	 * Sets the value of the i22OUTNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO08(String value) {
		this.i22OUTNO08 = value;
	}

	/**
	 * Gets the value of the i22OUTNO09 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI22OUTNO09() {
		return i22OUTNO09;
	}

	/**
	 * Sets the value of the i22OUTNO09 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI22OUTNO09(String value) {
		this.i22OUTNO09 = value;
	}

	/**
	 * Gets the value of the i22OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i22OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI22OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I22OutPersonalInformationBean }
	 * 
	 * 
	 */
	public List<I22OutPersonalInformationBean> getI22OUTSUB01() {
		if (i22OUTSUB01 == null) {
			i22OUTSUB01 = new ArrayList<I22OutPersonalInformationBean>();
		}
		return this.i22OUTSUB01;
	}

}
