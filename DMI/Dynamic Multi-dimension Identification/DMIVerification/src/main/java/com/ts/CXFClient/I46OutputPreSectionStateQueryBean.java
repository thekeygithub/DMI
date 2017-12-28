package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i46OutputPreSectionStateQueryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i46OutputPreSectionStateQueryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i46_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i46_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i46OutCalculationResultInformationBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i46_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i46OutFundSegmentInformationBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i46OutputPreSectionStateQueryBean", propOrder = {
		"i46OUTNO01", "i46OUTNO02", "i46OUTNO03", "i46OUTNO04", "i46OUTNO05",
		"i46OUTNO06", "i46OUTSUB01", "i46OUTSUB02" })
public class I46OutputPreSectionStateQueryBean {

	@XmlElement(name = "i46_OUT_NO01")
	protected String i46OUTNO01;
	@XmlElement(name = "i46_OUT_NO02")
	protected String i46OUTNO02;
	@XmlElement(name = "i46_OUT_NO03")
	protected String i46OUTNO03;
	@XmlElement(name = "i46_OUT_NO04")
	protected String i46OUTNO04;
	@XmlElement(name = "i46_OUT_NO05")
	protected String i46OUTNO05;
	@XmlElement(name = "i46_OUT_NO06")
	protected String i46OUTNO06;
	@XmlElement(name = "i46_OUT_SUB01", nillable = true)
	protected List<I46OutCalculationResultInformationBean> i46OUTSUB01;
	@XmlElement(name = "i46_OUT_SUB02", nillable = true)
	protected List<I46OutFundSegmentInformationBean> i46OUTSUB02;

	/**
	 * Gets the value of the i46OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO01() {
		return i46OUTNO01;
	}

	/**
	 * Sets the value of the i46OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO01(String value) {
		this.i46OUTNO01 = value;
	}

	/**
	 * Gets the value of the i46OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO02() {
		return i46OUTNO02;
	}

	/**
	 * Sets the value of the i46OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO02(String value) {
		this.i46OUTNO02 = value;
	}

	/**
	 * Gets the value of the i46OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO03() {
		return i46OUTNO03;
	}

	/**
	 * Sets the value of the i46OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO03(String value) {
		this.i46OUTNO03 = value;
	}

	/**
	 * Gets the value of the i46OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO04() {
		return i46OUTNO04;
	}

	/**
	 * Sets the value of the i46OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO04(String value) {
		this.i46OUTNO04 = value;
	}

	/**
	 * Gets the value of the i46OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO05() {
		return i46OUTNO05;
	}

	/**
	 * Sets the value of the i46OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO05(String value) {
		this.i46OUTNO05 = value;
	}

	/**
	 * Gets the value of the i46OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI46OUTNO06() {
		return i46OUTNO06;
	}

	/**
	 * Sets the value of the i46OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI46OUTNO06(String value) {
		this.i46OUTNO06 = value;
	}

	/**
	 * Gets the value of the i46OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i46OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI46OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I46OutCalculationResultInformationBean }
	 * 
	 * 
	 */
	public List<I46OutCalculationResultInformationBean> getI46OUTSUB01() {
		if (i46OUTSUB01 == null) {
			i46OUTSUB01 = new ArrayList<I46OutCalculationResultInformationBean>();
		}
		return this.i46OUTSUB01;
	}

	/**
	 * Gets the value of the i46OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i46OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI46OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I46OutFundSegmentInformationBean }
	 * 
	 * 
	 */
	public List<I46OutFundSegmentInformationBean> getI46OUTSUB02() {
		if (i46OUTSUB02 == null) {
			i46OUTSUB02 = new ArrayList<I46OutFundSegmentInformationBean>();
		}
		return this.i46OUTSUB02;
	}

}
