package com.mahfuz.codeshare.models;

public class SourceCode {

	private int Id;
	private String title;
	private String language;
	private String source_code;
	private int visibility;
	private int created_by;
	private String created_by_name;
	private String created_by_alt;
	private int[] shared_persons;
	private String created_at;
	private String expire_at;
	private int status;

	public SourceCode(int id, String title, String language, String source_code, int visibility, int created_by,
			String created_by_name, String created_by_alt, int[] shared_persons, String created_at, String expire_at,
			int status) {
		super();
		Id = id;
		this.title = title;
		this.language = language;
		this.source_code = source_code;
		this.visibility = visibility;
		this.created_by = created_by;
		this.created_by_name = created_by_name;
		this.created_by_alt = created_by_alt;
		this.shared_persons = shared_persons;
		this.created_at = created_at;
		this.expire_at = expire_at;
		this.status = status;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource_code() {
		return source_code;
	}

	public void setSource_code(String source_code) {
		this.source_code = source_code;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public String getCreated_by_name() {
		return created_by_name;
	}

	public void setCreated_by_name(String created_by_name) {
		this.created_by_name = created_by_name;
	}

	public String getCreated_by_alt() {
		return created_by_alt;
	}

	public void setCreated_by_alt(String created_by_alt) {
		this.created_by_alt = created_by_alt;
	}

	public int[] getShared_persons() {
		return shared_persons;
	}

	public void setShared_persons(int[] shared_persons) {
		this.shared_persons = shared_persons;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getExpire_at() {
		return expire_at;
	}

	public void setExpire_at(String expire_at) {
		this.expire_at = expire_at;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
