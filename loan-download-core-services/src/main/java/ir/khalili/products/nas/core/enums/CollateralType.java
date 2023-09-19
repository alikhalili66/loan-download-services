package ir.khalili.products.nas.core.enums;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public enum CollateralType {
    CHEQUE('C' , "چک")
    //
    ;
    private static final Logger logger = LogManager.getLogger(CollateralType.class);

    public final char type;
    public final String persianName;

    CollateralType(final char type , final String persianName) {
        this.type = type;
        this.persianName = persianName;
    }

    public static CollateralType to(final String typeStr) {

        if (StringUtils.isNotEmpty(typeStr) && typeStr.length() == 1) {
            try {
                final char type = typeStr.charAt(0);
                for (final CollateralType value : values()) {
                    if (value.type == type) return value;
                }
            } catch (Exception e) {
                logger.error("Fail to convert enum type to enum name: " + typeStr , e);
            }
        }

        return null;
    }

    public static CollateralType toEnum(final String type) {
        if (type != null && !type.isEmpty()) {
            try {
                return valueOf(type);
            } catch (Exception e) {
                logger.error("Fail to enum: " + type , e);
            }
        }
        return null;
    }
}
