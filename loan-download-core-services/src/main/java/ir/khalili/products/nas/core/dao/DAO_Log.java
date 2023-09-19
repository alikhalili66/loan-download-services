package ir.khalili.products.nas.core.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.nas.core.EntryPoint;
import ir.khalili.products.nas.core.constants.AppConstants;
import ir.khalili.products.nas.core.excp.dao.DAOEXCP_Internal;
import ir.khalili.products.nas.core.helper.external.services.kyc.dto.LogDto;
import ir.khalili.products.nas.core.utils.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAO_Log {

    private static final Logger logger = LogManager.getLogger(DAO_Log.class);

    public static Future<Void> saveLog(LogDto logDto) {

        Promise<Void> promise = Promise.promise();

        logger.trace(logDto);

        JDBCClient ircJDBC = JDBCClient.createShared(EntryPoint.vertx , Configuration.getDataBaseConfig() , AppConstants.APP_DS_NAS);
        ircJDBC.getConnection(connection -> {

            if (connection.failed()) {
                logger.error("Unable to get connection from database:" , connection.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "امکان برقراری ارتباط با بانک اطلاعاتی نیست"));
                return;
            }

            SQLConnection sqlConnection = connection.result();

            JsonArray params = new JsonArray();
            params.add(logDto.getCustomerId());
            params.add(logDto.getAgentId());
            params.add(logDto.getCustomerServiceId());
            if (null != logDto.getInputJson() && logDto.getInputJson().length() > 3000) {
                params.add(logDto.getInputJson().substring(0 , 3000));
            } else {
                params.add(logDto.getInputJson());
            }

            if (null != logDto.getOutputJson() && logDto.getOutputJson().length() > 2000) {
                params.add(logDto.getOutputJson().substring(0 , 2000));
            } else {
                params.add(logDto.getOutputJson());
            }

            try {
                params.add(URLEncoder.encode(logDto.getOutputJson() , "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                params.add(logDto.getOutputJson());
            }

            params.add(logDto.getServiceName());
            params.add(logDto.getRequestDate());

            logger.trace(params);

            sqlConnection.updateWithParams(""
                                           + "insert into tNASLOG("
                                           + "ID,"
                                           + "CUSTOMER_ID,"
                                           + "AGENT_ID,"
                                           + "CUSTOMER_SERVICE_ID,"
                                           + "INPUT_JSON,"
                                           + "OUTPUT_JSON,"
                                           + "OUTPUT_JSON2,"
                                           + "SERVICE_NAME,"
                                           + "REQUEST_DATE,"
                                           + "CREATIONDATE)"
                                           + "values(sNASLOG.nextVal,?,?,?,?,?,?,?,?,sysdate)" , params , resultHandler -> {

                sqlConnection.close();

                if (resultHandler.failed()) {
                    logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                }

                logger.trace("SaveAgentSuccessful");
                promise.complete();

            });
        });


        return promise.future();
    }

    public static Future<JsonObject> fetchOutput(final SQLConnection sqlConnection , final long customerServiceId , final String serviceName) {

        final Promise<JsonObject> promise = Promise.promise();

        final JsonArray params = new JsonArray()
                .add(customerServiceId)
                .add(serviceName);

        final String query = "select OUTPUT_JSON, OUTPUT_JSON2 from tnaslog  where CUSTOMER_SERVICE_ID = ? and SERVICE_NAME = ? order by ID desc";

        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                logger.warn("Not found");
                promise.complete(new JsonObject());
                return;
            }

            logger.info("Successfully fetch log: {}" , resultHandler.result().getRows().get(0));

            promise.complete(resultHandler.result().getRows().get(0));

        });

        return promise.future();
    }

    public static Future<JsonArray> fetchLogByCustomerServiceId(final SQLConnection sqlConnection , final long customerServiceId) {

        final Promise<JsonArray> promise = Promise.promise();

        final JsonArray params = new JsonArray()
                .add(customerServiceId);

        final String query = " select " +
                             "    ID, " +
                             "    CUSTOMER_ID, " +
                             "    CUSTOMER_SERVICE_ID, " +
                             "    INPUT_JSON, " +
                             "    OUTPUT_JSON, " +
                             "    SERVICE_NAME, " +
                             "    REQUEST_DATE, " +
                             "    TO_CHAR(CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, " +
                             "    OUTPUT_JSON2 " +
                             " from TNASLOG " +
                             " where CUSTOMER_SERVICE_ID = ?" +
                             " order by ID desc ";

        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Fail to fetch log by customer service id: {}" , customerServiceId , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                logger.warn("Not found, CustomerServiceId[{}]" , customerServiceId);
                promise.complete(new JsonArray());
                return;
            }

            logger.info("Successfully fetch log: {}" , resultHandler.result().getRows());

            promise.complete(new JsonArray(resultHandler.result().getRows()));

        });

        return promise.future();
    }

}
