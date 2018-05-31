package com.jrj.jd.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author JRJ
 *
 */
@Data
public class news implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String iiid;
	private String title;
	private String makedate;
	private String cls;
	private String stock;
	private String infourl;
	private String infosource;
	private String keyword;
	private String dutyname;
	private String author;
	private String inputdate;
	private String detail;
	private String infodesc;
	private String content;

	@Override
	public String toString() {
		return "news [iiid=" + iiid + ", title=" + title + ", makedate="
				+ makedate + ", cls=" + cls + ", stock=" + stock + ", infourl="
				+ infourl + ", infosource=" + infosource + ", keyword="
				+ keyword + ", dutyname=" + dutyname + ", author=" + author
				+ ", inputdate=" + inputdate + ", detail=" + detail
				+ ", infodesc=" + infodesc + ", content=" + content + "]";
	}
}