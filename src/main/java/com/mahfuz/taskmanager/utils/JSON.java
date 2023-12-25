package com.mahfuz.taskmanager.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

public class JSON {
  public static void respond(HttpServletResponse response, int status, String message)
      throws IOException {
    respond(response, status, message, null); // Call the overloaded method with data set to null
  }

  public static void respond(
      HttpServletResponse response,
      int status,
      String message, Object data)
      throws IOException {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("message", message);
    if (data != null) {
      responseMap.put("data", data);
    }

    Gson gson = new Gson();
    String jsonResponse = gson.toJson(responseMap);

    response.setStatus(status);
    response.getWriter().write(jsonResponse);
  }
}
