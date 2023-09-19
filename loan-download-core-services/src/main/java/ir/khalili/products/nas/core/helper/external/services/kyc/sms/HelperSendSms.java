package ir.khalili.products.nas.core.helper.external.services.kyc.sms;

import java.util.Date;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.nas.core.dao.DAO_Log;
import ir.khalili.products.nas.core.excp.BIZEXCP_Lotus;
import ir.khalili.products.nas.core.helper.external.services.kyc.KycHelper;
import ir.khalili.products.nas.core.helper.external.services.kyc.dto.LogDto;


public class HelperSendSms extends KycHelper {

    public static Future<Void> sendSms(String mobileNumber, String smsMessage, int agentId, int customerServiceId, int customerId) {
        Promise<Void> result = Promise.promise();

		JsonObject joInput = new JsonObject();
		joInput.put("requestType", "TRANSACTION");
		joInput.put("serviceId", "outputmanagement.message.sendSmsForSelectedobileNumberMsg");
		joInput.put("username", "husseini");
		joInput.put("branchCode", 1002);
		joInput.put("inbound", new JsonObject().put("mobileNumber", mobileNumber).put("smsMessage", smsMessage));
		
        client.post(port, host, url).sendJson(joInput, ar -> {
            try {
                if (ar.succeeded()) {

                	logger.trace("joOutput:" + ar.result().bodyAsString());
					
                    LogDto logDto = LogDto.of(customerId, customerServiceId, agentId,
                            "outputmanagement.message.sendSmsForSelectedobileNumberMsg",
                            joInput.toString(), ar.result().bodyAsString(), new Date().toString());
                    DAO_Log.saveLog(logDto).onComplete(handler->{
                    });
                    result.complete();
                    
                } else {
                    logger.error(ar.cause().toString());
                    result.fail(new BIZEXCP_Lotus(-110, "خطا در فراخوانی سرویس لوتوس."));
                }
            } catch(DecodeException e) {
            	logger.error(e.getMessage());
                result.fail(new BIZEXCP_Lotus(-110, "خطا در فراخوانی سرویس لوتوس."));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.fail(new BIZEXCP_Lotus(-110, "خطا در فراخوانی سرویس لوتوس."));
            }
        });


        return result.future();
    }
    
    public static String replaceContex(String smsMessage, JsonObject joCustomer) {

        smsMessage = smsMessage
				.replaceAll("name", joCustomer.getString("NAME"))
				.replaceAll("lastName", joCustomer.getString("LASTNAME"))
				.replaceAll("gender", joCustomer.getString("GENDER").equals("M")? "جناب اقای" : "سرکار خانم")
				.replaceAll("cardNumber", joCustomer.getString("CARD_NUMBER"))
				.replaceAll("accountNumber", joCustomer.getString("ACCOUNT_NUMBER"))
				;
        
        return smsMessage;
    }
    
//    public static String replaceContex(String smsMessage, ConfirmParams confirmParams) {
//
//        smsMessage = smsMessage
//				.replaceAll("name", confirmParams.get.getString("NAME"))
//				.replaceAll("lastName", joCustomer.getString("LASTNAME"));
//        
//        return smsMessage;
//    }
    
}
