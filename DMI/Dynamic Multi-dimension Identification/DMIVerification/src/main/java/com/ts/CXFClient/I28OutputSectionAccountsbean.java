package com.ts.CXFClient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for i28OutputSectionAccountsbean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="i28OutputSectionAccountsbean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="i28_OUT_LIST01" type="{http://www.ts.com/services/MedicarePayment}i28OutTransfiniteDetailListBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_NO09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="i28_OUT_SUB01" type="{http://www.ts.com/services/MedicarePayment}i28OutCalculationResultInfoBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="i28_OUT_SUB02" type="{http://www.ts.com/services/MedicarePayment}i28OutSegmentedInfoStructureBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "i28OutputSectionAccountsbean", propOrder = { "i28OUTLIST01",
		"i28OUTNO01", "i28OUTNO02", "i28OUTNO03", "i28OUTNO04", "i28OUTNO05",
		"i28OUTNO06", "i28OUTNO07", "i28OUTNO08", "i28OUTNO09", "i28OUTSUB01",
		"i28OUTSUB02" })
public class I28OutputSectionAccountsbean {

	@XmlElement(name = "i28_OUT_LIST01", nillable = true)
	protected List<I28OutTransfiniteDetailListBean> i28OUTLIST01;
	@XmlElement(name = "i28_OUT_NO01")
	protected String i28OUTNO01;
	@XmlElement(name = "i28_OUT_NO02")
	protected String i28OUTNO02;
	@XmlElement(name = "i28_OUT_NO03")
	protected String i28OUTNO03;
	@XmlElement(name = "i28_OUT_NO04")
	protected String i28OUTNO04;
	@XmlElement(name = "i28_OUT_NO05")
	protected String i28OUTNO05;
	@XmlElement(name = "i28_OUT_NO06")
	protected String i28OUTNO06;
	@XmlElement(name = "i28_OUT_NO07")
	protected String i28OUTNO07;
	@XmlElement(name = "i28_OUT_NO08")
	protected String i28OUTNO08;
	@XmlElement(name = "i28_OUT_NO09")
	protected String i28OUTNO09;
	@XmlElement(name = "i28_OUT_SUB01", nillable = true)
	protected List<I28OutCalculationResultInfoBean> i28OUTSUB01;
	@XmlElement(name = "i28_OUT_SUB02", nillable = true)
	protected List<I28OutSegmentedInfoStructureBean> i28OUTSUB02;

	/**
	 * Gets the value of the i28OUTLIST01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i28OUTLIST01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI28OUTLIST01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I28OutTransfiniteDetailListBean }
	 * 
	 * 
	 */
	public List<I28OutTransfiniteDetailListBean> getI28OUTLIST01() {
		if (i28OUTLIST01 == null) {
			i28OUTLIST01 = new ArrayList<I28OutTransfiniteDetailListBean>();
		}
		return this.i28OUTLIST01;
	}

	/**
	 * Gets the value of the i28OUTNO01 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO01() {
		return i28OUTNO01;
	}

	/**
	 * Sets the value of the i28OUTNO01 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO01(String value) {
		this.i28OUTNO01 = value;
	}

	/**
	 * Gets the value of the i28OUTNO02 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO02() {
		return i28OUTNO02;
	}

	/**
	 * Sets the value of the i28OUTNO02 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO02(String value) {
		this.i28OUTNO02 = value;
	}

	/**
	 * Gets the value of the i28OUTNO03 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO03() {
		return i28OUTNO03;
	}

	/**
	 * Sets the value of the i28OUTNO03 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO03(String value) {
		this.i28OUTNO03 = value;
	}

	/**
	 * Gets the value of the i28OUTNO04 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO04() {
		return i28OUTNO04;
	}

	/**
	 * Sets the value of the i28OUTNO04 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO04(String value) {
		this.i28OUTNO04 = value;
	}

	/**
	 * Gets the value of the i28OUTNO05 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO05() {
		return i28OUTNO05;
	}

	/**
	 * Sets the value of the i28OUTNO05 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO05(String value) {
		this.i28OUTNO05 = value;
	}

	/**
	 * Gets the value of the i28OUTNO06 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO06() {
		return i28OUTNO06;
	}

	/**
	 * Sets the value of the i28OUTNO06 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO06(String value) {
		this.i28OUTNO06 = value;
	}

	/**
	 * Gets the value of the i28OUTNO07 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO07() {
		return i28OUTNO07;
	}

	/**
	 * Sets the value of the i28OUTNO07 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO07(String value) {
		this.i28OUTNO07 = value;
	}

	/**
	 * Gets the value of the i28OUTNO08 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO08() {
		return i28OUTNO08;
	}

	/**
	 * Sets the value of the i28OUTNO08 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO08(String value) {
		this.i28OUTNO08 = value;
	}

	/**
	 * Gets the value of the i28OUTNO09 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getI28OUTNO09() {
		return i28OUTNO09;
	}

	/**
	 * Sets the value of the i28OUTNO09 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setI28OUTNO09(String value) {
		this.i28OUTNO09 = value;
	}

	/**
	 * Gets the value of the i28OUTSUB01 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i28OUTSUB01 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI28OUTSUB01().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I28OutCalculationResultInfoBean }
	 * 
	 * 
	 */
	public List<I28OutCalculationResultInfoBean> getI28OUTSUB01() {
		if (i28OUTSUB01 == null) {
			i28OUTSUB01 = new ArrayList<I28OutCalculationResultInfoBean>();
		}
		return this.i28OUTSUB01;
	}

	/**
	 * Gets the value of the i28OUTSUB02 property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the i28OUTSUB02 property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getI28OUTSUB02().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link I28OutSegmentedInfoStructureBean }
	 * 
	 * 
	 */
	public List<I28OutSegmentedInfoStructureBean> getI28OUTSUB02() {
		if (i28OUTSUB02 == null) {
			i28OUTSUB02 = new ArrayList<I28OutSegmentedInfoStructureBean>();
		}
		return this.i28OUTSUB02;
	}

}
