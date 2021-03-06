package com.jaoafa.MyMaid2.Lib.Discord;

public class FieldEmbed {
	private String name;
	private String value;
	private boolean inline;
	public FieldEmbed(String name, String value, boolean inline) {
		this.name = name;
		this.value = value;
		this.inline = inline;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean getInline() {
		return inline;
	}
	public void setInline(boolean inline) {
		this.inline = inline;
	}
}
