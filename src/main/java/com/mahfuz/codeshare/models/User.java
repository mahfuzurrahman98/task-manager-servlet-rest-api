package com.mahfuz.codeshare.models;

public class User {

	private int Id;
	private String name;
	private String username;
	private String email;
	private String created_at;
	private String updated_at;
	private int status;

	public User(int id, String name, String username, String email, String created_at, String updated_at, int status) {
		super();
		Id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.status = status;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.name = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
