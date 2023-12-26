package com.mahfuz.taskmanager.models;

public class Task {
  private int id;
  private String title;
  private String description;
  private String created_at;
  private String updated_at;
  private int status;
  private int user_id;

  public Task(String title, String description, int user_id) {
    // this.id = id;
    this.title = title;
    this.description = description;
    this.user_id = user_id;
    this.status = 1;
    this.created_at = "";
    this.updated_at = "";
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setUserId(int user_id) {
    this.user_id = user_id;
  }

  public int getUserId() {
    return user_id;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setCreatedAt(String created_at) {
    this.created_at = created_at;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public void setUpdatedAt(String updated_at) {
    this.updated_at = updated_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }
}
