package com.youbanban.cordova.chooseimages.bean;

public class Images {
	
	private String path;
	private int num;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Images(String path, int num) {
		super();
		this.path = path;
		this.num = num;
	}
	

}
