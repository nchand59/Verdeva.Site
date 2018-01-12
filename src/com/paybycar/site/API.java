package com.paybycar.site;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class API {
    private static final String endpoint = "/api/transponders";

    String url;

    HttpClient client;
    SimpleDateFormat dateFormatGmt;

    public API(String server){
        url = server + endpoint;
        client = HttpClientBuilder.create().build();

        dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
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
