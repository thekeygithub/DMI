package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i36OutputHospitalAccountsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i36OutputHospitalAccountsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i36_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO07" type="{http://www.ts.com/services/MedicarePayment}i36OutCalculationResultBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO08" type="{http://www.ts.com/services/MedicarePayment}i36OutFundSegmentBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO09" type="{http://www.ts.com/services/MedicarePayment}i36OutCostSummaryBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i36_OUT_NO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i36OutputHospitalAccountsBean", propOrder = { "i36OUTNO01",
		"i36OUTNO02", "i36OUTNO03", "i36OUTNO04", "i36OUTNO05", "i36OUTNO06",
		"i36OUTNO07", "i36OUTNO08", "i36OUTNO09", "i36OUTNO10" })
public class I36OutputHospitalAccountsBean {

	@XmlElement(name = "i36_OUT_NO01")
	protected String i36OUTNO01;
	@XmlElement(name = "i36_OUT_NO02")
	protected String i36OUTNO02;
	@XmlElement(name = "i36_OUT_NO03")
	protected String i36OUTNO03;
	@XmlElement(name = "i36_OUT_NO04")
	protected String i36OUTNO04;
	@XmlElement(name = "i36_OUT_NO05")
	protected String i36OUTNO05;
	@XmlElement(name = "i36_OUT_NO06")
	protected String i36OUTNO06;
	@XmlElement(name = "i36_OUT_NO07", nillable = true)
	protected List<I36OutCalculationResultBean> i36OUTNO07;
	@XmlElement(name = "i36_OUT_NO08", nillable = true)
	protected List<I36OutFundSegmentBean> i36OUTNO08;
	@XmlElement(name = "i36_OUT_NO09", nillable = true)
	protected List<I36OutCostSummaryBean> i36OUTNO09;
	@XmlElement(name = "i36_OUT_NO10")
	protected String i36OUTNO10;

	/**
	 * Gets the value of the i36OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO01() {
		return i36OUTNO01;
	}

	/**
	 * Sets the value of the i36OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO01(String value) {
		this.i36OUTNO01 = value;
	}

	/**
	 * Gets the value of the i36OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO02() {
		return i36OUTNO02;
	}

	/**
	 * Sets the value of the i36OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO02(String value) {
		this.i36OUTNO02 = value;
	}

	/**
	 * Gets the value of the i36OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO03() {
		return i36OUTNO03;
	}

	/**
	 * Sets the value of the i36OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO03(String value) {
		this.i36OUTNO03 = value;
	}

	/**
	 * Gets the value of the i36OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO04() {
		return i36OUTNO04;
	}

	/**
	 * Sets the value of the i36OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO04(String value) {
		this.i36OUTNO04 = value;
	}

	/**
	 * Gets the value of the i36OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO05() {
		return i36OUTNO05;
	}

	/**
	 * Sets the value of the i36OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO05(String value) {
		this.i36OUTNO05 = value;
	}

	/**
	 * Gets the value of the i36OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO06() {
		return i36OUTNO06;
	}

	/**
	 * Sets the value of the i36OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO06(String value) {
		this.i36OUTNO06 = value;
	}

	/**
	 * Gets the value of the i36OUTNO07 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i36OUTNO07 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI36OUTNO07().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I36OutCalculationResultBean }
	 * 
	 * 
	 */
	public List<I36OutCalculationResultBean> getI36OUTNO07() {
		if (i36OUTNO07 == null) {
			i36OUTNO07 = new ArrayList<I36OutCalculationResultBean>();
		}
		return this.i36OUTNO07;
	}

	/**
	 * Gets the value of the i36OUTNO08 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i36OUTNO08 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI36OUTNO08().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I36OutFundSegmentBean }
	 * 
	 * 
	 */
	public List<I36OutFundSegmentBean> getI36OUTNO08() {
		if (i36OUTNO08 == null) {
			i36OUTNO08 = new ArrayList<I36OutFundSegmentBean>();
		}
		return this.i36OUTNO08;
	}

	/**
	 * Gets the value of the i36OUTNO09 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i36OUTNO09 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI36OUTNO09().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I36OutCostSummaryBean }
	 * 
	 * 
	 */
	public List<I36OutCostSummaryBean> getI36OUTNO09() {
		if (i36OUTNO09 == null) {
			i36OUTNO09 = new ArrayList<I36OutCostSummaryBean>();
		}
		return this.i36OUTNO09;
	}

	/**
	 * Gets the value of the i36OUTNO10 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI36OUTNO10() {
		return i36OUTNO10;
	}

	/**
	 * Sets the value of the i36OUTNO10 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI36OUTNO10(String value) {
		this.i36OUTNO10 = value;
	}

}
