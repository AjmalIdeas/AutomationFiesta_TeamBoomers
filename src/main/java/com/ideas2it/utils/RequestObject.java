package com.ideas2it.utils;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestObject {
    public String requestName; // Used in Performance tests
    public String requestUrl;
    public Map<String, String> requestHeaders;
    public String requestBody;
    public String methodType;
    public HttpRequest httpRequest;

    public RequestObject(HttpRequest httpRequest, String requestName, String requestBody){
        this.httpRequest = httpRequest;
        this.requestUrl = httpRequest.uri().toString();
        this.requestBody = requestBody;

        Map<String, String> headersMap = new HashMap<>();
        java.net.http.HttpHeaders headers = httpRequest.headers();
        for (Map.Entry<String, List<String>> header : headers.map().entrySet()){
            if(header.getValue() != null && !header.getValue().isEmpty())
                headersMap.put(header.getKey(), header.getValue().get(0));
        }

        this.requestHeaders = headersMap;
        this.requestName = requestName;
        this.methodType = httpRequest.method();
    }
}
