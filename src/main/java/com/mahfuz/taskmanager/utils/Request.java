package com.mahfuz.taskmanager.utils;

import java.util.Arrays;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;

public class Request {
    public static String[] getUriParams(HttpServletRequest request, int expectedLength, String baseURL) {
        try {
            String[] uriParams = null;
            String requestURI = request.getRequestURI();
            String urlAfterBaseURL = requestURI.substring(baseURL.length());
            uriParams = urlAfterBaseURL.split("/");

            if (uriParams.length != expectedLength + 1) {
                throw new CustomHttpException(400, "The number of URI parameters is not correct");
            }
            // Remove the first element which is empty
            uriParams = Arrays.copyOfRange(uriParams, 1, uriParams.length);

            return uriParams;
        } catch (CustomHttpException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static HashMap<String, String> getQueryParams(HttpServletRequest request) {
        try {
            HashMap<String, String> queryParams = new HashMap<>();

            String queryString = request.getQueryString();
            String[] queryParamPairs = queryString.split("&");

            for (String queryParamPair : queryParamPairs) {
                String[] queryParam = queryParamPair.split("=");
                queryParams.put(queryParam[0], queryParam[1]);
            }
            return queryParams;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
