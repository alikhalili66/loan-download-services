package ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg;

import java.io.Serializable;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.nas.core.excp.BIZEXCP_Lotus;
import ir.khalili.products.nas.core.helper.external.services.kyc.KycHelper;

public class HelperCustomerExistenceInquiry extends KycHelper {

	public static Future<ChCustomerExistenceInquiryResponseDto> customerExistenceInquiry(String customerNo) {
		logger.trace("CALL_customerExistenceInquiry");
		
        Promise<ChCustomerExistenceInquiryResponseDto> result = Promise.promise();
        MGCustomerExistenceInquiryMsg.Inbound request = new MGCustomerExistenceInquiryMsg.Inbound();
        ChCustomerExistenceInquiryRequestDto requestDto = new ChCustomerExistenceInquiryRequestDto();
        requestDto.setCustomerNo(customerNo);
        requestDto.setIndividualNationalCode(null);
        
        request.setRequestDto(requestDto);

        client.post(port, host, url).sendJson(MGCustomerExistenceInquiryMsgRequest.of(request), ar -> {
            try {
                if (ar.succeeded()) {
                	
                	logger.trace("customerExistenceInquiry:" + ar.result().bodyAsString());

                	JsonObject joResponse = new JsonObject(ar.result().bodyAsString());
                	
                	logger.trace("joResponse:" + joResponse);
                	
                	if(joResponse.containsKey("message")) {
                    	JsonObject joMessage = new JsonObject(joResponse.getString("message"));
                    	if(joMessage.getString("code").equals("cif.customer.not.found.exception")) {
                    		result.fail(new BIZEXCP_Lotus(-110,"مشتری در اطلاعات بانک وجود نمی باشد."));
                    	}else {
                    		if(joMessage.containsKey("fa_IR")) {
                    			result.fail(new BIZEXCP_Lotus(-110,joMessage.getString("fa_IR")));
                    		}else {
                    			result.fail(new BIZEXCP_Lotus(-110,"خطا در فراخوانی سرویس لوتوس."));
                    		}
                    	}
                	}else {
                		Outbound response = ar.result().bodyAsJson(Outbound.class);
                		result.complete(response.getResponseDto());
                	}
                } else {
                    logger.error(ar.cause().toString());
                    result.fail(new BIZEXCP_Lotus(-110,"خطا در فراخوانی سرویس لوتوس."));
                }
            } catch (Exception e) {
            	e.printStackTrace();
            	result.fail(new BIZEXCP_Lotus(-110,"خطا در فراخوانی سرویس لوتوس."));
            }
        });

		
	
			
        return result.future();
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
