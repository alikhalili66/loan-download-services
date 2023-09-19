package ir.khalili.products.nas.core.utils;

import io.vertx.core.json.JsonObject;
import ir.khalili.products.nas.core.utils.excp.WSEXCP_IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author @bardiademon
 */
public final class Base64Convertor {
    private static final Logger logger = LogManager.getLogger(Base64Convertor.class);

    public static final String KEY_RESULT_FILE = "file", KEY_RESULT_FILENAME = "filename", KEY_RESULT_CONTENT_TYPE = "content_type", KEY_RESULT_FILE_SIZE = "file_size";

    private Base64Convertor() {
    }

    public static String toImageFile(final String base64 , File output) throws IOException {
        return toImageFile(base64 , output , null);
    }

    public static String toImageFile(final String base64 , File output , final String filename) throws IOException {
        logger.info("Decoding base64: {}" , base64.length());
        final byte[] decode = Base64.getDecoder().decode(base64);

        logger.info("decoded base64");

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        final String outputFilename = filename == null ? UUID.randomUUID().toString() : realFilenameToOutputFilename(filename);
        output = new File(output.getPath() + File.separator + outputFilename);

        logger.info("Get content type");
        final String contentType = GetContentType.get(inputStream);

        final BufferedImage doc = ImageIO.read(inputStream);

        String type;
        try {
            type = contentType.split("/")[1];
        } catch (Exception e) {
            type = "png";
        }

        logger.info("Writing file -> type: {} , output: {}" , type , output);

        final boolean written;
        try {
            written = ImageIO.write(doc , type , output);
        } catch (Exception e) {
            throw new IOException("Cannot write image");
        }

        logger.info("File write: {}" , written);

        logger.info("Closing inputStream");
        inputStream.close();
        logger.info("Closed inputStream");

        if (written) return output.getPath();
        else throw new IOException("Cannot write file: " + output.getPath());
    }

    public static String toFile(final String base64 , File output) throws IOException {
        return toFile(base64 , output , null);
    }

    public static String toFile(final String base64 , File output , final String filename) throws IOException {
        logger.info("Decoding base64: {}" , base64.length());
        final byte[] decode = Base64.getDecoder().decode(base64);

        logger.info("decoded base64");

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        final String outputFilename = filename == null || filename.isEmpty() ? UUID.randomUUID().toString() : realFilenameToOutputFilename(filename);
        output = new File(output.getPath() + File.separator + outputFilename);

        logger.info("Writing file ->  output: {}" , output);

        try {
            Files.write(output.toPath() , inputStream.readAllBytes());
        } catch (Exception e) {
            logger.error("Cannot write file" , e);
            throw new IOException("Cannot write file");
        }

        logger.info("File written");

        logger.info("Closing inputStream");
        inputStream.close();
        logger.info("Closed inputStream");

        return output.getPath();
    }

    public static boolean validationImage(final String base64) {
        return (validationImage(base64 , 0));
    }

    public static boolean validationImage(final String base64 , final long validSize) {
        try {
            logger.info("Validation base64");
            logger.info("Decoding base64: {}" , base64.length());
            final byte[] decode = Base64.getDecoder().decode(base64);

            if (validSize > 0 && decode.length > validSize) return false;

            logger.info("Image size is valid: {}" , validSize);

            return ImageIO.read(new ByteArrayInputStream(decode)) != null;
        } catch (Exception e) {
            logger.info("Invalid" , e);
            return false;
        }
    }

    public static JsonObject fileToBase64(final String filePath) throws IOException {

        if (filePath == null || filePath.isEmpty()) {
            logger.info("FilePath is empty");
            throw new IOException("File path is null");
        }

        final File file = new File(filePath);
        if (!file.exists()) {
            logger.info("File not found: {}" , file.getPath());
            throw new FileNotFoundException("File not found: " + file.getPath());
        }

        try (final FileInputStream inputStream = new FileInputStream(file)) {
            final String encodedString = Base64.getEncoder().encodeToString(inputStream.readAllBytes());

            final Path path = new File(filePath).toPath();
            final String filename = outputFilenameToRealFilename(file.getName());
            final String contentType = GetContentType.get(filePath);
            final long fileSize = Files.size(path);

            logger.info("Get file path: {}" , file.getPath());
            logger.info("Get filename: {}" , filename);
            logger.info("Get content type: {}" , contentType);
            logger.info("Get file size: {}" , fileSize);

            final JsonObject result = createInfoFileJson(encodedString , contentType , fileSize , filename);

            logger.info("File info: {}" , result);

            return result;
        }
    }

    public static JsonObject streamToBase64(final InputStream stream , final String filename) throws IOException {

        if (stream == null) {
            logger.info("FilePath is empty");
            throw new IOException("File path is null");
        }

        final byte[] bytes = stream.readAllBytes();

        final String encodedString = Base64.getEncoder().encodeToString(bytes);

        final String contentType = GetContentType.get(stream);
        final long fileSize = bytes.length;

        logger.info("Get filename: {}" , filename);
        logger.info("Get content type: {}" , contentType);
        logger.info("Get file size: {}" , fileSize);

        final JsonObject result = createInfoFileJson(encodedString , contentType , fileSize , filename);

        logger.info("File info: {}" , result);

        return result;
    }

    public static String fileToBase64String(final String filePath) throws IOException {

        if (filePath == null || filePath.isEmpty()) {
            logger.info("FilePath is empty");
            throw new IOException("File path is null");
        }

        final File file = new File(filePath);
        if (!file.exists()) {
            logger.info("File not found: {}" , file.getPath());
            throw new FileNotFoundException("File not found: " + file.getPath());
        }

        try (final FileInputStream inputStream = new FileInputStream(file)) {
            return Base64.getEncoder().encodeToString(inputStream.readAllBytes());
        }
    }

    private static String realFilenameToOutputFilename(final String filename) {
        return String.format("%d_%s" , new Date().getTime() , filename.trim());
    }

    private static String outputFilenameToRealFilename(final String outputFilename) {
        try {
            final String PATTERN = "t([0-9]+)t_([\\s\\S]*)";
            final Matcher matcher = Pattern.compile(PATTERN).matcher(outputFilename);
            if (matcher.find()) {
                return matcher.group(2).trim();
            }
        } catch (Exception e) {
            logger.error("Fail to get output real filename: {}" , outputFilename , e);
        }
        return outputFilename;
    }

    public static boolean validationBase64(final String base64) {
        try {
            logger.info("Validation base64");
            logger.info("Decoding base64: {}" , base64.length());
            final byte[] decode = Base64.getDecoder().decode(base64);
            return decode != null && decode.length > 0;
        } catch (Exception e) {
            logger.info("Invalid" , e);
            return false;
        }
    }

    public static JsonObject fileToBase64(InputStream in) throws IOException {


        final String encodedString = Base64.getEncoder().encodeToString(in.readAllBytes());


        final JsonObject result = createInfoFileJson(encodedString , null , 1);

        logger.info("File info: {}" , result);

        return result;


    }

    public static JsonObject createEmptyInfoFileJson() {
        return createInfoFileJson(null , null , 0L);
    }

    public static JsonObject createInfoFileJson(final String file , final String contentType , final long fileSize) {
        return createInfoFileJson(file , contentType , fileSize , null);
    }

    public static JsonObject createInfoFileJson(final String file , final String contentType , final long fileSize , final String filename) {
        return new JsonObject()
                .put(KEY_RESULT_FILE , file == null ? "" : file)
                .put(KEY_RESULT_CONTENT_TYPE , contentType == null ? "" : contentType)
                .put(KEY_RESULT_FILE_SIZE , fileSize)
                .put(KEY_RESULT_FILENAME , filename);
    }

    public static String toBase64(final byte[] bytes) {
        try {
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            logger.error("Fail to convert bytes to base64" , e);
        }
        return null;
    }

    public static String writeImage(final String image , final File output) throws WSEXCP_IOException {
        return writeImage(image , output , null);
    }

    public static String writeImage(final String image , final File output , final String filename) throws WSEXCP_IOException {
        try {
            logger.info("Write image to file: {}" , output);
            final String imagePath = Base64Convertor.toImageFile(image , output , filename);

            logger.info("Written image: {}" , imagePath);
            return imagePath;
        } catch (IOException e) {
            logger.error("Fail to write doc" , e);
            throw new WSEXCP_IOException("خطا در آپلود فایل لطفا فرمت فایل را بررسی کنید");
        } catch (Exception e) {
            logger.error("Internal server error" , e);
            throw new WSEXCP_IOException("خطای داخلی لطفا با راهبر سامانه تماس بگیرید.");
        }
    }

    public static InputStream toInputStream(final String base64) throws Exception {
        try {
            logger.info("Decoding: {}" , base64);
            final byte[] decode = Base64.getDecoder().decode(base64);
            final InputStream inputStream = new ByteArrayInputStream(decode);
            logger.info("Successfully decode base64: {}" , decode.length);

            return inputStream;
        } catch (Exception e) {
            logger.error("Fail to decode base64: {}" , base64 , e);
            throw e;
        }
    }

    public static String write(final String file , final File output) throws WSEXCP_IOException {
        return write(file , output , null);
    }

    public static String write(final String file , final File output , final String filename) throws WSEXCP_IOException {
        try {
            logger.info("Write file: {}" , output);
            final String imagePath = Base64Convertor.toFile(file , output , filename);

            logger.info("Written file: {}" , imagePath);
            return imagePath;
        } catch (IOException e) {
            logger.error("Fail to write file" , e);
            throw new WSEXCP_IOException("خطا در آپلود فایل لطفا فرمت فایل را بررسی کنید");
        } catch (Exception e) {
            logger.error("Internal server error" , e);
            throw new WSEXCP_IOException("خطای داخلی لطفا با راهبر سامانه تماس بگیرید.");
        }
    }
}
