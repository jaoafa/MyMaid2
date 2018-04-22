package com.jaoafa.MyMaid2.Lib.Discord;

public class ThumbnailEmbed {
	private String url;
	private String proxy_url;
	private int height;
	private int width;

	public ThumbnailEmbed(String url, String proxy_url, int height, int width) {
		setUrl(url);
		setProxy_url(proxy_url);
		setHeight(height);
		setWidth(width);
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProxy_url() {
		return proxy_url;
	}
	public void setProxy_url(String proxy_url) {
		this.proxy_url = proxy_url;
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
