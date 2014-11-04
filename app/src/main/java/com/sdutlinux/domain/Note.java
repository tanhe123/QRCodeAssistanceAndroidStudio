package com.sdutlinux.domain;

public class Note {
	public String content;
	public int id;
	public String date;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Note(int id, String content, String date) {
		this.content = content;
		this.id = id;
		this.date = date;
	}

	public Note(String content, String date) {
		this.content = content;
		this.date = date;
	}

		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		return "id: " + id + " content: " + content + " time: " + date;
	}
}
