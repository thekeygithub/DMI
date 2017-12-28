package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i27OutputPreSectionAccountsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i27OutputPreSectionAccountsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i27_OUT_LIST01" type="{http://www.ts.com/services/MedicarePayment}i27OutTransfiniteDetailListBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i27_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i27OutCalculationResultInfoBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i27_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i27OutSegmentedInfoStructureBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i27OutputPreSectionAccountsBean", propOrder = {
		"i27OUTLIST01", "i27OUTNO01", "i27OUTNO02", "i27OUTNO03", "i27OUTNO04",
		"i27OUTNO05", "i27OUTNO06", "i27OUTNO07", "i27OUTNO08", "i27OUTSUB01",
		"i27OUTSUB02" })
public class I27OutputPreSectionAccountsBean {

	@XmlElement(name = "i27_OUT_LIST01", nillable = true)
	protected List<I27OutTransfiniteDetailListBean> i27OUTLIST01;
	@XmlElement(name = "i27_OUT_NO01")
	protected String i27OUTNO01;
	@XmlElement(name = "i27_OUT_NO02")
	protected String i27OUTNO02;
	@XmlElement(name = "i27_OUT_NO03")
	protected String i27OUTNO03;
	@XmlElement(name = "i27_OUT_NO04")
	protected String i27OUTNO04;
	@XmlElement(name = "i27_OUT_NO05")
	protected String i27OUTNO05;
	@XmlElement(name = "i27_OUT_NO06")
	protected String i27OUTNO06;
	@XmlElement(name = "i27_OUT_NO07")
	protected String i27OUTNO07;
	@XmlElement(name = "i27_OUT_NO08")
	protected String i27OUTNO08;
	@XmlElement(name = "i27_OUT_SUB01", nillable = true)
	protected List<I27OutCalculationResultInfoBean> i27OUTSUB01;
	@XmlElement(name = "i27_OUT_SUB02", nillable = true)
	protected List<I27OutSegmentedInfoStructureBean> i27OUTSUB02;

	/**
	 * Gets the value of the i27OUTLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i27OUTLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI27OUTLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I27OutTransfiniteDetailListBean }
	 * 
	 * 
	 */
	public List<I27OutTransfiniteDetailListBean> getI27OUTLIST01() {
		if (i27OUTLIST01 == null) {
			i27OUTLIST01 = new ArrayList<I27OutTransfiniteDetailListBean>();
		}
		return this.i27OUTLIST01;
	}

	/**
	 * Gets the value of the i27OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO01() {
		return i27OUTNO01;
	}

	/**
	 * Sets the value of the i27OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO01(String value) {
		this.i27OUTNO01 = value;
	}

	/**
	 * Gets the value of the i27OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO02() {
		return i27OUTNO02;
	}

	/**
	 * Sets the value of the i27OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO02(String value) {
		this.i27OUTNO02 = value;
	}

	/**
	 * Gets the value of the i27OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO03() {
		return i27OUTNO03;
	}

	/**
	 * Sets the value of the i27OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO03(String value) {
		this.i27OUTNO03 = value;
	}

	/**
	 * Gets the value of the i27OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO04() {
		return i27OUTNO04;
	}

	/**
	 * Sets the value of the i27OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO04(String value) {
		this.i27OUTNO04 = value;
	}

	/**
	 * Gets the value of the i27OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO05() {
		return i27OUTNO05;
	}

	/**
	 * Sets the value of the i27OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO05(String value) {
		this.i27OUTNO05 = value;
	}

	/**
	 * Gets the value of the i27OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO06() {
		return i27OUTNO06;
	}

	/**
	 * Sets the value of the i27OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO06(String value) {
		this.i27OUTNO06 = value;
	}

	/**
	 * Gets the value of the i27OUTNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO07() {
		return i27OUTNO07;
	}

	/**
	 * Sets the value of the i27OUTNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO07(String value) {
		this.i27OUTNO07 = value;
	}

	/**
	 * Gets the value of the i27OUTNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI27OUTNO08() {
		return i27OUTNO08;
	}

	/**
	 * Sets the value of the i27OUTNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI27OUTNO08(String value) {
		this.i27OUTNO08 = value;
	}

	/**
	 * Gets the value of the i27OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i27OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI27OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I27OutCalculationResultInfoBean }
	 * 
	 * 
	 */
	public List<I27OutCalculationResultInfoBean> getI27OUTSUB01() {
		if (i27OUTSUB01 == null) {
			i27OUTSUB01 = new ArrayList<I27OutCalculationResultInfoBean>();
		}
		return this.i27OUTSUB01;
	}

	/**
	 * Gets the value of the i27OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i27OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI27OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I27OutSegmentedInfoStructureBean }
	 * 
	 * 
	 */
	public List<I27OutSegmentedInfoStructureBean> getI27OUTSUB02() {
		if (i27OUTSUB02 == null) {
			i27OUTSUB02 = new ArrayList<I27OutSegmentedInfoStructureBean>();
		}
		return this.i27OUTSUB02;
	}

}
