package com.jaoafa.MyMaid2.Lib.Discord;

public class FooterEmbed {
	private String text;
	private String icon_url;
	private String proxy_icon_url;

	public FooterEmbed(String text, String icon_url, String proxy_icon_url){
		setText(text);
		setIcon_url(icon_url);
		setProxy_icon_url(proxy_icon_url);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
