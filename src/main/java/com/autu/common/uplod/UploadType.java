package com.autu.common.uplod;

public enum UploadType {
	/**文章图片*/
	ARTICLE_IMG,
	/**缩略图*/
	THUMB_IMG;
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
