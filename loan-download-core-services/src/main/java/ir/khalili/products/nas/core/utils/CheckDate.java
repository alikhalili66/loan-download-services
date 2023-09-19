package ir.khalili.products.nas.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author @bardiademon
 */
public final class CheckDate {
    private CheckDate() {

    }

    public static boolean check(final String date , final String pattern) {
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            final Date parse = dateFormat.parse(date);
            return parse != null;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
