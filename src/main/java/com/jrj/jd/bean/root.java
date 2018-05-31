package com.jrj.jd.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

//XML文件中的根标识
@XmlRootElement(name = "root")
public class root implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private news news;

	public news getNews() {
		return news;
	}

	public void setNews(news news) {
		this.news = news;
	}

}