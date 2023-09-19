package ir.khalili.products.nas.core.utils.DocumentsDirectory;

import ir.khalili.products.nas.core.EntryPoint;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author @bardiademon
 */
public class DocumentsDirectory {
    private static final Logger logger = LogManager.getLogger(DocumentsDirectory.class);

    protected DocumentsDirectory() {
    }

    public static File getDir(final String docsDirName) throws IOException {
        final String docs = EntryPoint.joConfig.getString("docs");

        if (StringUtils.isEmpty(docs)) {
            logger.error("Not found customer docs path: {}" , EntryPoint.joConfig.toString());
            throw new IOException("Not found customer docs path");
        }

        /*
         * @see resources/conf/config.json -> docs
         */
        final File directory = new File(docs + File.separator + docsDirName);

        if (!directory.exists() && !directory.mkdirs()) {
            logger.error("Fail to create directory: {}" , directory.getPath());
            throw new IOException("Fail to create directory: " + directory.getPath());
        }

        return directory;
    }
}
