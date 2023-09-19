package ir.khalili.products.nas.core.enums;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public enum CustomerServiceStatus {

    TERM_CONDITION,
    VERIFICATION,
    JOB,
    ADDRESS,
    CONFIRM_INFO,
    SELECT_LOAN,
    PAYMENT,
    INQUIRY,
    INQUIRY_CONFIRM,
    UPLOAD_DOCUMENT,
    COLLATERAL,
    COLLATERAL_GUARANTOR_1,
    COLLATERAL_GUARANTOR_2,
    VALIDATE_INFO_BY_OPERATOR,
    DONE,
    CARTABLE,
    CANCEL,
    REJECT,

    ;
    private static final Logger logger = LogManager.getLogger(CustomerServiceStatus.class);

    public static CustomerServiceStatus toEnum(final String status) {
        if (status != null && !status.isEmpty()) {
            try {
                return valueOf(status);
            } catch (Exception e) {
                logger.error("Fail to enum: " + status , e);
            }
        }
        return null;
    }
}
