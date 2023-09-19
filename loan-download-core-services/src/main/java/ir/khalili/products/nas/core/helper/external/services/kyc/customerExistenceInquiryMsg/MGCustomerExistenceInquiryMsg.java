package ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg;

import java.io.Serializable;

/**
 * Created by gheibi on 26/09/2017.
 */


public class MGCustomerExistenceInquiryMsg implements Serializable {
    public static final String MESSAGE_ID = "ECHO";
    private static final long serialVersionUID = -5894873838024108812L;

    public final static class Inbound implements Serializable {
        private static final long serialVersionUID = -8613205389985813732L;
        private ChCustomerExistenceInquiryRequestDto requestDto;

        public ChCustomerExistenceInquiryRequestDto getRequestDto() {
            return requestDto;
        }

        public void setRequestDto(ChCustomerExistenceInquiryRequestDto requestDto) {
            this.requestDto = requestDto;
        }
    }

    public final static class Outbound implements Serializable {
        private static final long serialVersionUID = 331642111671942588L;
        private ChCustomerExistenceInquiryResponseDto responseDto;

        public ChCustomerExistenceInquiryResponseDto getResponseDto() {
            return responseDto;
        }

        public void setResponseDto(ChCustomerExistenceInquiryResponseDto responseDto) {
            this.responseDto = responseDto;
        }
    }
}
