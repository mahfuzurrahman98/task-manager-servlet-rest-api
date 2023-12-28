package com.mahfuz.taskmanager.models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mahfuz.taskmanager.daos.TaskDAO;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private String created_at;
	private String updated_at;
	private int status;

	public User(String name, String email) {
		this(name, email, "");
	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.created_at = "";
		this.updated_at = "";
		this.status = 1;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
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

	public ArrayList<Task> getTasks() throws IOException {
		TaskDAO taskDao = new TaskDAO();
		ArrayList<Task> tasks = null;
		try {
			tasks = taskDao.getByUserId(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
}
