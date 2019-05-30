package com.autu.common.dto;

/**
 * 保存文件上传后的信息
 * @author Lemon
 *
 */
public class FileUploadInfo {
	private String relativePath;
	private String fileName;
	private String absolutePath;
	/**
	 * 文件的网络路径
	 */
	private String url;
	
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
