package com.autu._admin.disk;

public enum DiskType {
	/**文件夹*/
	FOLDER,
	/**图片*/
	PNG,
	JPG,
	/***/
	ZIP,
	/***/
	TXT,
	/***/
	HTML,
	/***/
	JAVA,
	/***/
	MP4,
	/***/
	MP3,
	/***/
	AVI,
	/***/
	CODE,
	/***/
	BT,
	/***/
	DEFAULT;
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
	
	
}
