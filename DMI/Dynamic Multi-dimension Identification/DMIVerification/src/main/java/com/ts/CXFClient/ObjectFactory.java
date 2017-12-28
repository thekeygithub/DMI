package com.ts.CXFClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.ts.CXFClient package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _GetHospitalAccounts_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment", "getHospitalAccounts");
	private final static QName _GetSectionRefund_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment", "getSectionRefund");
	private final static QName _GetPersonalInformationResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPersonalInformationResponse");
	private final static QName _GetTradeResultsQueryResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getTradeResultsQueryResponse");
	private final static QName _GetPreInHospitalAccountsResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreInHospitalAccountsResponse");
	private final static QName _GetPreSectionAccounts_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreSectionAccounts");
	private final static QName _GetTradeConfirmResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getTradeConfirmResponse");
	private final static QName _GetHospitalDetailQuery_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getHospitalDetailQuery");
	private final static QName _GetPreInHospitalAccounts_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreInHospitalAccounts");
	private final static QName _GetSectionAccountsResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getSectionAccountsResponse");
	private final static QName _GetSectionRefundResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getSectionRefundResponse");
	private final static QName _GetSectionAccounts_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment", "getSectionAccounts");
	private final static QName _GetTradeResultsQuery_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getTradeResultsQuery");
	private final static QName _GetPreSectionStateQueryResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreSectionStateQueryResponse");
	private final static QName _GetTradeConfirm_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment", "getTradeConfirm");
	private final static QName _GetHospitalAccountsResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getHospitalAccountsResponse");
	private final static QName _GetPreSectionStateQuery_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreSectionStateQuery");
	private final static QName _GetPreSectionAccountsResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPreSectionAccountsResponse");
	private final static QName _GetHospitalDetailQueryResponse_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getHospitalDetailQueryResponse");
	private final static QName _GetPersonalInformation_QNAME = new QName(
			"http://www.ts.com/services/MedicarePayment",
			"getPersonalInformation");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.ts.CXFClient
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link I28InputSectionAccountsBean }
	 * 
	 */
	public I28InputSectionAccountsBean createI28InputSectionAccountsBean() {
		return new I28InputSectionAccountsBean();
	}

	/**
	 * Create an instance of {@link I36OutCostSummaryBean }
	 * 
	 */
	public I36OutCostSummaryBean createI36OutCostSummaryBean() {
		return new I36OutCostSummaryBean();
	}

	/**
	 * Create an instance of {@link I36OutFundSegmentBean }
	 * 
	 */
	public I36OutFundSegmentBean createI36OutFundSegmentBean() {
		return new I36OutFundSegmentBean();
	}

	/**
	 * Create an instance of {@link I28OutCalculationResultInfoBean }
	 * 
	 */
	public I28OutCalculationResultInfoBean createI28OutCalculationResultInfoBean() {
		return new I28OutCalculationResultInfoBean();
	}

	/**
	 * Create an instance of {@link GetPreSectionStateQueryResponse }
	 * 
	 */
	public GetPreSectionStateQueryResponse createGetPreSectionStateQueryResponse() {
		return new GetPreSectionStateQueryResponse();
	}

	/**
	 * Create an instance of {@link GetSectionAccountsResponse }
	 * 
	 */
	public GetSectionAccountsResponse createGetSectionAccountsResponse() {
		return new GetSectionAccountsResponse();
	}

	/**
	 * Create an instance of {@link I46OutputPreSectionStateQueryBean }
	 * 
	 */
	public I46OutputPreSectionStateQueryBean createI46OutputPreSectionStateQueryBean() {
		return new I46OutputPreSectionStateQueryBean();
	}

	/**
	 * Create an instance of {@link I27OutCalculationResultInfoBean }
	 * 
	 */
	public I27OutCalculationResultInfoBean createI27OutCalculationResultInfoBean() {
		return new I27OutCalculationResultInfoBean();
	}

	/**
	 * Create an instance of {@link I43OutCostSummaryBean }
	 * 
	 */
	public I43OutCostSummaryBean createI43OutCostSummaryBean() {
		return new I43OutCostSummaryBean();
	}

	/**
	 * Create an instance of {@link I31OutputSectionRefundBean }
	 * 
	 */
	public I31OutputSectionRefundBean createI31OutputSectionRefundBean() {
		return new I31OutputSectionRefundBean();
	}

	/**
	 * Create an instance of {@link I43OutFundSegmentBean }
	 * 
	 */
	public I43OutFundSegmentBean createI43OutFundSegmentBean() {
		return new I43OutFundSegmentBean();
	}

	/**
	 * Create an instance of {@link GetTradeResultsQuery }
	 * 
	 */
	public GetTradeResultsQuery createGetTradeResultsQuery() {
		return new GetTradeResultsQuery();
	}

	/**
	 * Create an instance of {@link I22OutputGetInfoPersonBean }
	 * 
	 */
	public I22OutputGetInfoPersonBean createI22OutputGetInfoPersonBean() {
		return new I22OutputGetInfoPersonBean();
	}

	/**
	 * Create an instance of {@link I34OutFundSegmentBean }
	 * 
	 */
	public I34OutFundSegmentBean createI34OutFundSegmentBean() {
		return new I34OutFundSegmentBean();
	}

	/**
	 * Create an instance of {@link I27OutSegmentedInfoStructureBean }
	 * 
	 */
	public I27OutSegmentedInfoStructureBean createI27OutSegmentedInfoStructureBean() {
		return new I27OutSegmentedInfoStructureBean();
	}

	/**
	 * Create an instance of {@link I27OutTransfiniteDetailListBean }
	 * 
	 */
	public I27OutTransfiniteDetailListBean createI27OutTransfiniteDetailListBean() {
		return new I27OutTransfiniteDetailListBean();
	}

	/**
	 * Create an instance of {@link I34OutCalculationResultBean }
	 * 
	 */
	public I34OutCalculationResultBean createI34OutCalculationResultBean() {
		return new I34OutCalculationResultBean();
	}

	/**
	 * Create an instance of {@link I31OutCalculationResultInfoBean }
	 * 
	 */
	public I31OutCalculationResultInfoBean createI31OutCalculationResultInfoBean() {
		return new I31OutCalculationResultInfoBean();
	}

	/**
	 * Create an instance of {@link GetPreSectionAccounts }
	 * 
	 */
	public GetPreSectionAccounts createGetPreSectionAccounts() {
		return new GetPreSectionAccounts();
	}

	/**
	 * Create an instance of {@link I46InputPreSectionStateQueryBean }
	 * 
	 */
	public I46InputPreSectionStateQueryBean createI46InputPreSectionStateQueryBean() {
		return new I46InputPreSectionStateQueryBean();
	}

	/**
	 * Create an instance of {@link I36InputHospitalAccountsBean }
	 * 
	 */
	public I36InputHospitalAccountsBean createI36InputHospitalAccountsBean() {
		return new I36InputHospitalAccountsBean();
	}

	/**
	 * Create an instance of {@link I27InputPreSectionAccountsBean }
	 * 
	 */
	public I27InputPreSectionAccountsBean createI27InputPreSectionAccountsBean() {
		return new I27InputPreSectionAccountsBean();
	}

	/**
	 * Create an instance of {@link I27InListDocumentsBean }
	 * 
	 */
	public I27InListDocumentsBean createI27InListDocumentsBean() {
		return new I27InListDocumentsBean();
	}

	/**
	 * Create an instance of {@link I49OutputTradeConfirmBean }
	 * 
	 */
	public I49OutputTradeConfirmBean createI49OutputTradeConfirmBean() {
		return new I49OutputTradeConfirmBean();
	}

	/**
	 * Create an instance of {@link I36OutCalculationResultBean }
	 * 
	 */
	public I36OutCalculationResultBean createI36OutCalculationResultBean() {
		return new I36OutCalculationResultBean();
	}

	/**
	 * Create an instance of {@link I43InputTradeResultsQueryBean }
	 * 
	 */
	public I43InputTradeResultsQueryBean createI43InputTradeResultsQueryBean() {
		return new I43InputTradeResultsQueryBean();
	}

	/**
	 * Create an instance of {@link I31InputSectionRefundBean }
	 * 
	 */
	public I31InputSectionRefundBean createI31InputSectionRefundBean() {
		return new I31InputSectionRefundBean();
	}

	/**
	 * Create an instance of {@link GetSectionAccounts }
	 * 
	 */
	public GetSectionAccounts createGetSectionAccounts() {
		return new GetSectionAccounts();
	}

	/**
	 * Create an instance of {@link GetSectionRefund }
	 * 
	 */
	public GetSectionRefund createGetSectionRefund() {
		return new GetSectionRefund();
	}

	/**
	 * Create an instance of {@link I28InListChargesBean }
	 * 
	 */
	public I28InListChargesBean createI28InListChargesBean() {
		return new I28InListChargesBean();
	}

	/**
	 * Create an instance of {@link I28OutSegmentedInfoStructureBean }
	 * 
	 */
	public I28OutSegmentedInfoStructureBean createI28OutSegmentedInfoStructureBean() {
		return new I28OutSegmentedInfoStructureBean();
	}

	/**
	 * Create an instance of {@link I28OutTransfiniteDetailListBean }
	 * 
	 */
	public I28OutTransfiniteDetailListBean createI28OutTransfiniteDetailListBean() {
		return new I28OutTransfiniteDetailListBean();
	}

	/**
	 * Create an instance of {@link GetTradeConfirm }
	 * 
	 */
	public GetTradeConfirm createGetTradeConfirm() {
		return new GetTradeConfirm();
	}

	/**
	 * Create an instance of {@link GetTradeResultsQueryResponse }
	 * 
	 */
	public GetTradeResultsQueryResponse createGetTradeResultsQueryResponse() {
		return new GetTradeResultsQueryResponse();
	}

	/**
	 * Create an instance of {@link GetPersonalInformation }
	 * 
	 */
	public GetPersonalInformation createGetPersonalInformation() {
		return new GetPersonalInformation();
	}

	/**
	 * Create an instance of {@link I36OutputHospitalAccountsBean }
	 * 
	 */
	public I36OutputHospitalAccountsBean createI36OutputHospitalAccountsBean() {
		return new I36OutputHospitalAccountsBean();
	}

	/**
	 * Create an instance of {@link I43OutCalculationResultBean }
	 * 
	 */
	public I43OutCalculationResultBean createI43OutCalculationResultBean() {
		return new I43OutCalculationResultBean();
	}

	/**
	 * Create an instance of {@link I27OutputPreSectionAccountsBean }
	 * 
	 */
	public I27OutputPreSectionAccountsBean createI27OutputPreSectionAccountsBean() {
		return new I27OutputPreSectionAccountsBean();
	}

	/**
	 * Create an instance of {@link GetPreSectionAccountsResponse }
	 * 
	 */
	public GetPreSectionAccountsResponse createGetPreSectionAccountsResponse() {
		return new GetPreSectionAccountsResponse();
	}

	/**
	 * Create an instance of {@link I28OutputSectionAccountsbean }
	 * 
	 */
	public I28OutputSectionAccountsbean createI28OutputSectionAccountsbean() {
		return new I28OutputSectionAccountsbean();
	}

	/**
	 * Create an instance of {@link I46OutCalculationResultInformationBean }
	 * 
	 */
	public I46OutCalculationResultInformationBean createI46OutCalculationResultInformationBean() {
		return new I46OutCalculationResultInformationBean();
	}

	/**
	 * Create an instance of {@link I22InputGetInfoPersonBean }
	 * 
	 */
	public I22InputGetInfoPersonBean createI22InputGetInfoPersonBean() {
		return new I22InputGetInfoPersonBean();
	}

	/**
	 * Create an instance of {@link I41InputHospitalDetailQueryBean }
	 * 
	 */
	public I41InputHospitalDetailQueryBean createI41InputHospitalDetailQueryBean() {
		return new I41InputHospitalDetailQueryBean();
	}

	/**
	 * Create an instance of {@link I31OutSegmentedInfoStructureBean }
	 * 
	 */
	public I31OutSegmentedInfoStructureBean createI31OutSegmentedInfoStructureBean() {
		return new I31OutSegmentedInfoStructureBean();
	}

	/**
	 * Create an instance of {@link I41OutputHospitalDetailQueryBean }
	 * 
	 */
	public I41OutputHospitalDetailQueryBean createI41OutputHospitalDetailQueryBean() {
		return new I41OutputHospitalDetailQueryBean();
	}

	/**
	 * Create an instance of {@link GetPreInHospitalAccounts }
	 * 
	 */
	public GetPreInHospitalAccounts createGetPreInHospitalAccounts() {
		return new GetPreInHospitalAccounts();
	}

	/**
	 * Create an instance of {@link GetHospitalAccountsResponse }
	 * 
	 */
	public GetHospitalAccountsResponse createGetHospitalAccountsResponse() {
		return new GetHospitalAccountsResponse();
	}

	/**
	 * Create an instance of {@link GetHospitalDetailQuery }
	 * 
	 */
	public GetHospitalDetailQuery createGetHospitalDetailQuery() {
		return new GetHospitalDetailQuery();
	}

	/**
	 * Create an instance of {@link GetHospitalAccounts }
	 * 
	 */
	public GetHospitalAccounts createGetHospitalAccounts() {
		return new GetHospitalAccounts();
	}

	/**
	 * Create an instance of {@link I27InListChargesBean }
	 * 
	 */
	public I27InListChargesBean createI27InListChargesBean() {
		return new I27InListChargesBean();
	}

	/**
	 * Create an instance of {@link I43OutputTradeResultsQueryBean }
	 * 
	 */
	public I43OutputTradeResultsQueryBean createI43OutputTradeResultsQueryBean() {
		return new I43OutputTradeResultsQueryBean();
	}

	/**
	 * Create an instance of {@link I28InListDocumentsBean }
	 * 
	 */
	public I28InListDocumentsBean createI28InListDocumentsBean() {
		return new I28InListDocumentsBean();
	}

	/**
	 * Create an instance of {@link GetTradeConfirmResponse }
	 * 
	 */
	public GetTradeConfirmResponse createGetTradeConfirmResponse() {
		return new GetTradeConfirmResponse();
	}

	/**
	 * Create an instance of {@link I34OutputPreInHospitalAccountsBean }
	 * 
	 */
	public I34OutputPreInHospitalAccountsBean createI34OutputPreInHospitalAccountsBean() {
		return new I34OutputPreInHospitalAccountsBean();
	}

	/**
	 * Create an instance of {@link GetHospitalDetailQueryResponse }
	 * 
	 */
	public GetHospitalDetailQueryResponse createGetHospitalDetailQueryResponse() {
		return new GetHospitalDetailQueryResponse();
	}

	/**
	 * Create an instance of {@link GetSectionRefundResponse }
	 * 
	 */
	public GetSectionRefundResponse createGetSectionRefundResponse() {
		return new GetSectionRefundResponse();
	}

	/**
	 * Create an instance of {@link I22OutPersonalInformationBean }
	 * 
	 */
	public I22OutPersonalInformationBean createI22OutPersonalInformationBean() {
		return new I22OutPersonalInformationBean();
	}

	/**
	 * Create an instance of {@link GetPreSectionStateQuery }
	 * 
	 */
	public GetPreSectionStateQuery createGetPreSectionStateQuery() {
		return new GetPreSectionStateQuery();
	}

	/**
	 * Create an instance of {@link GetPersonalInformationResponse }
	 * 
	 */
	public GetPersonalInformationResponse createGetPersonalInformationResponse() {
		return new GetPersonalInformationResponse();
	}

	/**
	 * Create an instance of {@link I46OutFundSegmentInformationBean }
	 * 
	 */
	public I46OutFundSegmentInformationBean createI46OutFundSegmentInformationBean() {
		return new I46OutFundSegmentInformationBean();
	}

	/**
	 * Create an instance of {@link GetPreInHospitalAccountsResponse }
	 * 
	 */
	public GetPreInHospitalAccountsResponse createGetPreInHospitalAccountsResponse() {
		return new GetPreInHospitalAccountsResponse();
	}

	/**
	 * Create an instance of {@link I34InputPreInHospitalAccountsBean }
	 * 
	 */
	public I34InputPreInHospitalAccountsBean createI34InputPreInHospitalAccountsBean() {
		return new I34InputPreInHospitalAccountsBean();
	}

	/**
	 * Create an instance of {@link I49InputTradeConfirmBean }
	 * 
	 */
	public I49InputTradeConfirmBean createI49InputTradeConfirmBean() {
		return new I49InputTradeConfirmBean();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetHospitalAccounts }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getHospitalAccounts")
	public JAXBElement<GetHospitalAccounts> createGetHospitalAccounts(
			GetHospitalAccounts value) {
		return new JAXBElement<GetHospitalAccounts>(_GetHospitalAccounts_QNAME,
				GetHospitalAccounts.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetSectionRefund }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getSectionRefund")
	public JAXBElement<GetSectionRefund> createGetSectionRefund(
			GetSectionRefund value) {
		return new JAXBElement<GetSectionRefund>(_GetSectionRefund_QNAME,
				GetSectionRefund.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPersonalInformationResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPersonalInformationResponse")
	public JAXBElement<GetPersonalInformationResponse> createGetPersonalInformationResponse(
			GetPersonalInformationResponse value) {
		return new JAXBElement<GetPersonalInformationResponse>(
				_GetPersonalInformationResponse_QNAME,
				GetPersonalInformationResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetTradeResultsQueryResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getTradeResultsQueryResponse")
	public JAXBElement<GetTradeResultsQueryResponse> createGetTradeResultsQueryResponse(
			GetTradeResultsQueryResponse value) {
		return new JAXBElement<GetTradeResultsQueryResponse>(
				_GetTradeResultsQueryResponse_QNAME,
				GetTradeResultsQueryResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreInHospitalAccountsResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreInHospitalAccountsResponse")
	public JAXBElement<GetPreInHospitalAccountsResponse> createGetPreInHospitalAccountsResponse(
			GetPreInHospitalAccountsResponse value) {
		return new JAXBElement<GetPreInHospitalAccountsResponse>(
				_GetPreInHospitalAccountsResponse_QNAME,
				GetPreInHospitalAccountsResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreSectionAccounts }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreSectionAccounts")
	public JAXBElement<GetPreSectionAccounts> createGetPreSectionAccounts(
			GetPreSectionAccounts value) {
		return new JAXBElement<GetPreSectionAccounts>(
				_GetPreSectionAccounts_QNAME, GetPreSectionAccounts.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetTradeConfirmResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getTradeConfirmResponse")
	public JAXBElement<GetTradeConfirmResponse> createGetTradeConfirmResponse(
			GetTradeConfirmResponse value) {
		return new JAXBElement<GetTradeConfirmResponse>(
				_GetTradeConfirmResponse_QNAME, GetTradeConfirmResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetHospitalDetailQuery }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getHospitalDetailQuery")
	public JAXBElement<GetHospitalDetailQuery> createGetHospitalDetailQuery(
			GetHospitalDetailQuery value) {
		return new JAXBElement<GetHospitalDetailQuery>(
				_GetHospitalDetailQuery_QNAME, GetHospitalDetailQuery.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreInHospitalAccounts }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreInHospitalAccounts")
	public JAXBElement<GetPreInHospitalAccounts> createGetPreInHospitalAccounts(
			GetPreInHospitalAccounts value) {
		return new JAXBElement<GetPreInHospitalAccounts>(
				_GetPreInHospitalAccounts_QNAME,
				GetPreInHospitalAccounts.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetSectionAccountsResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getSectionAccountsResponse")
	public JAXBElement<GetSectionAccountsResponse> createGetSectionAccountsResponse(
			GetSectionAccountsResponse value) {
		return new JAXBElement<GetSectionAccountsResponse>(
				_GetSectionAccountsResponse_QNAME,
				GetSectionAccountsResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetSectionRefundResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getSectionRefundResponse")
	public JAXBElement<GetSectionRefundResponse> createGetSectionRefundResponse(
			GetSectionRefundResponse value) {
		return new JAXBElement<GetSectionRefundResponse>(
				_GetSectionRefundResponse_QNAME,
				GetSectionRefundResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetSectionAccounts }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getSectionAccounts")
	public JAXBElement<GetSectionAccounts> createGetSectionAccounts(
			GetSectionAccounts value) {
		return new JAXBElement<GetSectionAccounts>(_GetSectionAccounts_QNAME,
				GetSectionAccounts.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetTradeResultsQuery }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getTradeResultsQuery")
	public JAXBElement<GetTradeResultsQuery> createGetTradeResultsQuery(
			GetTradeResultsQuery value) {
		return new JAXBElement<GetTradeResultsQuery>(
				_GetTradeResultsQuery_QNAME, GetTradeResultsQuery.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreSectionStateQueryResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreSectionStateQueryResponse")
	public JAXBElement<GetPreSectionStateQueryResponse> createGetPreSectionStateQueryResponse(
			GetPreSectionStateQueryResponse value) {
		return new JAXBElement<GetPreSectionStateQueryResponse>(
				_GetPreSectionStateQueryResponse_QNAME,
				GetPreSectionStateQueryResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetTradeConfirm }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getTradeConfirm")
	public JAXBElement<GetTradeConfirm> createGetTradeConfirm(
			GetTradeConfirm value) {
		return new JAXBElement<GetTradeConfirm>(_GetTradeConfirm_QNAME,
				GetTradeConfirm.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetHospitalAccountsResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getHospitalAccountsResponse")
	public JAXBElement<GetHospitalAccountsResponse> createGetHospitalAccountsResponse(
			GetHospitalAccountsResponse value) {
		return new JAXBElement<GetHospitalAccountsResponse>(
				_GetHospitalAccountsResponse_QNAME,
				GetHospitalAccountsResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreSectionStateQuery }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreSectionStateQuery")
	public JAXBElement<GetPreSectionStateQuery> createGetPreSectionStateQuery(
			GetPreSectionStateQuery value) {
		return new JAXBElement<GetPreSectionStateQuery>(
				_GetPreSectionStateQuery_QNAME, GetPreSectionStateQuery.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPreSectionAccountsResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPreSectionAccountsResponse")
	public JAXBElement<GetPreSectionAccountsResponse> createGetPreSectionAccountsResponse(
			GetPreSectionAccountsResponse value) {
		return new JAXBElement<GetPreSectionAccountsResponse>(
				_GetPreSectionAccountsResponse_QNAME,
				GetPreSectionAccountsResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetHospitalDetailQueryResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getHospitalDetailQueryResponse")
	public JAXBElement<GetHospitalDetailQueryResponse> createGetHospitalDetailQueryResponse(
			GetHospitalDetailQueryResponse value) {
		return new JAXBElement<GetHospitalDetailQueryResponse>(
				_GetHospitalDetailQueryResponse_QNAME,
				GetHospitalDetailQueryResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPersonalInformation }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ts.com/services/MedicarePayment", name = "getPersonalInformation")
	public JAXBElement<GetPersonalInformation> createGetPersonalInformation(
			GetPersonalInformation value) {
		return new JAXBElement<GetPersonalInformation>(
				_GetPersonalInformation_QNAME, GetPersonalInformation.class,
				null, value);
	}

}
