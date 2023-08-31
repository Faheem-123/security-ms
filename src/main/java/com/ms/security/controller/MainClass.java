package com.ms.security.controller;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.clientapi.core.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainClass {
    private static final String ZAP_PROXY_HOST = "localhost";
    private static final int ZAP_PROXY_PORT = 8080;
    private static final String API_TARGET_URL = "https://public-firing-range.appspot.com";

    public static void main(String[] args) {


        /*
        try {
            // Initialize ZAP client API
            ClientApi clientApi = new ClientApi(ZAP_PROXY_HOST, ZAP_PROXY_PORT);

            // Start a new ZAP session
            clientApi.core.newSession("name1","name2");

            // Access your API to perform scanning
            clientApi.accessUrl(API_TARGET_URL);

            // Spider your API to discover endpoints
            clientApi.spider.scan(API_TARGET_URL, null, null, null, null);

            // Wait for the spidering to complete
            while (clientApi.spider.status(API_TARGET_URL).getName().equals("running")) {
                Thread.sleep(1000);
            }

            // Activate the passive scanning mode
            clientApi.ascan.enableAllScanners("");

            // Run the active scanning
            clientApi.ascan.scan(API_TARGET_URL, "True", "False", null, null, null);



        System.out.println(s.compareTo(s2));

         */

    }
}
