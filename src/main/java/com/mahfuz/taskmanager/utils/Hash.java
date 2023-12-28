package com.mahfuz.taskmanager.utils;

public class Hash {
  public static String make(String password) {
    return "123" + new StringBuilder(password).reverse().toString() + "xyz";
  }

  public static boolean verify(String password, String hashedPassword) {
    String reversed = new StringBuilder(hashedPassword).reverse().toString();
    String original = reversed.substring(3, reversed.length() - 3);
    return original.equals(password);
  }
}
