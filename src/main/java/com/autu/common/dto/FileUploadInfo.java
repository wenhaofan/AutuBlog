package com.autu.common.dto;

import com.jfinal.kit.Ret;

/**
 * 保存文件上传后的信息
 * @author Lemon
 *
 */
public class FileUploadInfo {
	private String relativePath;
	private String fileName;
	private String absolutePath;
	
	private long size;
	
	private String type;
	
	
	public Ret getUeditorRet() {
		return Ret.create("state", "SUCCESS")
				.set("url", this.getUrl())
				.set("title",this.getFileName())
				.set("original", this.getUrl())
				.set("type", this.getType())
				.set("size", this.getSize());
	}
	
	/**
	 * 文件的网络路径
	 */
	private String url;
	
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
