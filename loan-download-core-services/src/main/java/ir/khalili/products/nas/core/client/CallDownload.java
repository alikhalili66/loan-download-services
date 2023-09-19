package ir.khalili.products.nas.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class CallDownload extends AbstractVerticle {

    public static void main(String[] args) {

        System.out.println("CallLoan STARTING ......");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new CallDownload());
    }

    @Override
    public void start() throws Exception {
        final WebClient client = WebClient.create(vertx);

        creditReportNaturalPerson(client);
    }

    public void creditReportNaturalPerson(WebClient client) {
        JsonObject joInput = new JsonObject();

        joInput.put("customerServiceId" , 2175);

        System.out.println("joInput:" + joInput);
        try {
            client.post(8089 , "127.0.0.1" , "/v1/service/loan/inquiry/download").sendJson(joInput , ar -> {
//            client.post(8089 , "192.168.251.112" , "/v1/service/loan/inquiry/download").sendJson(joInput , ar -> {

                try {
                    if (ar.succeeded()) {
                        JsonObject response = new JsonObject(ar.result().bodyAsString());
                        System.out.println(Json.encodePrettily(response));
                    } else {
                        System.out.println(ar.cause());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    System.exit(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


}
