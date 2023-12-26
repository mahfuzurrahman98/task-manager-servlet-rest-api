package com.mahfuz.taskmanager.daos;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO<T> {
  T create(T t) throws SQLException;

  ArrayList<T> all() throws SQLException;

  T get(int id) throws SQLException;

  T update(int id, T t) throws SQLException;

  boolean delete(int id) throws SQLException;
}
