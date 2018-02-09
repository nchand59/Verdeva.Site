package com.paybycar.site;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class API {
    private static final String endpoint = "/api/transponders";

    String url;
    String server;

    HttpClient client;
    SimpleDateFormat dateFormatGmt;

    public API(String server) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException {
        url = server + endpoint;
        this.server = server;

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (chain, authType) -> {

                    for(int ii = 0; ii< chain.length; ii++){
                        chain[ii].checkValidity();
                        System.out.printf("%s: %s\n", chain[ii].getIssuerDN().getName(), chain[ii].getSerialNumber().toString());
                    }

                    return true;
                })
                .build();

        client = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

        dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void heartbeat(int antennaId, int statusCode) throws JSONException, IOException {
        String url = server + "/api/appliance/heartbeat";

        System.out.printf("%s %d %d\n", url, antennaId, statusCode);

        HttpPost request = new HttpPost(url);

        JSONObject message = new JSONObject()
                .put("AntennaId", antennaId)
                .put("StatusCode", statusCode);

        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(message.toString()));

        HttpResponse response = client.execute(request);

        System.out.printf("heardbeat: %d\n", response.getStatusLine().getStatusCode());
    }
    public void post(int antennaId, String transponderId) throws JSONException, IOException {
        HttpPost request = new HttpPost(url);

        Date now = new Date();

        JSONObject message = new JSONObject()
                .put("AntennaId", antennaId)
                .put("TransponderId", transponderId)
                .put("TransponderType", 2)
                .put("TimeStamp", dateFormatGmt.format(now)); // TDM

        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(message.toString()));

        System.out.printf("send %s to %s\n", message.toString(), url);

        HttpResponse response = client.execute(request);

        System.out.println(response.getStatusLine().getStatusCode());
    }
}
