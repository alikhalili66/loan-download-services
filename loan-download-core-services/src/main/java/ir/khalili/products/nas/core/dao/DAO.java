package ir.khalili.products.nas.core.dao;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.nas.core.excp.dao.DAOEXCP_Internal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAO {
    private static final Logger logger = LogManager.getLogger(DAO.class);

    public static Future<Long> fetchSequenceId(final SQLConnection sqlConnection , final String sequenceName) {
        final Promise<Long> promise = Promise.promise();

        final String query = String.format("select %s.nextval from dual" , sequenceName);

        logger.info("Executing -> Query: {}" , query);
        sqlConnection.query(query , sequenceResult -> {
            if (sequenceResult.failed()) {
                logger.error("Fail to fetch sequence id: {}" , sequenceName , sequenceResult.cause());
                promise.fail(new DAOEXCP_Internal(-200 , "خطای داخلی، با راهبر سامانه تماس بگیرید."));
                return;
            }
            promise.complete(sequenceResult.result().getRows().get(0).getLong("NEXTVAL"));
        });

        return promise.future();
    }

    public static String createQueryForInClause(int paramSize) {

        StringBuilder inClause = new StringBuilder();
        inClause.append("?, ".repeat(Math.max(0 , paramSize)));
        String str = inClause.toString();
        if (str.endsWith(", ")) {
            str = str.substring(0 , str.length() - 2);
        }

        return str;
    }
}
