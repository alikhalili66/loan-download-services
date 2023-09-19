package ir.khalili.products.nas.core.utils;

import io.vertx.core.json.JsonObject;
import ir.khalili.products.nas.core.EntryPoint;

public class Configuration {

	public static JsonObject config = null;
	
	public static JsonObject getDataBaseConfig() {
		
		String URL = EntryPoint.joConfig.getJsonObject("dbConfig").getString("url");
		String USERNAME = EntryPoint.joConfig.getJsonObject("dbConfig").getString("username");
		String PASSWORD = EntryPoint.joConfig.getJsonObject("dbConfig").getString("password");
//		String HOST = EntryPoint.joConfig.getJsonObject("dbConfig").getString("host");
//		String SERVICE_NAME = EntryPoint.joConfig.getJsonObject("dbConfig").getString("serviceName");
//		int PORT = EntryPoint.joConfig.getJsonObject("dbConfig").getInteger("port") ;
		
    	return new JsonObject()
			.put("url"		, URL)
			.put("provider_class", "io.vertx.ext.jdbc.spi.impl.C3P0DataSourceProvider")
			.put("driver_class"	, "oracle.jdbc.driver.OracleDriver")
			.put("user"		, USERNAME)
			.put("password"	, PASSWORD)
			;
	}

}
