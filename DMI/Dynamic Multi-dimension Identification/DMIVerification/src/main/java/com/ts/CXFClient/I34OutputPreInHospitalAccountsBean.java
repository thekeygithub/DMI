package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i34OutputPreInHospitalAccountsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i34OutputPreInHospitalAccountsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i34_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i34_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i34OutCalculationResultBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i34_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i34OutFundSegmentBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i34OutputPreInHospitalAccountsBean", propOrder = {
		"i34OUTNO01", "i34OUTNO02", "i34OUTNO03", "i34OUTNO04", "i34OUTNO05",
		"i34OUTNO06", "i34OUTSUB01", "i34OUTSUB02" })
public class I34OutputPreInHospitalAccountsBean {

	@XmlElement(name = "i34_OUT_NO01")
	protected String i34OUTNO01;
	@XmlElement(name = "i34_OUT_NO02")
	protected String i34OUTNO02;
	@XmlElement(name = "i34_OUT_NO03")
	protected String i34OUTNO03;
	@XmlElement(name = "i34_OUT_NO04")
	protected String i34OUTNO04;
	@XmlElement(name = "i34_OUT_NO05")
	protected String i34OUTNO05;
	@XmlElement(name = "i34_OUT_NO06")
	protected String i34OUTNO06;
	@XmlElement(name = "i34_OUT_SUB01", nillable = true)
	protected List<I34OutCalculationResultBean> i34OUTSUB01;
	@XmlElement(name = "i34_OUT_SUB02", nillable = true)
	protected List<I34OutFundSegmentBean> i34OUTSUB02;

	/**
	 * Gets the value of the i34OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO01() {
		return i34OUTNO01;
	}

	/**
	 * Sets the value of the i34OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO01(String value) {
		this.i34OUTNO01 = value;
	}

	/**
	 * Gets the value of the i34OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO02() {
		return i34OUTNO02;
	}

	/**
	 * Sets the value of the i34OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO02(String value) {
		this.i34OUTNO02 = value;
	}

	/**
	 * Gets the value of the i34OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO03() {
		return i34OUTNO03;
	}

	/**
	 * Sets the value of the i34OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO03(String value) {
		this.i34OUTNO03 = value;
	}

	/**
	 * Gets the value of the i34OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO04() {
		return i34OUTNO04;
	}

	/**
	 * Sets the value of the i34OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO04(String value) {
		this.i34OUTNO04 = value;
	}

	/**
	 * Gets the value of the i34OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO05() {
		return i34OUTNO05;
	}

	/**
	 * Sets the value of the i34OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO05(String value) {
		this.i34OUTNO05 = value;
	}

	/**
	 * Gets the value of the i34OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI34OUTNO06() {
		return i34OUTNO06;
	}

	/**
	 * Sets the value of the i34OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI34OUTNO06(String value) {
		this.i34OUTNO06 = value;
	}

	/**
	 * Gets the value of the i34OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i34OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI34OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I34OutCalculationResultBean }
	 * 
	 * 
	 */
	public List<I34OutCalculationResultBean> getI34OUTSUB01() {
		if (i34OUTSUB01 == null) {
			i34OUTSUB01 = new ArrayList<I34OutCalculationResultBean>();
		}
		return this.i34OUTSUB01;
	}

	/**
	 * Gets the value of the i34OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i34OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI34OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I34OutFundSegmentBean }
	 * 
	 * 
	 */
	public List<I34OutFundSegmentBean> getI34OUTSUB02() {
		if (i34OUTSUB02 == null) {
			i34OUTSUB02 = new ArrayList<I34OutFundSegmentBean>();
		}
		return this.i34OUTSUB02;
	}

}
