package com.mahfuz.taskmanager.utils;

public class CustomHttpException extends RuntimeException {
  private final int statusCode;

  public CustomHttpException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
