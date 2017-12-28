package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i43OutputTradeResultsQueryBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i43OutputTradeResultsQueryBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i43_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_NO09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i43OutCalculationResultBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i43OutFundSegmentBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i43_OUT_SUB03" type="{http://www.ts.com/services/MedicarePayment}i43OutCostSummaryBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i43OutputTradeResultsQueryBean", propOrder = { "i43OUTNO01",
		"i43OUTNO02", "i43OUTNO03", "i43OUTNO04", "i43OUTNO05", "i43OUTNO06",
		"i43OUTNO07", "i43OUTNO08", "i43OUTNO09", "i43OUTSUB01", "i43OUTSUB02",
		"i43OUTSUB03" })
public class I43OutputTradeResultsQueryBean {

	@XmlElement(name = "i43_OUT_NO01")
	protected String i43OUTNO01;
	@XmlElement(name = "i43_OUT_NO02")
	protected String i43OUTNO02;
	@XmlElement(name = "i43_OUT_NO03")
	protected String i43OUTNO03;
	@XmlElement(name = "i43_OUT_NO04")
	protected String i43OUTNO04;
	@XmlElement(name = "i43_OUT_NO05")
	protected String i43OUTNO05;
	@XmlElement(name = "i43_OUT_NO06")
	protected String i43OUTNO06;
	@XmlElement(name = "i43_OUT_NO07")
	protected String i43OUTNO07;
	@XmlElement(name = "i43_OUT_NO08")
	protected String i43OUTNO08;
	@XmlElement(name = "i43_OUT_NO09")
	protected String i43OUTNO09;
	@XmlElement(name = "i43_OUT_SUB01", nillable = true)
	protected List<I43OutCalculationResultBean> i43OUTSUB01;
	@XmlElement(name = "i43_OUT_SUB02", nillable = true)
	protected List<I43OutFundSegmentBean> i43OUTSUB02;
	@XmlElement(name = "i43_OUT_SUB03", nillable = true)
	protected List<I43OutCostSummaryBean> i43OUTSUB03;

	/**
	 * Gets the value of the i43OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO01() {
		return i43OUTNO01;
	}

	/**
	 * Sets the value of the i43OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO01(String value) {
		this.i43OUTNO01 = value;
	}

	/**
	 * Gets the value of the i43OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO02() {
		return i43OUTNO02;
	}

	/**
	 * Sets the value of the i43OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO02(String value) {
		this.i43OUTNO02 = value;
	}

	/**
	 * Gets the value of the i43OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO03() {
		return i43OUTNO03;
	}

	/**
	 * Sets the value of the i43OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO03(String value) {
		this.i43OUTNO03 = value;
	}

	/**
	 * Gets the value of the i43OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO04() {
		return i43OUTNO04;
	}

	/**
	 * Sets the value of the i43OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO04(String value) {
		this.i43OUTNO04 = value;
	}

	/**
	 * Gets the value of the i43OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO05() {
		return i43OUTNO05;
	}

	/**
	 * Sets the value of the i43OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO05(String value) {
		this.i43OUTNO05 = value;
	}

	/**
	 * Gets the value of the i43OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO06() {
		return i43OUTNO06;
	}

	/**
	 * Sets the value of the i43OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO06(String value) {
		this.i43OUTNO06 = value;
	}

	/**
	 * Gets the value of the i43OUTNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO07() {
		return i43OUTNO07;
	}

	/**
	 * Sets the value of the i43OUTNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO07(String value) {
		this.i43OUTNO07 = value;
	}

	/**
	 * Gets the value of the i43OUTNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO08() {
		return i43OUTNO08;
	}

	/**
	 * Sets the value of the i43OUTNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO08(String value) {
		this.i43OUTNO08 = value;
	}

	/**
	 * Gets the value of the i43OUTNO09 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI43OUTNO09() {
		return i43OUTNO09;
	}

	/**
	 * Sets the value of the i43OUTNO09 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI43OUTNO09(String value) {
		this.i43OUTNO09 = value;
	}

	/**
	 * Gets the value of the i43OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i43OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI43OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I43OutCalculationResultBean }
	 * 
	 * 
	 */
	public List<I43OutCalculationResultBean> getI43OUTSUB01() {
		if (i43OUTSUB01 == null) {
			i43OUTSUB01 = new ArrayList<I43OutCalculationResultBean>();
		}
		return this.i43OUTSUB01;
	}

	/**
	 * Gets the value of the i43OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i43OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI43OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I43OutFundSegmentBean }
	 * 
	 * 
	 */
	public List<I43OutFundSegmentBean> getI43OUTSUB02() {
		if (i43OUTSUB02 == null) {
			i43OUTSUB02 = new ArrayList<I43OutFundSegmentBean>();
		}
		return this.i43OUTSUB02;
	}

	/**
	 * Gets the value of the i43OUTSUB03 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i43OUTSUB03 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI43OUTSUB03().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I43OutCostSummaryBean }
	 * 
	 * 
	 */
	public List<I43OutCostSummaryBean> getI43OUTSUB03() {
		if (i43OUTSUB03 == null) {
			i43OUTSUB03 = new ArrayList<I43OutCostSummaryBean>();
		}
		return this.i43OUTSUB03;
	}

}
