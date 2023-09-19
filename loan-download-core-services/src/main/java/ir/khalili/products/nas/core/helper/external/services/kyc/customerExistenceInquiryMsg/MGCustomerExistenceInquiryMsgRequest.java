package ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg;


import java.io.Serializable;

import ir.khalili.products.nas.core.helper.external.services.kyc.KycRequest;

public class MGCustomerExistenceInquiryMsgRequest extends KycRequest implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 8926023228242668563L;
	private MGCustomerExistenceInquiryMsg.Inbound inbound;

    public MGCustomerExistenceInquiryMsgRequest(MGCustomerExistenceInquiryMsg.Inbound inbound) {
        super("channelManagement.MGCustomerExistenceInquiryMsg",null);
        this.inbound = inbound;
    }

    public static MGCustomerExistenceInquiryMsgRequest of(MGCustomerExistenceInquiryMsg.Inbound inbound) {
        return new MGCustomerExistenceInquiryMsgRequest(inbound);
    }

    public MGCustomerExistenceInquiryMsg.Inbound getInbound() {
        return inbound;
    }

    public void setInbound(MGCustomerExistenceInquiryMsg.Inbound inbound) {
        this.inbound = inbound;
    }
}
