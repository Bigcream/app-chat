package com.example.appchat.controller;

import org.springframework.http.HttpHeaders;

public class BaseController {
    protected HttpHeaders noCacheHeader;

    public BaseController() {
        this.noCacheHeader = initNoCacheHeader();
    }
    private HttpHeaders initNoCacheHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires","-1");
        return httpHeaders;
    }
}
