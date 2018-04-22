package com.jaoafa.MyMaid2.Lib.Discord;

public class VideoEmbed {
	private String url;
	private int height;
	private int width;

	public VideoEmbed(String url, int height, int width) {
		setUrl(url);
		setHeight(height);
		setWidth(width);
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}
