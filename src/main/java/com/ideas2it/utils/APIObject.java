package com.ideas2it.utils;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

public class APIObject {
    public HttpHeaders responseHTTPheaders;
    public String requestName;
    public JSONObject responseBodyAsJson;
    public int responseCode;
    public long responseTime;
    public byte[] bodyAsByteArray;
    public Headers responseHeaders;
    public byte[] responseBodyAsByteArray;
    public String requestUrl;
    public Map<String, String> requestHeaders;
    public String requestBody;
    public String methodType;
    public Map<String, String> requestParams;

    public APIObject(RequestObject requestObject) {
        this.requestBody = requestObject.requestBody;
        this.requestHeaders = requestObject.requestHeaders;
        this.methodType = requestObject.methodType;
        this.requestUrl = requestObject.requestUrl;
        this.requestName = requestObject.requestName;
    }

    public APIObject(Response response) {
        this.responseBodyAsJson = getResponseBody(response);
        this.responseCode = response.statusCode();
        this.responseTime = response.time();
        this.bodyAsByteArray = response.then().extract().asByteArray();
        this.responseHeaders = response.headers();
        this.responseBodyAsByteArray = response.then().extract().asByteArray();
    }

    public APIObject(ResponseEntity<String> response){
        this.responseBodyAsJson = getResponseBody(response);
        this.responseCode = response.getStatusCode().value();
        this.bodyAsByteArray = Objects.requireNonNull(response.getBody()).getBytes();
        this.responseHTTPheaders = response.getHeaders();
    }

    public APIObject(String url, RequestSpecificationImpl request, Response response) {
        this.requestUrl = url;
        this.requestBody = request.getBody() != null? request.getBody().toString(): "";
        this.methodType = request.getMethod();
        this.responseBodyAsJson = getResponseBody(response);
        this.responseCode = response.statusCode();
        this.responseTime = response.time();
        this.bodyAsByteArray = response.then().extract().asByteArray();
        this.responseHeaders = response.headers();
        this.responseBodyAsByteArray = response.then().extract().asByteArray();

        this.requestHeaders = getMapFromHeaders(request.getHeaders());
    }

    public APIObject(String url, HttpEntity<String> request, ResponseEntity<String> response) {
        this.requestUrl = url;
        this.requestBody = request.getBody();
        this.responseCode = response.getStatusCode().value();
        this.responseHTTPheaders = response.getHeaders();
        if(response.getBody()!=null){
            this.responseBodyAsJson = getResponseBody(response);
            this.bodyAsByteArray = response.getBody().getBytes();
        } else {
            this.responseBodyAsJson = null;
            this.bodyAsByteArray = null;
        }
    }

    public APIObject(String url, List<Header> headers, String body, String methodType) {
        this(url, headers, body, methodType, new HashMap<String, String>());
    }

    public APIObject(String url, List<Header> headers, String body, String methodType, Map<String, String> requestParams) {
        this.requestUrl = url;
        this.requestHeaders = new HashMap<>();
        for (Header header : headers) {
            this.requestHeaders.put(header.getName(), header.getValue());
        }
        this.requestBody = body;
        this.methodType = methodType;
        this.requestParams = requestParams;
    }

    public APIObject(HttpRequest httpRequest, String requestName){
        this.requestUrl = httpRequest.uri().toString();
        this.requestBody = httpRequest.bodyPublisher().isPresent() ? httpRequest.bodyPublisher().get().toString(): null;

        Map<String, String> map = new HashMap<>();
        java.net.http.HttpHeaders headers = httpRequest.headers();
        for (Map.Entry<String, List<String>> header : headers.map().entrySet()){
            if(header.getValue() != null && !header.getValue().isEmpty())
                map.put(header.getKey(), header.getValue().get(0));
        }
        this.requestHeaders = map;
        this.requestName = requestName;
        this.methodType = httpRequest.method();
    }

    public String getResponseHeader(String headerName) {
        return this.responseHeaders.get(headerName).toString();
    }

    public HttpHeaders getResponseHTTPHeaders() {
        return this.responseHTTPheaders;
    }

    public JSONObject getResponseBody(Response response) {
        return getJsonObject(response.asString());
    }

    public JSONObject getResponseBody(ResponseEntity response) {
        return getJsonObject(response.getBody().toString());
    }

    public static JSONObject getJsonObject(String responseBody) {
        if (responseBody.equals("")) {
            responseBody = "{}";
        } else if (responseBody.charAt(0) == '[') {
            return new JSONObject().put("responseArray", new JSONArray(responseBody));
        } else if (responseBody.charAt(0) == '<') {
            return new JSONObject().put("responseError", responseBody);
        } else if (responseBody.equals("true") || responseBody.equals("false")) {
            return new JSONObject().put("responseBoolean", responseBody);
        } else if (responseBody.trim().startsWith("http")) {
            return new JSONObject().put("responseURL", responseBody);
        } else if ((responseBody.trim().startsWith("\"")) || (!responseBody.contains("{"))) {
            return new JSONObject().put("responseString", responseBody);
        }

        return new JSONObject(responseBody);
    }

    private Map<String, String> getMapFromHeaders(Headers headers){
        Map<String, String> mapHeaders = new HashMap<>();
        try {
            headers.iterator().forEachRemaining(h -> {
                mapHeaders.put(h.getName(),h.getValue());
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return mapHeaders;
    }
}
