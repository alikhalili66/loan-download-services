package ir.khalili.products.nas.core.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum FileType {

    LOAN_NATIONAL_CARD("کارت ملی"),
    LOAN_DOCUMENT_HOUSE("سند ملک"),
    LOAN_DEDUCATION_SALARY("گواهی کسر از حقوق"),
    LOAN_CERTIFICATE_EMPLOYMENT("گواهی اشتغال به کار"),
    LOAN_PAY_SLIP("فیش حقوقی"),
    LOAN_BUSINESS_LICENSE("جواز کسب و کار"),
    APPLICANT_CHECK("چک متقاضی"),
    GUARANTOR_CHECK_1("چک ضامن اول"),
    GUARANTOR_CHECK_2("چک ضامن دوم")
    //
    ;
    public final String persianName;
    private static final Logger logger = LogManager.getLogger(FileType.class);

    FileType(final String persianName) {
        this.persianName = persianName;
    }

    public static FileType toEnum(final String type) {
        if (type != null && !type.isEmpty()) {
            try {
                return valueOf(type);
            } catch (Exception e) {
                logger.error("Fail to enum: {}" , type , e);
            }
        }
        return null;
    }
}
