package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i41OutputHospitalDetailQueryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i41OutputHospitalDetailQueryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i41_OUT_LIST01" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i41_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i41_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i41OutputHospitalDetailQueryBean", propOrder = {
		"i41OUTLIST01", "i41OUTNO01", "i41OUTNO02", "i41OUTNO03", "i41OUTNO04",
		"i41OUTNO05" })
public class I41OutputHospitalDetailQueryBean {

	@XmlElement(name = "i41_OUT_LIST01", nillable = true)
	protected List<String> i41OUTLIST01;
	@XmlElement(name = "i41_OUT_NO01")
	protected String i41OUTNO01;
	@XmlElement(name = "i41_OUT_NO02")
	protected String i41OUTNO02;
	@XmlElement(name = "i41_OUT_NO03")
	protected String i41OUTNO03;
	@XmlElement(name = "i41_OUT_NO04")
	protected String i41OUTNO04;
	@XmlElement(name = "i41_OUT_NO05")
	protected String i41OUTNO05;

	/**
	 * Gets the value of the i41OUTLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i41OUTLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI41OUTLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getI41OUTLIST01() {
		if (i41OUTLIST01 == null) {
			i41OUTLIST01 = new ArrayList<String>();
		}
		return this.i41OUTLIST01;
	}

	/**
	 * Gets the value of the i41OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41OUTNO01() {
		return i41OUTNO01;
	}

	/**
	 * Sets the value of the i41OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41OUTNO01(String value) {
		this.i41OUTNO01 = value;
	}

	/**
	 * Gets the value of the i41OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41OUTNO02() {
		return i41OUTNO02;
	}

	/**
	 * Sets the value of the i41OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41OUTNO02(String value) {
		this.i41OUTNO02 = value;
	}

	/**
	 * Gets the value of the i41OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41OUTNO03() {
		return i41OUTNO03;
	}

	/**
	 * Sets the value of the i41OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41OUTNO03(String value) {
		this.i41OUTNO03 = value;
	}

	/**
	 * Gets the value of the i41OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41OUTNO04() {
		return i41OUTNO04;
	}

	/**
	 * Sets the value of the i41OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41OUTNO04(String value) {
		this.i41OUTNO04 = value;
	}

	/**
	 * Gets the value of the i41OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI41OUTNO05() {
		return i41OUTNO05;
	}

	/**
	 * Sets the value of the i41OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI41OUTNO05(String value) {
		this.i41OUTNO05 = value;
	}

}
