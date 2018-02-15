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
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class API {
    private static final String endpoint = "/api/transponders";

    String url;
    String server;
    String apiKey;

    HttpClient client;
    SimpleDateFormat dateFormatGmt;

    private static String getThumbprint(X509Certificate cert)
            throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        String digestHex = DatatypeConverter.printHexBinary(digest);
        return digestHex;
    }

    public API(String server, String apiKey) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, IOException {
        url = server + endpoint;
        this.server = server;
        this.apiKey = apiKey;

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (chain, authType) -> {
                    chain[0].checkValidity();
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
                .put("StatusCode", statusCode)
                .put("Key", apiKey);

        String json = message.toString();

        System.out.printf("heartbeat: %s\n", json);

        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(json));

        HttpResponse response = client.execute(request);

        System.out.printf("heardbeat: %d\n", response.getStatusLine().getStatusCode());
    }
    public void post(int antennaId, String transponderId) throws JSONException, IOException {
        HttpPost request = new HttpPost(url);

        Date now = new Date();

        JSONObject message = new JSONObject()
                .put("AntennaId", antennaId)
                .put("TransponderId", transponderId)
                .put("TransponderType", 2) // TDM
                .put("TimeStamp", dateFormatGmt.format(now))
                .put("Key", apiKey);

        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(message.toString()));

        System.out.printf("send %s to %s\n", message.toString(), url);

        HttpResponse response = client.execute(request);

        System.out.println("Tag Response: " + response.getStatusLine().getStatusCode());
    }
}
