package com.youbanban.cordova.chooseimages.bean;

import android.graphics.Bitmap;

public class Images {
	private String path;
	private int num;
	private Bitmap bitmap;
	private int isDelete;
	public Images(String path, int num, Bitmap bitmap,int isDelete) {
		super();
		this.path = path;
		this.num = num;
		this.bitmap = bitmap;
		this.isDelete = isDelete;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public Images(String path, int num, Bitmap bitmap) {
		super();
		this.path = path;
		this.num = num;
		this.bitmap = bitmap;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
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
