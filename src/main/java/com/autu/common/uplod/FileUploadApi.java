package com.autu.common.uplod;


import com.autu.common.controller.BaseController;
import com.autu.common.model.dto.FileUploadInfo;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;


/**
 * 文件上传控制器
 * @author fwh
 *
 *
 */
public class FileUploadApi extends BaseController {

	private UploadService service=UploadService.me;

	/**
	 * 文件上传处理
	 */
	public void index() {
		String uploadType=getPara(0);
		UploadFile	uf = getFile("upfile", UploadService.tempPath);
		FileUploadInfo info=service.fileUpload(uploadType, uf);
		//隐藏绝对路径
		info.setAbsolutePath("");
		renderJson(Ret.ok("info", info));
	}
}



