package ir.khalili.products.nas.core.helper;

import static ir.khalili.products.nas.core.EntryPoint.vertx;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.nas.core.EntryPoint;
import ir.khalili.products.nas.core.enums.UserType;
import ir.khalili.products.nas.core.excp.BIZEXCP_LockIn;
import ir.khalili.products.nas.core.excp.validation.EXCP_RtMgr_Validation;

public class HelperLockIn {

    private static final Logger logger = LogManager.getLogger(HelperLockIn.class);

    private static final WebClient client = WebClient.create(vertx);

    private static final String HOST;
    private static final Integer PORT;
    private static final String API;

    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("lockin");

        HOST = joLockin.getString("host");
        PORT = joLockin.getInteger("port");
        API = joLockin.getString("APIKEY");
    }

    public static Future<JsonObject> doLogin(String username, String password) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joLoginUser = new JsonObject();
        joLoginUser.put("username", username);
        joLoginUser.put("password", password);
        
        client.post(PORT, HOST, "/v1/service/nas/user/login").putHeader("API-KEY", API).sendJson(joLoginUser, resHandler -> {

            if (resHandler.succeeded()) {
                logger.info(resHandler.result().bodyAsJsonObject());
                JsonObject result = resHandler.result().bodyAsJsonObject();
                int resultCode = result.getInteger("resultCode");
                if (resultCode == 1) {
                    promise.complete(result.getJsonObject("info"));
                } else {
                    logger.error(resHandler.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_LockIn(-331, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } else {
                logger.error(resHandler.cause());
                promise.fail(new BIZEXCP_LockIn(-330, "خطا در برقراری ارتباط با سرویس احراز هویت."));
            }

        });

        return promise.future();
    }

    public static Future<JsonObject> checkToken(String token) {

        logger.trace(token);

        Promise<JsonObject> promise = Promise.promise();

        client
                .post(PORT, HOST, "/v1/service/nas/user/check/token")
                .putHeader("API-KEY", API)
                .putHeader("Authorization", token)
                .send(resHandler -> {

                    if (resHandler.succeeded()) {
                        logger.info(resHandler.result().bodyAsJsonObject());
                        JsonObject result = resHandler.result().bodyAsJsonObject();
                        int resultCode = result.getInteger("resultCode");
                        if (resultCode == 1) {
                            promise.complete(result.getJsonObject("info"));
                        } else {
                            logger.error(resHandler.result().bodyAsJsonObject());
                            promise.fail(new EXCP_RtMgr_Validation(-1, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                        }
                    } else {
                        logger.error(resHandler.cause());
                        promise.fail(new BIZEXCP_LockIn(-1, "خطا در برقراری ارتباط با سرویس احراز هویت."));
                    }

                });

        return promise.future();

    }

    public static Future<JsonObject> doSaveUser(String name, String lastname, String isActive, String username, String password, UserType userType) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joSave = new JsonObject();
        joSave.put("name", name);
        joSave.put("lastname", lastname);
        joSave.put("type", userType.getType());
        joSave.put("status", isActive);
        joSave.put("isOtpEnable", false);
        joSave.put("username", username);
        joSave.put("password", password);

        client.post(PORT, HOST, "/v1/service/nas/user/save").putHeader("API-KEY", API).sendJson(joSave, resHandler -> {

            if (resHandler.succeeded()) {
                logger.info(resHandler.result().bodyAsJsonObject());
                JsonObject result = resHandler.result().bodyAsJsonObject();
                int resultCode = result.getInteger("resultCode");
                if (resultCode == 1) {
                    promise.complete(result.getJsonObject("info"));
                } else {
                    logger.error(resHandler.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_LockIn(-1, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } else {
                logger.error(resHandler.cause());
                promise.fail(new BIZEXCP_LockIn(-1, "خطا در برقراری ارتباط با سرویس احراز هویت."));
            }

        });

        return promise.future();
    }

}
