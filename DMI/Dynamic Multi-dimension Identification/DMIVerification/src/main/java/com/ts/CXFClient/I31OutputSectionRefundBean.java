package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i31OutputSectionRefundBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i31OutputSectionRefundBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i31_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i31_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i31OutCalculationResultInfoBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i31_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i31OutSegmentedInfoStructureBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i31OutputSectionRefundBean", propOrder = { "i31OUTNO01",
		"i31OUTNO02", "i31OUTNO03", "i31OUTNO04", "i31OUTNO05", "i31OUTNO06",
		"i31OUTNO07", "i31OUTNO08", "i31OUTSUB01", "i31OUTSUB02" })
public class I31OutputSectionRefundBean {

	@XmlElement(name = "i31_OUT_NO01")
	protected String i31OUTNO01;
	@XmlElement(name = "i31_OUT_NO02")
	protected String i31OUTNO02;
	@XmlElement(name = "i31_OUT_NO03")
	protected String i31OUTNO03;
	@XmlElement(name = "i31_OUT_NO04")
	protected String i31OUTNO04;
	@XmlElement(name = "i31_OUT_NO05")
	protected String i31OUTNO05;
	@XmlElement(name = "i31_OUT_NO06")
	protected String i31OUTNO06;
	@XmlElement(name = "i31_OUT_NO07")
	protected String i31OUTNO07;
	@XmlElement(name = "i31_OUT_NO08")
	protected String i31OUTNO08;
	@XmlElement(name = "i31_OUT_SUB01", nillable = true)
	protected List<I31OutCalculationResultInfoBean> i31OUTSUB01;
	@XmlElement(name = "i31_OUT_SUB02", nillable = true)
	protected List<I31OutSegmentedInfoStructureBean> i31OUTSUB02;

	/**
	 * Gets the value of the i31OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO01() {
		return i31OUTNO01;
	}

	/**
	 * Sets the value of the i31OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO01(String value) {
		this.i31OUTNO01 = value;
	}

	/**
	 * Gets the value of the i31OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO02() {
		return i31OUTNO02;
	}

	/**
	 * Sets the value of the i31OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO02(String value) {
		this.i31OUTNO02 = value;
	}

	/**
	 * Gets the value of the i31OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO03() {
		return i31OUTNO03;
	}

	/**
	 * Sets the value of the i31OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO03(String value) {
		this.i31OUTNO03 = value;
	}

	/**
	 * Gets the value of the i31OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO04() {
		return i31OUTNO04;
	}

	/**
	 * Sets the value of the i31OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO04(String value) {
		this.i31OUTNO04 = value;
	}

	/**
	 * Gets the value of the i31OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO05() {
		return i31OUTNO05;
	}

	/**
	 * Sets the value of the i31OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO05(String value) {
		this.i31OUTNO05 = value;
	}

	/**
	 * Gets the value of the i31OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO06() {
		return i31OUTNO06;
	}

	/**
	 * Sets the value of the i31OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO06(String value) {
		this.i31OUTNO06 = value;
	}

	/**
	 * Gets the value of the i31OUTNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO07() {
		return i31OUTNO07;
	}

	/**
	 * Sets the value of the i31OUTNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO07(String value) {
		this.i31OUTNO07 = value;
	}

	/**
	 * Gets the value of the i31OUTNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI31OUTNO08() {
		return i31OUTNO08;
	}

	/**
	 * Sets the value of the i31OUTNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI31OUTNO08(String value) {
		this.i31OUTNO08 = value;
	}

	/**
	 * Gets the value of the i31OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i31OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI31OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I31OutCalculationResultInfoBean }
	 * 
	 * 
	 */
	public List<I31OutCalculationResultInfoBean> getI31OUTSUB01() {
		if (i31OUTSUB01 == null) {
			i31OUTSUB01 = new ArrayList<I31OutCalculationResultInfoBean>();
		}
		return this.i31OUTSUB01;
	}

	/**
	 * Gets the value of the i31OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i31OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI31OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I31OutSegmentedInfoStructureBean }
	 * 
	 * 
	 */
	public List<I31OutSegmentedInfoStructureBean> getI31OUTSUB02() {
		if (i31OUTSUB02 == null) {
			i31OUTSUB02 = new ArrayList<I31OutSegmentedInfoStructureBean>();
		}
		return this.i31OUTSUB02;
	}

}
