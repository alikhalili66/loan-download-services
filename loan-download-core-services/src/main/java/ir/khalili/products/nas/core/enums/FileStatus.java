package ir.khalili.products.nas.core.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum FileStatus {
    INITIAL('I'),
    REJECT('R'),
    CONFIRM('C'),
    DOCUMENT_DEFECTS('D')
    //
    ;
    public final char type;
    private static final Logger logger = LogManager.getLogger(FileStatus.class);

    FileStatus(final char type) {
        this.type = type;
    }

    public static FileStatus toEnum(final String type) {
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
