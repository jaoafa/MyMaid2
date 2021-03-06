package com.jaoafa.MyMaid2.Lib.Discord;

public class AuthorEmbed {
	private String name;
	private String url;
	private String icon_url;
	private String proxy_icon_url;
	public AuthorEmbed(String name, String url, String icon_url, String proxy_icon_url) {
		setName(name);
		setUrl(url);
		setIcon_url(icon_url);
		setProxy_icon_url(proxy_icon_url);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getProxy_icon_url() {
		return proxy_icon_url;
	}
	public void setProxy_icon_url(String proxy_icon_url) {
		this.proxy_icon_url = proxy_icon_url;
	}

}
