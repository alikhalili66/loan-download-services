package ir.khalili.products.nas.core.helper.external.services.kyc;

import static ir.khalili.products.nas.core.EntryPoint.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.nas.core.EntryPoint;

public abstract class KycHelper {

	protected static final WebClient client = WebClient.create(vertx);
	protected static Logger logger = LoggerFactory.getLogger(KycHelper.class);

	public static final int port = EntryPoint.joConfig.getJsonObject("lotus").getInteger("port");
	public static final String host = EntryPoint.joConfig.getJsonObject("lotus").getString("host");
	public static final String url = EntryPoint.joConfig.getJsonObject("lotus").getString("url");
	
}
