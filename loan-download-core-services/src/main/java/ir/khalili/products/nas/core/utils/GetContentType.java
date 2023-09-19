package ir.khalili.products.nas.core.utils;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * @author @bardiademon
 */
public final class GetContentType {

    private static final Logger logger = LogManager.getLogger(GetContentType.class);

    private GetContentType() {
    }

    public static String get(final InputStream inputStream) {

        if (inputStream == null) return "";

        final String contentType = getWithTika(inputStream);
        if (contentType != null) return contentType;

        return "";
    }

    public static String get(final String filePath) {

        if (filePath == null || filePath.isEmpty()) return "";

        final File file = new File(filePath);

        if (!file.exists()) return "";

        String contentType = getWithTika(file);
        if (contentType != null) return contentType;

        contentType = getWithMagicMatch(file);
        if (contentType != null) return contentType;

        contentType = getWithProbe(file);
        if (contentType != null) return contentType;

        contentType = getWithUrlConnection(file);
        if (contentType != null) return contentType;

        contentType = getWithUrlConnectionGuess(file);
        if (contentType != null) return contentType;

        return "";
    }

    private static String getWithTika(final InputStream inputStream) {
        final Tika tika = new Tika();
        try {
            return tika.detect(inputStream);
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }

    private static String getWithTika(final File file) {
        final Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }

    private static String getWithMagicMatch(final File file) {
        try {
            final MagicMatch match = Magic.getMagicMatch(file , false);
            return match.getMimeType();
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }

    private static String getWithProbe(final File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }

    private static String getWithUrlConnection(final File file) {
        try {
            return URLConnection.getFileNameMap().getContentTypeFor(file.getName());
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }

    private static String getWithUrlConnectionGuess(final File file) {
        try {
            return URLConnection.guessContentTypeFromName(file.getName());
        } catch (Exception e) {
            logger.error("Fail get content type" , e);
        }
        return null;
    }
}
