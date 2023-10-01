package ir.khalili.products.nas.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.StaticHandler;
import ir.khalili.products.nas.core.helper.HelperInitial;
import ir.khalili.products.nas.core.routemanager.request.RtMgr_16_DownloadCreditReportNaturalPerson;
import ir.khalili.products.nas.core.verticle.request.VRTCL_16_DownloadCreditReportNaturalPerson;

public class EntryPoint extends AbstractVerticle {

    private static final Logger logger = LogManager.getLogger(EntryPoint.class);
    public static Vertx vertx;
    public static JsonObject joConfig;
    private static Router router;
    private static Router routerGateway;
    private static int port;
    public static int clientId = 1;

    public static void main(String[] args) {
        logger.info("STARTING ......");

		logger.info("args.length ...... " + args.length);

		InputStream in;
		
		try {
			
			if(args.length == 1) {
				logger.info("configPath ...... " + args[0]);
				///opt/apprepo/config/kyc.json
				in = new FileInputStream(new File(args[0]));
			} else {
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.json");
			}

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(
                    new InputStreamReader(in , Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }

            String jsonString = textBuilder.toString();
            logger.info("jsonString:" + jsonString);

            joConfig = new JsonObject(jsonString);
            port = joConfig.getInteger("port");
            
        } catch (Exception e) {
            logger.error("configNotValid:" , e);
            System.exit(0);
        }

        try {
            port = joConfig.getInteger("port");
        } catch (Exception e) {
            logger.error("invalidConfig:" , e);
            System.exit(0);
        }

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(1);

        vertx = Vertx.vertx();
        vertx.deployVerticle(EntryPoint.class.getName() , deploymentOptions , resHandler -> {

            if (resHandler.failed()) {
                logger.error("deployVerticleFailed:" , resHandler.cause());
                vertx.close();
                System.exit(0);
            }

            HelperInitial.initialize(vertx).onComplete(handler -> {

                if (handler.failed()) {
                    logger.error("HelperInitial.initialize.Failed:" , handler.cause());
                    vertx.close();
                    System.exit(0);
                }

                deployVerticle();

            });
        });

    }

    private static void deployVerticle() {

    	@SuppressWarnings("rawtypes")
		List<Future> list = new ArrayList<>();
    	
    	list.add(vertx.deployVerticle(VRTCL_16_DownloadCreditReportNaturalPerson.class.getName()));

        CompositeFuture.join(list).onComplete(handler->{
        	
        	if(handler.failed()) {
        		logger.error("Deploy.Verticle.Failed:", handler.cause());
                vertx.close();
                System.exit(0);
        	}

            String version = "";
            try {
            	final Properties properties = new Properties();
    			properties.load(EntryPoint.class.getClassLoader().getResourceAsStream("project.properties"));
    			version = properties.getProperty("version");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
        	System.out.println();
        	System.out.println();
        	System.out.println("***************************************************************************");
        	System.out.println("***************************************************************************");
        	System.out.println("*******************************LOAN_DOWNLOAD_START*************************");
        	System.out.println("*******************************"+ version+ "******************");
        	System.out.println("***************************************************************************");
        	System.out.println("***************************************************************************");
        	System.out.println();
        	System.out.println();
        	
    	});
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        /*********************************************************/
        /*******************CorsHandler***************************/
        /*********************************************************/

        CorsHandler corsHandler = CorsHandler.create();
        corsHandler.allowedMethod(HttpMethod.POST);
        corsHandler.allowedMethod(HttpMethod.PUT);
        corsHandler.allowedMethod(HttpMethod.GET);
        corsHandler.allowedMethod(HttpMethod.DELETE);
        corsHandler.allowedHeader("agentSession");
        corsHandler.allowedHeader("customerSession");
        corsHandler.allowedHeader("Content-Type");
        corsHandler.allowedHeader("Access-Control-Request-Method");
        corsHandler.allowedHeader("Access-Control-Allow-Credentials");
        corsHandler.allowedHeader("Access-Control-Allow-Origin");
        corsHandler.allowedHeader("Access-Control-Allow-Headers");

        /*********************************************************/
        /*******************Router********************************/
        /*********************************************************/
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.route().handler(corsHandler);
        router.route().handler(ResponseTimeHandler.create());

        routerGateway = Router.router(vertx);
        routerGateway.route().handler(BodyHandler.create());
        routerGateway.route().handler(StaticHandler.create());
        routerGateway.route().handler(corsHandler);
        routerGateway.route().handler(ResponseTimeHandler.create());

        /*********************************************************/
        /*******************RouteManager**************************/
        /*********************************************************/


        router.post("/v1/service/loan/inquiry/download"			).handler(RtMgr_16_DownloadCreditReportNaturalPerson         ::handler);


        vertx.createHttpServer().requestHandler(router).listen(port);

        /*********************************************************/
        /*********************************************************/

        startPromise.complete();
    }


}
