package ir.khalili.products.nas.core.routemanager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.nas.core.excp.validation.EXCP_RtMgr_Validation;

public class RtMgr {

	private static Logger logger = LogManager.getLogger(RtMgr.class);
	public static final int MAX_TIMEOUT = 90000;
	
	public static void request(RoutingContext context, String address, JsonObject message) {

		DeliveryOptions deliveryOptions = new DeliveryOptions().setSendTimeout(MAX_TIMEOUT);
		
		context.vertx().eventBus().request(address, message, deliveryOptions, replyHandler -> {

			if (replyHandler.succeeded()) {
				logger.trace("jobs done:" + replyHandler.result().body());

				context.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(replyHandler.result().body()));

			} else {// Unable to communicate with event bus

				logger.error("UNABLE TO COMMUNICATE WITH EVENT BUS!");
				String resMessage = "";
				int resultCode;
				if (((ReplyException) replyHandler.cause()).failureType() == ReplyFailure.NO_HANDLERS) {
					logger.error("NO HANDLERS WERE AVAILABLE TO HANDLE THE MESSAGE.");
					resMessage = " ،برای رسیدگی به این درخواست ثبت نشده، لطفا با راهبر سامانه تماس بگیرید EventBus آدرسی در";
					resultCode = ((ReplyException) replyHandler.cause()).failureCode();
				} else if (((ReplyException) replyHandler.cause()).failureType() == ReplyFailure.TIMEOUT) {
					logger.error("NO REPLY WAS RECEIVED BEFORE THE TIMEOUT TIME.");
					resMessage = "عدم فراخوانی سرویس در زمان مناسب.";
					resultCode = -202;
				} else {
					logger.error("THE RECIPIENT ACTIVELY SENT BACK A FAILURE (REJECTED THE MESSAGE).");
					resMessage = replyHandler.cause().getMessage();
					resultCode = ((ReplyException) replyHandler.cause()).failureCode();
				}
				logger.error("EVENT BUS ERROR CAUSE IS : " + replyHandler.cause().toString());

				context.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(new JsonObject()
								.put("resultCode", resultCode)
								.put("resultMessage", resMessage)));

			}

		});

	}

	public static void failed(RoutingContext context, Throwable throwable) {
		context.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(new JsonObject()
						.put("resultCode", ((EXCP_RtMgr_Validation) throwable).getResultCode())
						.put("resultMessage", ((EXCP_RtMgr_Validation) throwable).getResultMessage())));
	}

}
