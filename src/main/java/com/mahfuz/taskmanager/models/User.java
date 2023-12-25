package com.mahfuz.taskmanager.models;

import com.mahfuz.taskmanager.utils.DateProcessing;

public class User {

	private int id;
	private String name;
	private String email;
	private String created_at;
	private String updated_at;
	private int status;

	public User(int id, String name, String email, int status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.created_at = new DateProcessing().getCurTimestamp().toString();
		this.updated_at = "";
		this.status = status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
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

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	// now the User might have a 0, 1, or more than 1 tasks, So we need to make
	// sometihg so that when calling a user we get the tasks as well

	// public ArrayList<Task> getTasks() {
	// 	TaskDAO taskDao = new TaskDAO();
	// 	return taskDao.getTasksByUserId(this.id);
	// }
}
