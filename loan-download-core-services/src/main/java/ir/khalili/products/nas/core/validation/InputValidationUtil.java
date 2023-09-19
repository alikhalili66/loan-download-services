package ir.khalili.products.nas.core.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.nas.core.EntryPoint;
import ir.khalili.products.nas.core.excp.validation.EXCP_RtMgr_Validation;;

/**
 * @author A.KH
 */
public final class InputValidationUtil {

	private static Logger logger = LogManager.getLogger(InputValidationUtil.class);

	/**
	 * This method to call locally without RoutingContext object and in main method
	 */
	public static JsonObject validate(RoutingContext context) throws EXCP_RtMgr_Validation {

		final JsonObject inputParameters;

		try {
			inputParameters = context.getBodyAsJson();
			logger.trace("VALID JSON FORMAT FOR INPUT : " + inputParameters);
		} catch (Exception e) {
			logger.error("INVALID JSON FORMAT FOR INPUT : " + context.getBodyAsString());
			throw new EXCP_RtMgr_Validation(-498, "اطلاعات ورودی ارسالی در قالب فرمت استاندارد جی سان نمی باشد.");
		}

		return inputParameters;
	}

	public static Future<JsonObject> validateAgentSession(RoutingContext context) {

		Promise<JsonObject> promise = Promise.promise();

//		String agentSession = context.request().getHeader("agentSession");
//
//		if (null == agentSession || agentSession.trim().isEmpty()) {
//			logger.error("agentSessionIsNull");
//			promise.fail(new EXCP_RtMgr_Validation(-301, "نشست عامل صحیح نمی باشد."));
//			return promise.future();
//		}
//
//		JsonObject joAgentSession;
//		try {
//			joAgentSession = new JsonObject(SecurityUtil.decrypt(agentSession, agentKey));
//		} catch (EXCPSEC_UnableToDecrypt e) {
//			logger.error("agentSessionIsNull");
//			promise.fail(new EXCP_RtMgr_Validation(-301, "نشست عامل صحیح نمی باشد."));
//			return promise.future();
//		}
//
//		validateToken(joAgentSession.getString("token")).onComplete(tokenHandler -> {
//
//			if (tokenHandler.failed()) {
//				promise.fail(tokenHandler.cause());
//				return;
//			}
//
//			promise.complete(joAgentSession);
//
//		});

		promise.complete(new JsonObject()
				.put("userId", EntryPoint.joConfig.getInteger("userId"))
				.put("agentId", EntryPoint.joConfig.getInteger("agentId"))
				);
		
		return promise.future();
	}
	
//	private static Future<JsonObject> validateToken(String token) {
//
//		Promise<JsonObject> promise = Promise.promise();
//
//		if (null == token || token.isEmpty()) {
//			logger.error("INVALID Token : " + token);
//			promise.fail(new EXCP_RtMgr_Validation(-302, "نشست عامل معتبر نمی باشد."));
//			return promise.future();
//		}
//
//		Future<JsonObject> futToken = HelperLockIn.checkToken("Bearer " + token);
//
//		CompositeFuture.join(Arrays.asList(futToken)).onComplete(joinHandler -> {
//
//			if (joinHandler.failed()) {
//				logger.error("INVALID Token : " + token);
//				promise.fail(new EXCP_RtMgr_Validation(-302, "نشست عامل معتبر نمی باشد."));
//				return;
//			}
//
//			logger.trace("tokenInfo: " + futToken.result());
//
//			promise.complete(futToken.result());
//
//		});
//
//		return promise.future();
//	}

}