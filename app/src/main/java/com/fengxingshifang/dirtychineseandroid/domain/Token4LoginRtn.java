package com.fengxingshifang.dirtychineseandroid.domain;

/**
 * Created by git on 2018/1/6.
 */

public class Token4LoginRtn {
    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE1MjA1NjI0LCJleHAiOjE1MTUyMDkyMjQsIm5iZiI6MTUxNTIwNTYyNCwianRpIjoiam5TTUgyN0FlQnlBWFdUVCIsInN1YiI6NjUsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEifQ.9uFzGirJJ12PhBKLyVe_plLgDcacQfdBR6B-oK1DgDg
     * status_code : 200
     * message : User Authenticated
     */

    private String token;
    private String status_code;
    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
