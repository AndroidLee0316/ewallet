package com.pasc.business.ewallet.picture.pictureSelect.bean;

import com.pasc.business.ewallet.picture.pictureSelect.LocalPicture;

import java.io.Serializable;
import java.util.List;


/**
 * 一个目录下的相册对象
 */
public class PhotoUpImageBucket implements Serializable {
	
	public int count = 0;
	public String bucketName;
	public List<LocalPicture> imageList;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public List<LocalPicture> getImageList() {
		return imageList;
	}
	public void setImageList(List<LocalPicture> imageList) {
		this.imageList = imageList;
	}
	
}
