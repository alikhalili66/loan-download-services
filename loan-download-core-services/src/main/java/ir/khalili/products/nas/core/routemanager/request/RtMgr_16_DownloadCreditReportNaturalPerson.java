package ir.khalili.products.nas.core.routemanager.request;


import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.nas.core.constants.AppConstants;
import ir.khalili.products.nas.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.nas.core.routemanager.RtMgr;
import ir.khalili.products.nas.core.validation.InputValidationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RtMgr_16_DownloadCreditReportNaturalPerson extends RtMgr {


    private static final Logger logger = LogManager.getLogger(RtMgr_16_DownloadCreditReportNaturalPerson.class);

    public static void handler(RoutingContext context) {

        InputValidationUtil.validateAgentSession(context).onComplete(handler -> {

            if (handler.failed()) {
                failed(context , handler.cause());
                return;
            }

            final JsonObject joSession = handler.result();

            Long customerServiceId;

            try {
                final JsonObject inputParameters = InputValidationUtil.validate(context);
                customerServiceId = inputParameters.getLong("customerServiceId");

                if (customerServiceId == null || customerServiceId < 1) {
                    throw new EXCP_RtMgr_Validation(-101 , "شناسه خدمت مشتری صحیح نمی باشد.");
                }

            } catch (EXCP_RtMgr_Validation e) {
                failed(context , e);
                return;
            } catch (Exception e) {
                logger.error("INPUT TYPE VALIDATION FAILED." , e);
                failed(context , new EXCP_RtMgr_Validation(-499 , "نوع داده اقلام ارسال شده معتبر نیست. به سند راهنما رجوع کنید "));
                return;
            }

            final JsonObject joResult = new JsonObject();
            joResult.put("customerServiceId" , customerServiceId);

            joResult.put("userId" , joSession.getInteger("userId"));
            joResult.put("agentId" , joSession.getInteger("agentId"));
            joResult.put("clientInfo" , context.request().getHeader("User-Agent"));
            joResult.put("ip" , context.request().remoteAddress().host());

            request(context , AppConstants.EVNT_BUS_ADR_SRVCS_REQ_CREDIT_REPORT_NATURAL_PERSON , joResult);

        });

    }

}
