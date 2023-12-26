package com.mahfuz.taskmanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JSON {
  public static HashMap<String, Object> read(HttpServletRequest request) {
    HashMap<String, Object> jsonData = null;
    try {
      BufferedReader reader = request.getReader();
      String jsonString = reader.lines().collect(Collectors.joining());
      System.out.println(jsonString);
      // now make the json string to hashmap and return it
      jsonString = jsonString.replace("\\", "");
      System.out.println(jsonString);
      Gson gson = new Gson();
      // jsonData = gson.fromJson(jsonString, HashMap.class);
      jsonData = gson.fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
      }.getType());

      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jsonData;
  }

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
