package com.autu._admin.common.upload;

import com.autu.common.controller.BaseController;
import com.autu.common.dto.FileUploadInfo;
import com.autu.common.uplod.UploadService;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

public class AdminEditormdUploadApi extends BaseController {

	private UploadService service=UploadService.me;
	
	public void index() {
		String uploadType="article";
		 
		try {
			UploadFile	uf = getFile("editormd-image-file", UploadService.tempPath);
			FileUploadInfo info=service.fileUpload(uploadType, uf);
			renderJson(Ret.create("success", 1)
					.set("message", "上传成功")
					.set("url", info.getUrl()));
		} catch (Exception e) {
			e.printStackTrace();
			
			renderJson(Ret.create("success", 0).set("message", "系统繁忙"));
		}
 
	 
	}
	
}
