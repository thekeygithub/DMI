package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i41InputHospitalDetailQueryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i41InputHospitalDetailQueryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i41_INP_LIST01" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_INP_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i41InputHospitalDetailQueryBean", propOrder = {
		"i41INPLIST01", "i41INPNO01", "i41INPNO02", "i41INPNO03", "i41INPNO04",
		"i41INPNO05", "i41INPNO06", "i41INPNO07", "i41INPNO08" })
public class I41InputHospitalDetailQueryBean {

	@XmlElement(name = "i41_INP_LIST01", nillable = true)
	protected List<String> i41INPLIST01;
	@XmlElement(name = "i41_INP_NO01")
	protected String i41INPNO01;
	@XmlElement(name = "i41_INP_NO02")
	protected String i41INPNO02;
	@XmlElement(name = "i41_INP_NO03")
	protected String i41INPNO03;
	@XmlElement(name = "i41_INP_NO04")
	protected String i41INPNO04;
	@XmlElement(name = "i41_INP_NO05")
	protected String i41INPNO05;
	@XmlElement(name = "i41_INP_NO06")
	protected String i41INPNO06;
	@XmlElement(name = "i41_INP_NO07")
	protected String i41INPNO07;
	@XmlElement(name = "i41_INP_NO08")
	protected String i41INPNO08;

	/**
	 * Gets the value of the i41INPLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i41INPLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI41INPLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getI41INPLIST01() {
		if (i41INPLIST01 == null) {
			i41INPLIST01 = new ArrayList<String>();
		}
		return this.i41INPLIST01;
	}

	/**
	 * Gets the value of the i41INPNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO01() {
		return i41INPNO01;
	}

	/**
	 * Sets the value of the i41INPNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO01(String value) {
		this.i41INPNO01 = value;
	}

	/**
	 * Gets the value of the i41INPNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO02() {
		return i41INPNO02;
	}

	/**
	 * Sets the value of the i41INPNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO02(String value) {
		this.i41INPNO02 = value;
	}

	/**
	 * Gets the value of the i41INPNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO03() {
		return i41INPNO03;
	}

	/**
	 * Sets the value of the i41INPNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO03(String value) {
		this.i41INPNO03 = value;
	}

	/**
	 * Gets the value of the i41INPNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO04() {
		return i41INPNO04;
	}

	/**
	 * Sets the value of the i41INPNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO04(String value) {
		this.i41INPNO04 = value;
	}

	/**
	 * Gets the value of the i41INPNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO05() {
		return i41INPNO05;
	}

	/**
	 * Sets the value of the i41INPNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO05(String value) {
		this.i41INPNO05 = value;
	}

	/**
	 * Gets the value of the i41INPNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO06() {
		return i41INPNO06;
	}

	/**
	 * Sets the value of the i41INPNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO06(String value) {
		this.i41INPNO06 = value;
	}

	/**
	 * Gets the value of the i41INPNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO07() {
		return i41INPNO07;
	}

	/**
	 * Sets the value of the i41INPNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO07(String value) {
		this.i41INPNO07 = value;
	}

	/**
	 * Gets the value of the i41INPNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41INPNO08() {
		return i41INPNO08;
	}

	/**
	 * Sets the value of the i41INPNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41INPNO08(String value) {
		this.i41INPNO08 = value;
	}

}
