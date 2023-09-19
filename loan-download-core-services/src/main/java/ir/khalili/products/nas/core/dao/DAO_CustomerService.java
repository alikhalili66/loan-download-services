package ir.khalili.products.nas.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.nas.core.enums.CustomerServiceHistory;
import ir.khalili.products.nas.core.enums.CustomerServiceStatus;
import ir.khalili.products.nas.core.enums.ServiceType;
import ir.khalili.products.nas.core.excp.dao.DAOEXCP_Internal;

public class DAO_CustomerService {

    private static final Logger logger = LogManager.getLogger(DAO_CustomerService.class);

    public static Future<Integer> fetchCustomerServiceId(SQLConnection sqlConnection) {
        Promise<Integer> promise = Promise.promise();

        sqlConnection.query("select SNASCUSTOMERService.nextval from dual" , sequenceResult -> {

            if (sequenceResult.failed()) {
                logger.error("Unable to get accessQueryResult:" + sequenceResult.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }
            promise.complete(sequenceResult.result().getRows().get(0).getInteger("NEXTVAL"));
        });

        return promise.future();
    }

    public static Future<Integer> saveCustomerService(SQLConnection sqlConnection , int id , Integer serviceId , int customerId , int agentId , Integer operatorId , CustomerServiceStatus status , String description , String info) {
        Promise<Integer> promise = Promise.promise();

        JsonArray params = new JsonArray();
        params.add(id);
        params.add(serviceId);
        params.add(customerId);
        params.add(agentId);
        params.add(operatorId);
        params.add(status.name());
        params.add(0);
        params.add(description);
        params.add(info);

        sqlConnection.queryWithParams(""
                                      + "insert into TNASCUSTOMERSERVICE ("
                                      + "ID,"
                                      + "service_id,"
                                      + "customer_id,"
                                      + "agent_id,"
                                      + "operator_Id,"
                                      + "status,"
                                      + "activeTime,"
                                      + "description,"
                                      + "creationDate,"
                                      + "LASTACTIVITY,"
                                      + "info)"
                                      + "values(?,?,?,?,?,?,?,?,sysdate, sysdate,?) " , params , insertRes -> {
            if (insertRes.failed()) {
                logger.error("Unable to get accessQueryResult:" + insertRes.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }


            logger.trace("saveCustomerSuccessfull");
            JsonArray paramsInsert = new JsonArray();
            paramsInsert.add(id);
            paramsInsert.add(status);
            paramsInsert.add(description);
            paramsInsert.add(0);
            paramsInsert.add(-1);
            paramsInsert.add(CustomerServiceHistory.Save.name());
            paramsInsert.add("");

            String queryInsert = ""
                                 + "insert into tnascustomerservicehistory ("
                                 + "id,"
                                 + "CUSTOMERSERVICE_ID,"
                                 + "STATUS,"
                                 + "DESCRIPTION,"
                                 + "ACTIVETIME,"
                                 + "historyBy_ID,"
                                 + "HISTORYTYPE,"
                                 + "historyDescription,"
                                 + "historyDate)"
                                 + "values(snascustomerservicehistory.nextval,?,?,?,?,?,?,?,sysdate) ";

            sqlConnection.updateWithParams(queryInsert , paramsInsert , resultHandler -> {
                if (resultHandler.failed()) {
                    logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                    promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                    return;
                }

                logger.trace("doSaveCustomerServiceHistorySuccessful");
                promise.complete(id);

            });

        });

        return promise.future();

    }

    public static Future<List<JsonObject>> fetchByCreationDate(SQLConnection sqlConnection , String customerNumber , String from , String to) {

        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerNumber);
        params.add(from);
        params.add(to);

        sqlConnection.queryWithParams(""
                                      + "select "
                                      + "cs.ID, "
                                      + "cs.STATUS, "
                                      + "s.name servicename, "
                                      + "s.symbol serviceSymbol, "
                                      + "cs.DESCRIPTION, "
                                      + "TO_CHAR(cs.CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, "
                                      + "cs.CONSIGNMENTCODE,"
                                      + "cs.INFO "
                                      + "from tnascustomerservice cs, tnascustomer c, tnasService s "
                                      + "where c.CUSTOMER_NO = ? "
                                      + "and cs.customer_ID = c.id "
                                      + "and cs.service_ID = s.id "
                                      + "and s.id in (9,10,11) "
                                      + "and cs.CREATIONDATE >= TO_DATE(?, 'YYYYMMDD') "
                                      + "and cs.CREATIONDATE <= TO_DATE(?, 'YYYYMMDD') "
                                      + "order by id desc " , params , resultHandler -> {
            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                logger.warn("ServiceNotFound");
                promise.complete(new ArrayList<>());
            } else {
                logger.trace("RESULT:" + resultHandler.result().getRows().get(0));
                promise.complete(resultHandler.result().getRows());
            }

        });

        return promise.future();
    }

    public static Future<JsonObject> fetchCustomerServiceInfo(SQLConnection sqlConnection , int customerServiceId , int agentId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerServiceId);
        params.add(agentId);
        sqlConnection.queryWithParams("select ID, STATUS, SERVICE_ID, CUSTOMER_ID, CONSIGNMENTCODE as CONSIGNMENT_CODE, INFO from TNASCUSTOMERSERVICE cs where id=? and agent_id=? " , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                logger.warn("CustomerNotFound");
                promise.fail(new DAOEXCP_Internal(-203 , "درخواست مورد نظر یافت نشد."));
            } else {
                logger.trace("CustomerRESULT:" + resultHandler.result().getRows().get(0));
                promise.complete(resultHandler.result().getRows().get(0));
            }

        });
        return promise.future();
    }

    public static Future<JsonObject> fetchCustomerServiceInfo(SQLConnection sqlConnection , String cardNumber , int agentId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(cardNumber);
        params.add(agentId);

        sqlConnection.queryWithParams(""
                                      + "select ID, STATUS, SERVICE_ID, CUSTOMER_ID, CONSIGNMENTCODE as CONSIGNMENT_CODE, INFO "
                                      + "from TNASCUSTOMERSERVICE cs "
                                      + "where JSON_VALUE(cs.info, '$.cardInfoDto.cardNumber')=? and agent_id=? order by id desc" , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                logger.warn("CustomerServiceNotFound");
                promise.fail(new DAOEXCP_Internal(-203 , "درخواست مورد نظر یافت نشد."));
            } else {
                logger.trace("CustomerRESULT:" + resultHandler.result().getRows().get(0));
                promise.complete(resultHandler.result().getRows().get(0));
            }

        });
        return promise.future();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////             /////////////////////////////////////////////////////
    ////////////////////////////////////////// BARDIADEMON //////////////////////////////////////////////////////
    /////////////////////////////////////////             ///////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static Future<JsonObject> fetchCustomerServiceByCustomerId(final SQLConnection sqlConnection , final long customerId , final long serviceId) {

        final Promise<JsonObject> promise = Promise.promise();

        final String query = "SELECT * FROM (select " +
                             "    ID, " +
                             "    SERVICE_ID, " +
                             "    CUSTOMER_ID, " +
                             "    AGENT_ID, " +
                             "    STATUS, " +
                             "    DESCRIPTION, " +
                             "    ACTIVETIME, " +
                             "    OPERATOR_ID, " +
                             "    TO_CHAR(CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, " +
                             "    TO_CHAR(VALIDTO, 'YYYY/MM/DD HH24:MI:SS') as VALIDTO, " +
                             "    ACCOUNTCODE, " +
                             "    ISCARD, " +
                             "    ISVIRTUAL, " +
                             "    POSTCODE, " +
                             "    ADDRESS, " +
                             "    TO_CHAR(LASTACTIVITY, 'YYYY/MM/DD HH24:MI:SS') as  LASTACTIVITY, " +
                             "    CONSIGNMENTCODE, " +
                             "    CARDNUMBER, " +
                             "    AI, " +
                             "    INFO " +
                             " from TNASCUSTOMERSERVICE " +
                             " where CUSTOMER_ID = ? and SERVICE_ID = ?" +
                             " ORDER BY ID desc ) WHERE ROWNUM = 1";

        final JsonArray params = new JsonArray()
                .add(customerId)
                .add(serviceId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (resultHandler.result() == null || resultHandler.result().getRows() == null || resultHandler.result().getRows().isEmpty()) {
                logger.error("Not found customer service by customer id: {}" , customerId);
                promise.complete(null);
                return;
            }

            logger.info("Successfully fetch service by customer id: {} , Rows: {}" , customerId , resultHandler.result().getRows());

            promise.complete(resultHandler.result().getRows().get(0));

        });
        return promise.future();
    }

    public static Future<JsonObject> fetchCustomerServiceById(final SQLConnection sqlConnection , final long customerServiceId) {

        final Promise<JsonObject> promise = Promise.promise();

        final String query = "SELECT * FROM (select " +
                             "    ID, " +
                             "    SERVICE_ID, " +
                             "    CUSTOMER_ID, " +
                             "    AGENT_ID, " +
                             "    STATUS, " +
                             "    DESCRIPTION, " +
                             "    ACTIVETIME, " +
                             "    OPERATOR_ID, " +
                             "    TO_CHAR(CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, " +
                             "    TO_CHAR(VALIDTO, 'YYYY/MM/DD HH24:MI:SS') as VALIDTO, " +
                             "    ACCOUNTCODE, " +
                             "    ISCARD, " +
                             "    ISVIRTUAL, " +
                             "    POSTCODE, " +
                             "    ADDRESS, " +
                             "    TO_CHAR(LASTACTIVITY, 'YYYY/MM/DD HH24:MI:SS') as  LASTACTIVITY, " +
                             "    CONSIGNMENTCODE, " +
                             "    CARDNUMBER, " +
                             "    AI, " +
                             "    INFO " +
                             " from TNASCUSTOMERSERVICE " +
                             " where ID = ?" +
                             " ORDER BY ID desc ) WHERE ROWNUM = 1";

        final JsonArray params = new JsonArray()
                .add(customerServiceId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Fail to fetch customer service by id: {}" , customerServiceId , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (resultHandler.result() == null || resultHandler.result().getRows() == null || resultHandler.result().getRows().isEmpty()) {
                logger.error("Not found customer service by id: {}" , customerServiceId);
                promise.complete(null);
                return;
            }

            logger.info("Successfully  fetch customer service by id: {}" , resultHandler.result().getRows());

            promise.complete(resultHandler.result().getRows().get(0));

        });
        return promise.future();
    }

    public static Future<JsonObject> fetchCustomerServiceByCustomerNumber(final SQLConnection sqlConnection , final String customerNumber , final long serviceId) {

        final Promise<JsonObject> promise = Promise.promise();

        final String query = "SELECT * FROM (select " +
                             "    ID, " +
                             "    SERVICE_ID, " +
                             "    CUSTOMER_ID, " +
                             "    AGENT_ID, " +
                             "    STATUS, " +
                             "    DESCRIPTION, " +
                             "    ACTIVETIME, " +
                             "    OPERATOR_ID, " +
                             "    TO_CHAR(CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, " +
                             "    TO_CHAR(VALIDTO, 'YYYY/MM/DD HH24:MI:SS') as VALIDTO, " +
                             "    ACCOUNTCODE, " +
                             "    ISCARD, " +
                             "    ISVIRTUAL, " +
                             "    POSTCODE, " +
                             "    ADDRESS, " +
                             "    TO_CHAR(LASTACTIVITY, 'YYYY/MM/DD HH24:MI:SS') as  LASTACTIVITY, " +
                             "    CONSIGNMENTCODE, " +
                             "    CARDNUMBER, " +
                             "    AI, " +
                             "    INFO " +
                             " from TNASCUSTOMERSERVICE " +
                             " where CUSTOMER_ID in (select customer.ID from TNASCUSTOMER customer where customer.CUSTOMER_NO = ?) and SERVICE_ID = ?" +
                             " ORDER BY ID desc ) WHERE ROWNUM = 1";

        final JsonArray params = new JsonArray()
                .add(customerNumber)
                .add(serviceId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Fail to get customer service by customer number" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (resultHandler.result() == null || resultHandler.result().getRows() == null || resultHandler.result().getRows().isEmpty()) {
                logger.error("Not found customer service by customer number: {}" , customerNumber);

                JsonObject jo = new JsonObject();
                jo.put("ID" , 0);
                jo.put("STATUS" , CustomerServiceStatus.TERM_CONDITION);
                promise.complete(jo);
                return;
            }

            logger.info("Successfully fetch service by customer number: {} , Rows: {}" , customerNumber , resultHandler.result().getRows());

            final JsonObject row = resultHandler.result().getRows().get(0);
            if (row.containsKey("INFO") && row.getValue("INFO") != null) {
                row.put("INFO" , new JsonObject(row.getValue("INFO").toString()));
            } else row.put("INFO" , new JsonObject());

            promise.complete(row);

        });
        return promise.future();
    }

    public static Future<JsonArray> fetchAllLoans(final SQLConnection sqlConnection , final int customerId , final int customerServiceId) {

        final Promise<JsonArray> promise = Promise.promise();

        final String query = " select " +
                             "    ID, " +
                             "    SERVICE_ID, " +
                             "    CUSTOMER_ID, " +
                             "    AGENT_ID, " +
                             "    STATUS, " +
                             "    DESCRIPTION, " +
                             "    ACTIVETIME, " +
                             "    OPERATOR_ID, " +
                             "    TO_CHAR(CREATIONDATE, 'YYYY/MM/DD HH24:MI:SS') as CREATIONDATE, " +
                             "    TO_CHAR(VALIDTO, 'YYYY/MM/DD HH24:MI:SS') as VALIDTO, " +
                             "    ACCOUNTCODE, " +
                             "    ISCARD, " +
                             "    ISVIRTUAL, " +
                             "    POSTCODE, " +
                             "    ADDRESS, " +
                             "    TO_CHAR(LASTACTIVITY, 'YYYY/MM/DD HH24:MI:SS') as  LASTACTIVITY, " +
                             "    CONSIGNMENTCODE, " +
                             "    CARDNUMBER, " +
                             "    AI, " +
                             "    INFO " +
                             " from TNASCUSTOMERSERVICE " +
                             " where ID = ? and CUSTOMER_ID = ? and SERVICE_ID = ? " +
                             " order by ID desc";

        final JsonArray params = new JsonArray()
                .add(customerServiceId)
                .add(customerId)
                .add(ServiceType.LOAN.getServiceId());

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Fail to fetch loans" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (resultHandler.result() == null || resultHandler.result().getRows() == null || resultHandler.result().getRows().isEmpty()) {
                logger.error("Not found loans");
                promise.complete(new JsonArray());
                return;
            }

            logger.info("Successfully fetch loans , Rows: {}" , resultHandler.result().getRows());

            final JsonArray result = new JsonArray();

            final List<JsonObject> rows = resultHandler.result().getRows();
            for (final JsonObject row : rows) {
                if (row.containsKey("INFO") && row.getValue("INFO") != null) {
                    row.put("INFO" , new JsonObject(row.getValue("INFO").toString()));
                } else row.put("INFO" , new JsonObject());

                result.add(row);
            }

            promise.complete(result);

        });
        return promise.future();
    }

    public static Future<JsonObject> fetchCollateralGuarantor1(final SQLConnection sqlConnection , final int customerId , final int customerServiceId) {

        final Promise<JsonObject> promise = Promise.promise();

        final String query = " select JSON_QUERY(INFO, '$.collateralGuarantor1') as collateralGuarantor1 from TNASCUSTOMERSERVICE " +
                             " where ID = ? and CUSTOMER_ID = ? and SERVICE_ID = ?";

        final JsonArray params = new JsonArray()
                .add(customerServiceId)
                .add(customerId)
                .add(ServiceType.LOAN.getServiceId());

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.queryWithParams(query , params , resultHandler -> {

            if (resultHandler.failed()) {
                logger.error("Fail to fetch collateral guarantor 1" , resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }

            if (resultHandler.result() == null || resultHandler.result().getRows() == null || resultHandler.result().getRows().isEmpty()) {
                logger.error("Not found collateral guarantor 1");
                promise.complete(new JsonObject());
                return;
            }

            logger.info("Successfully fetch collateral guarantor 1 , Rows: {}" , resultHandler.result().getRows().get(0));

            promise.complete(new JsonObject(resultHandler.result().getRows().get(0).getString("COLLATERALGUARANTOR1")));

        });
        return promise.future();
    }

    public static Future<Void> saveCustomerServiceInfoLoan(final SQLConnection sqlConnection , final long customerId , final long customerServiceId , final long loanTypeId , final long amount , final int installment) {
        final Promise<Void> promise = Promise.promise();

        final String query = " UPDATE TNASCUSTOMERSERVICE set " +
                             " INFO = (case when (INFO is null) then ? else json_mergepatch(INFO, ?) end) " +
                             " where ID = ? and CUSTOMER_ID = ?";

        final String info = new JsonObject()
                .put("loanTypeId" , loanTypeId)
                .put("amount" , amount)
                .put("installment" , installment)
                .toString();

        final JsonArray params = new JsonArray()
                .add(info) // then
                .add(info) // else
                .add(customerServiceId)
                .add(customerId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.updateWithParams(query , params , ar ->
        {
            if (ar.failed()) {
                logger.error("Fail to save customer service info loan" , ar.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            logger.trace("Successfully save customer service info loan");

            promise.complete();
        });
        return promise.future();
    }

    public static Future<Void> putInfo(final SQLConnection sqlConnection , final int customerId , final int customerServiceId , final JsonObject info) {
        final Promise<Void> promise = Promise.promise();

        final String query = " UPDATE TNASCUSTOMERSERVICE set " +
                             " INFO = json_mergepatch(INFO, ?) " +
                             " where ID = ? and CUSTOMER_ID = ?";

        final JsonArray params = new JsonArray()
                .add(info.toString())
                .add(customerServiceId)
                .add(customerId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.updateWithParams(query , params , ar ->
        {
            if (ar.failed()) {
                logger.error("Fail to save customer service info" , ar.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            logger.trace("Successfully save customer service info");

            promise.complete();
        });
        return promise.future();
    }

    public static Future<Void> changeStatusCustomerServiceById(final SQLConnection sqlConnection , final long customerServiceId , final CustomerServiceStatus status) {
        final Promise<Void> promise = Promise.promise();

        String query = "UPDATE TNASCUSTOMERSERVICE set STATUS = ? where ID = ?";

        final JsonArray params = new JsonArray()
                .add(status)
                .add(customerServiceId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.updateWithParams(query , params , ar ->
        {
            if (ar.failed()) {
                logger.error("Fail to change status customer service" , ar.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            logger.trace("Successfully change status customer service");

            promise.complete();
        });
        return promise.future();
    }

    //TODO
    public static Future<Void> changeStatusCustomerServiceById(final SQLConnection sqlConnection , final long customerServiceId , final CustomerServiceStatus status , JsonObject joInfo) {
        final Promise<Void> promise = Promise.promise();

        String query = "UPDATE TNASCUSTOMERSERVICE set STATUS = ?, INFO=json_mergepatch(INFO, ?) where ID = ?";

        final JsonArray params = new JsonArray()
                .add(status)
                .add(joInfo.toString())
                .add(customerServiceId);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.updateWithParams(query , params , ar ->
        {
            if (ar.failed()) {
                logger.error("Fail to change status customer service" , ar.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            logger.trace("Successfully change status customer service");

            promise.complete();
        });
        return promise.future();
    }

    public static Future<Void> saveCustomerServiceHistory(final SQLConnection sqlConnection , final JsonObject customerService , final long userId , final CustomerServiceHistory CustomerServiceHistory , final String description) {
        final Promise<Void> promise = Promise.promise();

        String query = "insert into TNASCUSTOMERSERVICEHISTORY " +
                       " ( " +
                       "    ID, " +
                       "    CUSTOMERSERVICE_ID, " +
                       "    STATUS, " +
                       "    DESCRIPTION, " +
                       "    ACTIVETIME, " +
                       "    HISTORYBY_ID, " +
                       "    HISTORYTYPE, " +
                       "    HISTORYDATE, " +
                       "    HISTORYDESCRIPTION " +
                       " ) " +
                       " values (SNASCUSTOMERSERVICEHISTORY.nextval, ?, ?, ?, ?, ?, ?, sysdate, ?)";

        final JsonArray params = new JsonArray()
                .add(customerService.getLong("ID"))
                .add(customerService.getString("STATUS"))
                .add(customerService.getString("DESCRIPTION"))
                .add(customerService.getInteger("ACTIVETIME"))
                .add(userId)
                .add(CustomerServiceHistory.name())
                .add(description);

        logger.info("Executing -> Query: {} , Params: {} , ParamsSize: {}" , query , params , params.size());
        sqlConnection.updateWithParams(query , params , ar ->
        {
            if (ar.failed()) {
                logger.error("Fail to save service customer history" , ar.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                return;
            }

            logger.trace("Successfully save service customer history");

            promise.complete();
        });
        return promise.future();
    }

    public static Future<Void> updateCustomerServiceStatus(SQLConnection sqlConnection , Integer customerServiceId , String status , String description , Long activeTime , int userId , CustomerServiceHistory historyType) {
        return updateCustomerServiceStatus(sqlConnection , customerServiceId , status , description , activeTime , userId , historyType , null);
    }

    public static Future<Void> updateCustomerServiceStatus(SQLConnection sqlConnection , Integer customerServiceId , String status , String description , Long activeTime , int userId , CustomerServiceHistory historyType , JsonObject info) {

        Promise<Void> promise = Promise.promise();

        JsonArray paramsUpdate = new JsonArray();
        paramsUpdate.add(status);
        paramsUpdate.add(description);
        //        if (info != null) {
        //        	paramsUpdate.add(info);
        //		}
        paramsUpdate.add(activeTime);
        paramsUpdate.add(customerServiceId);

        StringBuilder queryUpdate = new StringBuilder();

        queryUpdate.append("update TNASCUSTOMERSERVICE cs set cs.status=?, cs.description=nvl(?, cs.description),");
        if (info != null) {
            queryUpdate.append(" cs.INFO=json_mergepatch(cs.INFO, '" + info.toString() + "'), ");
        }
        queryUpdate.append(" cs.LASTACTIVITY=sysdate, cs.ACTIVETIME=nvl(?, cs.ACTIVETIME) where cs.id=? ");

        sqlConnection.queryWithParams(queryUpdate.toString() , paramsUpdate , updateHandler -> {
            if (updateHandler.failed()) {
                logger.error("Unable to get accessQueryResult:" + updateHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                logger.trace("updateCustomerServiceStatusSuccessful");


                JsonArray paramsInsert = new JsonArray();
                paramsInsert.add(customerServiceId);
                paramsInsert.add(status);
                paramsInsert.add(description);
                paramsInsert.add(activeTime);
                paramsInsert.add(userId);
                paramsInsert.add(historyType.name());
                paramsInsert.add("");

                String queryInsert = ""
                                     + "insert into tnascustomerservicehistory ("
                                     + "id,"
                                     + "CUSTOMERSERVICE_ID,"
                                     + "STATUS,"
                                     + "DESCRIPTION,"
                                     + "ACTIVETIME,"
                                     + "historyBy_ID,"
                                     + "HistoryType,"
                                     + "historyDescription,"
                                     + "historyDate)"
                                     + "values(snascustomerservicehistory.nextval,?,?,?,?,?,?,?,sysdate) ";

                sqlConnection.updateWithParams(queryInsert , paramsInsert , resultHandler -> {
                    if (resultHandler.failed()) {
                        logger.error("Unable to get accessQueryResult:" , resultHandler.cause());
                    }

                    logger.trace("doSaveCustomerServiceHistorySuccessful");
                    promise.complete();

                });

            }

        });
        return promise.future();

    }

}
