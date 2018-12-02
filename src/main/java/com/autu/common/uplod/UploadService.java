

package com.autu.common.uplod;

import java.io.File;
import java.util.Date;

import com.autu.common.kit.DateKit;
import com.autu.common.kit.QiniuFileUtils;
import com.autu.common.model.dto.FileUploadInfo;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

/**
 * 上传业务
 * 1：不同模块分别保存到不同子目录
 * 2：每个目录下文件数达到 5000 时创建新的子目录，upload_counter 用于记录每个模块文件上传总数
 *    用于创建子目录
 */
public class UploadService {

	public static final UploadService me = new UploadService();

	/**
	 * 上传图片允许的最大尺寸，目前只允许 200KB
	 */
	public static final int imageMaxSize = 200 * 1024;


	/**
	 * 相对于 webRootPath 之后的目录，与"/upload" 是与 baseUploadPath 重合的部分
	 */
	private static final String basePath = "/upload/";

	/**
	 * 上传文件暂存目录
	 */
	public static final String tempPath="/upload/temp";
	
	/**
	 * 每个子目录允许存 5000 个文件
 	 */
	public static final int FILES_PER_SUB_DIR = 5000;

	
	public FileUploadInfo fileUpload(String uploadType, UploadFile uf) {
		String[] arr=uf.getFileName().split("\\.");
		String[] pathAndFileName=buildPathAndFileName(basePath,uploadType,arr[1]);
		String absolutePath= PathKit.getWebRootPath()+pathAndFileName[0];
		String fileName=pathAndFileName[1];
		saveOriginalFileToTargetFile(uf.getFile(),absolutePath,fileName);
		FileUploadInfo info=new FileUploadInfo();
		info.setAbsolutePath(absolutePath);
		info.setRelativePath(pathAndFileName[0]);
		info.setFileName(pathAndFileName[1]);
		
		Ret ret=QiniuFileUtils.uploadFile(absolutePath, fileName, fileName);
	
		String fileUrl=null;
		
		//如果七牛云上传失败则返回服务器资源路径
		if(ret.isFail()){
			fileUrl=basePath+uploadType+"/"+arr[1]+"/"+fileName;
		}else {
			fileUrl=ret.getStr("url");
		}
		info.setUrl(fileUrl);
		return info;
	}
	
	/**
	 * 生成规范的文件名
	 * accountId_年月日时分秒.jpg
	 * 包含 accountId 以便于找到某人上传的图片，便于定位该用户所有文章，方便清除恶意上传
	 * 图片中添加一些 meta 信息：accountId_201604161359.jpg
	 * 目录中已经包含了模块名了，这里的 meta 只需要体现 accountId 与时间就可以了
	 */
	private String generateFileName(Integer accountId, String extName) {
		return accountId + "_" + DateKit.format(new Date(),"yyyyMMddHHmmss" )+"."+ extName;
	}

	/**
	 * 根据上传类型生成完整的文件保存路径
	 * @param uploadType 上传类型
	 */
	private String[] buildPathAndFileName(String basePath,
			String uploadType,
			String extName) {

		String	relativePath = basePath + uploadType+"/"+extName;

		String fileName = generateFileName(520520, extName);
	
		return new String[] {relativePath,fileName};
	}

	private void saveOriginalFileToTargetFile(File originalFile, String targetFilePath,String targetFileName) {
		File file=new File(targetFilePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		saveOriginalFileToTargetFile(originalFile, targetFilePath+"/"+targetFileName);
	}

	/**
	 * 目前使用 File.renameTo(targetFileName) 的方式保存到目标文件，
	 * 如果 linux 下不支持，或者将来在 linux 下要跨磁盘保存，则需要
	 * 改成 copy 文件内容的方式并删除原来文件的方式来保存
	 */
	private void saveOriginalFileToTargetFile(File originalFile, String targetFile) {
		originalFile.renameTo(new File(targetFile));
	}


}
