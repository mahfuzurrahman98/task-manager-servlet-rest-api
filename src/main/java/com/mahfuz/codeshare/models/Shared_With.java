package com.mahfuz.codeshare.models;

public class Shared_With {
	private int source_id;
	private int shared_user_id;

	public Shared_With(int source_id, int shared_user_id) {
		super();
		this.source_id = source_id;
		this.shared_user_id = shared_user_id;
	}

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public int getShared_user_id() {
		return shared_user_id;
	}

	public void setShared_user_id(int shared_user_id) {
		this.shared_user_id = shared_user_id;
	}
}
