package io.crm.app.utils;

import io.crm.app.model.smssender.SmsGlobalSendResponse;

import javax.crypto.Mac;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;

public class SMSGlobal {
    private String apiKey;
    private String secretKey;
    private String apiURL;
    private int port;

    private Charset charSet = StandardCharsets.UTF_8;

    /**
     * this constructor will be rarely used, unless the user knows all values to parse to it
     *
     * @param newApiKey                 user's REST API key
     * @param newSecretKey              user's secret key
     * @param newApiURL                 Sg API URL
     * @param newPort                   HTTP port to be used
     */
    public SMSGlobal(String newApiKey, String newSecretKey, String newApiURL, int newPort) {
        this.apiKey = newApiKey;
        this.secretKey = newSecretKey;
        this.apiURL = newApiURL;
        this.port = newPort;
    }

    /**
     * default constructor, setup all parameters needed for the class
     * however user still needs to set their api keys for the calls to work
     */
    public SMSGlobal() {
        this("", "", "api.smsglobal.com", 443);
    }

    /**
     * setup user's API key set
     *
     * @param newApiKey
     * @param newSecretKey
     * @return Nothing
     */
    public void setApiKey(String newApiKey, String newSecretKey) {
        this.apiKey = newApiKey;
        this.secretKey = newSecretKey;
    }

    /**
     * @param newApiURL
     * @return Nothing
     */
    public void setApiURL(String newApiURL) {
        this.apiURL = newApiURL;
    }

    /**
     * @param newPort
     * @return Nothing
     */
    public void setPort(int newPort) {
        this.port = newPort;
    }

    /**
     *
     * @param timeStamp                 UNIX timestamp
     * @param randomString              a random generated string
     * @param method                    RESTful API verb (GET, POST, PUT, DELETE, etc.)
     * @param endPoint                  request URI
     * @return String                   hashed string to be used in the request
     */
    public String generateHash(String timeStamp, String randomString, String method, String endPoint) {
        String strHash = String.format("%s\n%s\n%s\n%s\n%s\n%s\n\n", timeStamp, randomString, method, endPoint, this.apiURL, this.port);
        String hash = "";
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA256");
            hmacSHA256.init(secret_key);

            Base64.Encoder encoder64 = Base64.getEncoder();
            hash = encoder64.encodeToString(hmacSHA256.doFinal(strHash.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid Algorithm");
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Secret key");
        }

        return hash;
    }

    /**
     *
     * @param endPoint                                          API endpoint, e.g. /v2/sms/
     * @param responseContentType                               either "application/json" or "application/xml"
     * @param params                                            request parameters
     * @return String                                           response from server
     * @throws MalformedURLException, ProtocolException, IOException, Exception
     */
    public SmsGlobalSendResponse makeGetRequest(String endPoint, String responseContentType, HashMap<String, String> params) throws Exception {
        SmsGlobalSendResponse smsResponse=new SmsGlobalSendResponse();
        smsResponse.setMessageStatus(Boolean.FALSE);
        String response = "";
        String timeStamp = Long.toString(Instant.now().getEpochSecond());
        String nonce = this.generateRandomString(32);

        // process params
        if (!params.isEmpty()) {
            endPoint += "?";
        }
        try {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                endPoint += pair.getKey() + "=" + URLEncoder.encode((String)pair.getValue(), this.charSet.toString()) + "&";
                it.remove(); // avoids a ConcurrentModificationException
            }
            endPoint = endPoint.substring(0, endPoint.length() -1 );            // remove the last & character
        } catch (UnsupportedEncodingException e) {
            // will never be thrown, message is put here for contingency
           // System.out.println("Invalid encoding while trying to process params");
            smsResponse.setMessageResponse("Invalid encoding while trying to process params");
        }
        try {
        String hash = this.generateHash(timeStamp, nonce, "GET", endPoint);
        String authToken = String.format("MAC id=\"%s\", ts=\"%s\", nonce=\"%s\", mac=\"%s\"", this.apiKey, timeStamp, nonce, hash);

        String protocol = "http://";
        if (this.port == 443) {
            protocol = "https://";
        }
        String urlStr = protocol + this.apiURL + endPoint;

        URL url = new URL(urlStr);                                              // this can throw MalformedURLException
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Authorization", authToken);
        conn.setRequestProperty("Accept", responseContentType);
        conn.setRequestMethod("GET");                                           // this can throw ProtocolException
        conn.setUseCaches(false);

        // read response from server
        if (conn.getResponseCode() == 200) {
            Reader reader = new InputStreamReader(conn.getInputStream());       // this can throw IOException
            int ch;
            do {
                ch = reader.read();
                response += (char)ch;
            } while (ch != -1);
            smsResponse.setMessageStatus(Boolean.TRUE);
            smsResponse.setMessageResponse(response);
        } else {

            smsResponse.setMessageResponse("HTTP Error code: " + conn.getResponseCode() + ", HTTP Error message: " + conn.getResponseMessage());
            //throw new Exception("HTTP Error code: " + conn.getResponseCode() + ", HTTP Error message: " + conn.getResponseMessage());
        }
        conn.disconnect();
        } catch (Exception e) {
            smsResponse.setMessageResponse(e.getMessage());
        }
        return smsResponse;
    }

    /**
     *
     * @param endPoint                                          API endpoint, e.g. /v2/sms/
     * @param requestContentType                                either "application/json" or "application/xml"
     * @param responseContentType                               either "application/json" or "application/xml"
     * @param params                                            request parameters
     * @return String                                           response from server
     * @throws MalformedURLException, ProtocolException, IOException, Exception
     */
    public SmsGlobalSendResponse makePostRequest(String recipient,String msg,String endPoint, String requestContentType, String responseContentType, HashMap<String, String> params) throws Exception {
        SmsGlobalSendResponse smsResponse=new SmsGlobalSendResponse();
        smsResponse.setMessageStatus(Boolean.FALSE);
        try {
        String response = "";
        String timeStamp = Long.toString(Instant.now().getEpochSecond());
        String nonce = this.generateRandomString(32);

        String hash = this.generateHash(timeStamp, nonce, "POST", endPoint);
        String authToken = String.format("MAC id=\"%s\", ts=\"%s\", nonce=\"%s\", mac=\"%s\"", this.apiKey, timeStamp, nonce, hash);

        // sample hardcode JSON request, will need to translate from params to JSON or XML string to cover generic request
        String postDataStr = "{" +
                "\"origin\" : \"SMSGlobal\", " +
                "\"destination\" : \""+recipient+"\", " +
                "\"message\" : \""+msg+"\"" +
                "}";
            System.out.println( "postDataStr" + postDataStr);
        String protocol = "http://";
        if (this.port == 443) {
            protocol = "https://";
        }
        String urlStr = protocol + this.apiURL + endPoint;

        URL url = new URL(urlStr);                                              // this can throw MalformedURLException
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Authorization", authToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", responseContentType);
        conn.setRequestMethod("POST");                                           // this can throw ProtocolException
        conn.setUseCaches(false);

        conn.getOutputStream().write(postDataStr.getBytes(this.charSet));

        // read response from server
        if (conn.getResponseCode() == 200) {
            Reader reader = new InputStreamReader(conn.getInputStream());       // this can throw IOException
            int ch;
            do {
                ch = reader.read();
                response += (char)ch;
            } while (ch != -1);
            smsResponse.setMessageStatus(Boolean.TRUE);
            smsResponse.setMessageResponse(response);
        } else {
            // read error
            Reader err = new InputStreamReader(conn.getErrorStream());
            int chr;
            do {
                chr = err.read();
                System.out.print((char)chr);
            } while (chr != -1);
            smsResponse.setMessageResponse("HTTP Error code: " + conn.getResponseCode() + ", HTTP Error message: " + conn.getResponseMessage());
           // throw new Exception("HTTP Error code: " + conn.getResponseCode() + ", HTTP Error message: " + conn.getResponseMessage());
        }
        conn.disconnect();
    } catch (Exception e) {
        smsResponse.setMessageResponse(e.getMessage());
    }

        return smsResponse;
    }

    private String generateRandomString(int length) {
        String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();

        int i = 0;
        while (i < length) {
            int index = (int)(rnd.nextFloat() * validCharacters.length());
            str.append(validCharacters.charAt(index));
            i++;
        }
        return str.toString();
    }

    /**
     * @param responseContentType               define which format is expected from server
     * @return String                           string represents the response from server (can be in JSON or XML format depending on responseContentType)
     */
    public SmsGlobalSendResponse getOutGoingMessages(String responseContentType, HashMap<String, String> params) throws Exception {
        String endPoint = "/v2/sms/";
        return this.makeGetRequest(endPoint, responseContentType, params);
    }

    public SmsGlobalSendResponse sendMessage(String recipient,String msg,String requestContentType, String responseContentType, HashMap<String, String> params) throws Exception {
        String endPoint = "/v2/sms/";
        System.out.println("--------sendSms-- request-------:"+ msg +"," +recipient+"," + requestContentType +"," +responseContentType);
        return this.makePostRequest(recipient,msg,endPoint, requestContentType, responseContentType, params);
    }

//    public String listGroups(String requestContentType, HashMap<String, String> params) throws Exception {
//        String endPoint = "/v2/group";
//        return this.makeGetRequest(endPoint, requestContentType, params);
//    }

//    public static void main(String[] args) {
//        SMSGlobal apiObj = new SMSGlobal();
//       // apiObj.setApiKey("ec55b213be68255fead2273826f19a00", "2e3fb7a3c6921039cbbda04307a009f2");
//        apiObj.setApiKey("204f5a09f926efd995561b9b53767e9d", "911e93437a1856ef5283d232d37c77e6");
//        // get all messages
////        HashMap<String, String> params = new HashMap<String, String>();
////        params.put("limit", "10");
////        try {
////            System.out.println(apiObj.getOutGoingMessages("application/xml", params));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        // send a message
//        try {
//            System.out.println(apiObj.sendMessage("application/json", "application/json", new HashMap<String, String>()));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
