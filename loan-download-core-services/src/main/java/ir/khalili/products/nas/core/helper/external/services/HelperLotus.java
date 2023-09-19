package ir.khalili.products.nas.core.helper.external.services;

import static ir.khalili.products.nas.core.EntryPoint.vertx;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.nas.core.EntryPoint;
import ir.khalili.products.nas.core.dao.DAO_Log;
import ir.khalili.products.nas.core.excp.BIZEXCP_Lotus;
import ir.khalili.products.nas.core.helper.external.services.kyc.KycHelper;
import ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg.ChCustomerExistenceInquiryResponseDto;
import ir.khalili.products.nas.core.helper.external.services.kyc.dto.LogDto;


public class HelperLotus {

    private static final Logger logger = LogManager.getLogger(HelperLotus.class);

    private static final WebClient client = WebClient.create(vertx);

    public static Future<ChCustomerExistenceInquiryResponseDto> customerExistenceInquiry(String customerNo) {

        logger.trace("CALL_customerExistenceInquiry");

        Promise<ChCustomerExistenceInquiryResponseDto> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "channelManagement.MGCustomerExistenceInquiryMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("requestDto" , new JsonObject()
                        .put("customerNo" , customerNo)
                ));

        logger.trace("joInput:" + joInput);


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    logger.trace(Json.encodePrettily(response));
                    promise.complete();
                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
            }
        });
    

        return promise.future();
    }

    public static Future<Void> activateCard(String cardNumber , String cvv2 , String exp) {

        Promise<Void> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "TRANSACTION");
        joInput.put("serviceId" , "cardmanagement.cms.MGActivateIssueDebitCard");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("cardNumber" , cardNumber)
                .put("cvv2" , cvv2)
                .put("embossedExpireDate" , exp)
                .put("cardOtpRegister" , true)
                .put("otpPin" , "12345678")
        );

        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {

                    if (ar.result().bodyAsString().equals("{\"result\":true}")) {
                        promise.complete();
                    }

                    JsonObject joResponse = new JsonObject(ar.result().bodyAsString());

                    logger.trace(joResponse);

                    if (joResponse.containsKey("message")) {
                        JsonObject joMessage = new JsonObject(joResponse.getString("message"));
                        if (joMessage.containsKey("fa_IR")) {
                            promise.fail(new BIZEXCP_Lotus(-111 , joMessage.getString("fa_IR")));
                        } else {
                            promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                        }

                    } else {
                        promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس." + joResponse));
                    }

                } else {
                    logger.error(ar.cause().toString());
                    promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
            }
        });


        return promise.future();
    }

    public static Future<JsonObject> payment(String depositNumber , long amount , String description , String uniqueTrackingCode) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "TRANSACTION");
        joInput.put("serviceId" , "accounting.dealPosting.GetVirtualLoanCommissionMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("requestDto" , new JsonObject().put("depositNumber" , depositNumber).put("amount" , amount).put("branchCode" , 1002).put("description" , description))
                .put("uniqueTrackingCode" , uniqueTrackingCode)
        );

        logger.trace(joInput);

        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject joResponse = new JsonObject(ar.result().bodyAsString());
                    logger.trace(joResponse);

                    if (joResponse.containsKey("message")) {
                        JsonObject joMessage = new JsonObject(joResponse.getString("message"));
                        if (joMessage.containsKey("fa_IR")) {
                            promise.fail(new BIZEXCP_Lotus(-111 , joMessage.getString("fa_IR")));
                        } else {
                            promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                        }

                    } else {
                        promise.complete(joResponse);
                    }

                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
            }
        });

        return promise.future();
    }

    public static Future<JsonArray> fetchCustomerAddress(long customerNumber) {

        Promise<JsonArray> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "TRANSACTION");
        joInput.put("serviceId" , "channelManagement.GetAddressDetailsByExternalRefMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("externalRef" , customerNumber)
        );

        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {

                    JsonObject joResponse = new JsonObject(ar.result().bodyAsString());

                    logger.trace(joResponse);

                    if (joResponse.containsKey("message")) {
                        JsonObject joMessage = new JsonObject(joResponse.getString("message"));
                        if (joMessage.containsKey("fa_IR")) {
                            promise.fail(new BIZEXCP_Lotus(-111 , joMessage.getString("fa_IR")));
                        } else {
                            promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                        }

                    } else {
                        promise.complete(joResponse.getJsonArray("addressList" , new JsonArray()));
                    }

                } else {
                    logger.error(ar.cause().toString());
                    promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                promise.fail(new BIZEXCP_Lotus(-110 , "خطا در فراخوانی سرویس لوتوس."));
            }
        });


        return promise.future();
    }

    public static Future<JsonObject> getListOfProvincesMsg() {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "system.referencedata.GetListOfProvinces");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject().put("countryCode" , "IR"));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    System.out.println(Json.encodePrettily(response));
                    promise.complete(response);
                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });

        return promise.future();

    }

    public static Future<JsonObject> getListOfCities(Integer provinceCode) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "system.referencedata.GetListOfCities");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject().put("countryCode" , "IR").put("provinceCode" , String.valueOf(provinceCode)));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    System.out.println(Json.encodePrettily(response));
                    promise.complete(response);
                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });
    
        return promise.future();

    }

    public static Future<JsonObject> getJobGroup() {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "channelManagement.MGLoadCharityAccountsMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject().put("mgParentCode" , "JobGroup"));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    System.out.println(Json.encodePrettily(response));
                    promise.complete(response);
                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });
    
        return promise.future();

    }

    public static Future<JsonObject> getOccupation() {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "channelManagement.MGLoadCharityAccountsMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject().put("mgParentCode" , "Occupation"));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    System.out.println(Json.encodePrettily(response));
                    promise.complete(response);
                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });
    
        return promise.future();

    }

    public static Future<Boolean> getBouncedChequesByPersonInfo(String nationalId , Integer personType) {

        logger.trace("CALL_getBouncedChequesByPersonInfo");

        Promise<Boolean> promise = Promise.promise();

        if (EntryPoint.joConfig.getBoolean("isMock" , false)) {
            promise.complete(Boolean.FALSE);
            return promise.future();
        }

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "deposit.samacheque.message.GetBouncedChequesByPersonInfoMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("samaInputPersonInfoDto" , new JsonObject()
                        .put("nationalId" , nationalId)
                        .put("personType" , personType)));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    logger.trace("getBouncedChequesByPersonInfo_Successful");
                    logger.trace(Json.encodePrettily(response));

                    if (response.containsKey("chequeList") && response.getJsonArray("chequeList").size() > 0) {
                        promise.complete(Boolean.TRUE);
                    } else {
                        promise.complete(Boolean.FALSE);
                    }

                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });
    
        return promise.future();

    }

    public static Future<JsonObject> militaryStatusInquiryReportMsg(String nationalCode) {

        logger.trace("CALL_MilitaryStatusInquiryReportMsg");

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("requestType" , "INQUIRY");
        joInput.put("serviceId" , "cif.message.MilitaryStatusInquiryReportMsg");
        joInput.put("username" , "husseini");
        joInput.put("branchCode" , 1002);
        joInput.put("inbound" , new JsonObject()
                .put("nationalCode" , nationalCode));


        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {

                    logger.trace("MilitaryStatusInquiryReportMsg, input: {}, output: {}" , joInput , ar.result().bodyAsString());

                    JsonObject response = new JsonObject(ar.result().bodyAsString());

                    promise.complete(response);

                } else {
                    logger.error(ar.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ar.cause());
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });
    
        return promise.future();

    }


//    public static Future<JsonObject> peccoInsertInquiryResponseMsg(String nationalCode , long clientId) {
//
//        logger.trace("CALL_PeccoInsertInquiryResponseMsg");
//
//        Promise<JsonObject> promise = Promise.promise();
//
//        JsonObject joInput = new JsonObject();
//        joInput.put("requestType" , "INQUIRY");
//        joInput.put("serviceId" , "creditcard.message.PeccoInsertInquiryResponseMsg");
//        joInput.put("username" , "husseini");
//        joInput.put("branchCode" , 1002);
//        joInput.put("inbound" , new JsonObject()
//                .put("input" , new JsonObject()
//                        .put("clientId" , clientId)
//                        .put("nationalCode" , nationalCode)
//                ));
//
//
//        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(joInput , ar -> {
//            try {
//                if (ar.succeeded()) {
//
//                    logger.trace("PeccoInsertInquiryResponseMsg, input: {}, output: {}" , joInput , ar.result().bodyAsString());
//
//                    JsonObject response = new JsonObject(ar.result().bodyAsString());
//
//                    promise.complete(response);
//
//                } else {
//                    logger.error(ar.result().bodyAsJsonObject());
//                    promise.fail(new BIZEXCP_Lotus(-1 , ar.result().bodyAsJsonObject().getString("resultMessage")));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error(ar.cause());
//                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
//            }
//        });
//    
//        return promise.future();
//
//    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////      BARDIADEMON      ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Future<JsonObject> sendHttpRequest(int agentId, int customerId, int customerServiceId, final String serviceId , final JsonObject inbound, String requestType) {

        final Promise<JsonObject> promise = Promise.promise();

        final JsonObject request = new JsonObject()
                .put("requestType" , requestType)
                .put("username" , "husseini")
                .put("branchCode" , "1002")
                .put("serviceId" , serviceId)
                .put("inbound" , inbound);

        logger.info("input: {}", request);
        
        client.post(KycHelper.port , KycHelper.host , KycHelper.url).sendJson(request , ar -> {
            try {
                if (ar.succeeded()) {
                    final JsonObject response = new JsonObject(ar.result().bodyAsString());

                    logger.info("Successfully request: {}, {}" , serviceId, response);

                    LogDto logDto = new LogDto();
                    logDto.setAgentId(agentId);
                    logDto.setCustomerId(customerId);
                    logDto.setCustomerServiceId(customerServiceId);
                    logDto.setInputJson(inbound.toString());
                    logDto.setOutputJson(ar.result().bodyAsString());
                    logDto.setRequestDate(new Date().toString());
                    logDto.setServiceName(serviceId);
                    
                    DAO_Log.saveLog(logDto);
                    
                    if(response.containsKey("message")) {
                    	promise.fail(new BIZEXCP_Lotus(-110 , new JsonObject(response.getString("message")).getString("fa_IR", " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !")));
                    }else {
                    	promise.complete(response);
                    }

                } else {
                	logger.error(ar.cause().toString());
                    promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
                }
            } catch (Exception e) {
                logger.error("Fail request" , e);
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            }
        });

        return promise.future();
    }

    public static Future<JsonObject> inqueryIranian(int agentId, int customerId, int customerServiceId, String nationalNumber) {
    	
    	final Promise<JsonObject> promise = Promise.promise();
    	
        JsonObject joInput = new JsonObject();
        joInput.put("nationalCode" , nationalNumber);
        joInput.put("inquiryID", customerServiceId + "-" + new Date().getTime());
        joInput.put("inquiryTypes" , new JsonArray().add("IRANIAN_BASE").add("IRANIAN_STANDARD").add("IRANIAN_ADVANCE").add("IRANIAN_SCORE"));


        client.post(8080 , "192.168.243.126" , "/inquiry").putHeader("Content-Type", "application/json").sendJson(joInput , ar -> {
            try {
                if (ar.succeeded()) {
                	
                    LogDto logDto = new LogDto();
                    logDto.setAgentId(agentId);
                    logDto.setCustomerId(customerId);
                    logDto.setCustomerServiceId(customerServiceId);
                    logDto.setInputJson(joInput.toString());
                    logDto.setOutputJson(ar.result().bodyAsString());
                    logDto.setRequestDate(new Date().toString());
                    logDto.setServiceName("inqueryIranian");
                    
                    DAO_Log.saveLog(logDto);
                	
                	
                    JsonObject response = new JsonObject(ar.result().bodyAsString());
                    
                    if(response.containsKey("message")) {
                    	System.out.println();
                    	System.out.println();
                    	System.out.println(ar.result().bodyAsString());
                    	System.out.println();
                    	System.out.println();
                    	promise.fail(new BIZEXCP_Lotus(-110 , response.getString("message"," در فراخوانی سرویس های لوتوس خطایی به وجود امده است !")));
                    }else {
                    	promise.complete(response);
                    }
                    
                } else {
                	logger.error(ar.cause().toString());
                    promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
                }
            } catch (Exception e) {
            	logger.error("Fail request" , e);
                promise.fail(new BIZEXCP_Lotus(-110 , " در فراخوانی سرویس های لوتوس خطایی به وجود امده است !"));
            } 
        });
    
        
        return promise.future();
        
    }
    
}
