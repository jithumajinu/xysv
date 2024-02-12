package io.crm.app.controller;


import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.smstemplate.CreateSmsTemplateRequest;
import io.crm.app.model.smstemplate.SmsTemplateResponse;
//import io.crm.app.utils.SMSGlobal;
import io.crm.app.utils.SMSGlobalTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//------------------------------------------------
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/bulksms")
@RequiredArgsConstructor
public class SmsTestController {

    @PostMapping({"", "/test"})
    public ApiResponse<SmsTemplateResponse> createSmsTemplate(
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<SmsTemplateResponse>builder()
                .companyPublish(true);
        try {
            System.out.println("--------createbulkSmsTemplate-- impl-------:" + "request.toString()");
            getGroups();
            //SmsTemplateResponse response = smsTemplateService.createSmsTemplate(request);
            //responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }


    public void getGroups(){
        SMSGlobalTest apiObj = new SMSGlobalTest();
        apiObj.setApiKey("204f5a09f926efd995561b9b53767e9d", "911e93437a1856ef5283d232d37c77e6");
        // send a message
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("offset", "1");
            params.put("limit", "20");
            System.out.println(apiObj.listGroups("application/json", params));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
    public void sendSMS() {

        SMSGlobalTest apiObj = new SMSGlobalTest();
        // apiObj.setApiKey("ec55b213be68255fead2273826f19a00", "2e3fb7a3c6921039cbbda04307a009f2");
        apiObj.setApiKey("204f5a09f926efd995561b9b53767e9d", "911e93437a1856ef5283d232d37c77e6");
        // get all messages
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("limit", "10");
//        try {
//            System.out.println(apiObj.getOutGoingMessages("application/xml", params));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // send a message
        try {
            System.out.println(apiObj.sendMessage("application/json", "application/json", new HashMap<String, String>()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


//        String apiKey = "204f5a09f926efd995561b9b53767e9d";
//        String apiSecret = "911e93437a1856ef5283d232d37c77e6";
//        String destinationNumber = "919633346436";
//        String message = "your_message";
//
//        String url = "https://api.smsglobal.com/v2/sms/";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth(apiKey, apiSecret);
//
//        SmsPayload payload = new SmsPayload(destinationNumber, message);
//        HttpEntity<SmsPayload> request = new HttpEntity<>(payload, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            System.out.println("SMS sent successfully!");
//        } else {
//            System.out.println("Failed to send SMS. Error: " + response.getBody());
//        }

        //============================================================
//        String apiKey ="204f5a09f926efd995561b9b53767e9d";
//        String apiSecret = "911e93437a1856ef5283d232d37c77e6";
//        String destinationNumber = "919633346436";
//        String message = "Hello, this is a test message.";
//
//        try {
//            // Construct the URL for the SMS Global API
//            URL url = new URL("https://api.smsglobal.com/v2/group");
//
//            // Create a connection to the URL
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            // Set the API credentials in the request headers
//            conn.setRequestProperty("Authorization", "Basic " + encodeCredentials(apiKey, apiSecret));
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.
//
//            // Construct the JSON payload
//            String payload = String.format("{\"destination\":\"%s\",\"message\":\"%s\"}", destinationNumber, message);
//
//            // Send the request
//            OutputStream os = conn.getOutputStream();
//            os.write(payload.getBytes());
//            os.flush();
//            os.close();
//
//            // Read the response
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();
//
//            // Print the response
//            System.out.println("Response: " + response.toString());
//
//            // Close the connection
//            conn.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    private static String encodeCredentials(String apiKey, String apiSecret) {
//        String credentials = apiKey + ":" + apiSecret;
//        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
//    }
}

//class SmsPayload {
//    private String destination;
//    private String message;
//
//    public SmsPayload(String destination, String message) {
//        this.destination = destination;
//        this.message = message;
//    }
//
//    // Getters and setters
//}
