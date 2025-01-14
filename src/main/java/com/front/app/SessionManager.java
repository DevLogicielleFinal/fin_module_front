package com.front.app;

public class SessionManager {
    private static String jwtToken;

    public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String token) {
        jwtToken = token;
    }
}
