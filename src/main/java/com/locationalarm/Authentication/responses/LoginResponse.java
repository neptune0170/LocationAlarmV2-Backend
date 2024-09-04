package com.locationalarm.Authentication.responses;


public class LoginResponse {
    private String token;

    private long expiresIn;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public LoginResponse setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", expiresIn=" + expiresIn +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}