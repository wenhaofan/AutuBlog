package com.autu.common.uplod;


import com.autu.common.controller.BaseController;
import com.autu.common.dto.FileUploadInfo;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;


/**
 * 文件上传控制器
 * @author fwh
 *
 *
 */
public class UploadApi extends BaseController {

	private UploadService service=UploadService.me;

	/**
	 * 文件上传处理
	 */
	public void index() {
		
		if(notLogin()) {
			renderJson(Ret.fail());
		}
		
		String uploadType=getPara(0);
		UploadFile	uf = getFile("upfile", UploadService.tempPath);
		FileUploadInfo info=service.fileUpload(uploadType, uf);
		//隐藏绝对路径
		info.setAbsolutePath("");
		renderJson(Ret.ok("info", info));
	}
	
	
	
	/**
	 * 接管 ueditor 上传图片服务端
	 *
	 * 1：该 action 与 ueditor.config.js 中的 serverUrl: "/upload/ueditor" 对应
	 *
	 * 2：ueditor 页面加载时会向后端发送 "/xxx?action=config 请求用来获取服务端
	 *    /ueditor-home/jsp/config.json 中的配置，后续的上传将受该配置的影响
	 *
	 * 3：ueditor1_4_3_2-utf8-jsp 版本测试上传图片成功所返回的 json 数据格式如下：
	 *  {
	 *     "state": "SUCCESS",
	 *     "title": "1461249851191086496.png",
	 *     "original": "qr.png",
	 *     "type": ".png",
	 *     "url": "/ueditor/jsp/upload/image/20160421/1461249851191086496.png",
	 *     "size": "58640"
	 *  }
	 *
	 * 4：如果上传出现错误，直接响应如下的 json 即可：
	 *    {"state": "错误信息"}
	 *
	 */
	public void ueditor() {
		/**
		 * ueditor 在页面加载时会向后端请求获取 config.json 内容
		 */
		if ("config".equals(getPara("action"))) {
			notTheme();
			render("/assets/plugins/ueditor/jsp/config.json");
			return;
		}
		/**
		 * 对应 config.json 配置的 imageActionName: "uploadimage"
		 */
		if ( ! "uploadimage".equals(getPara("action"))) {
			renderJson("state", "UploadController 只支持图片类型的文件上传");
			return ;
		}
		/**
		 * uploadType 是通过如代码令 ueditor 在上传图片时通过问号挂参的方式传递过来的自定义参数
		 * ue.ready(function() {
		 *      ue.execCommand('serverparam', {
		 *          'uploadType': 'project'
		 *      });
		 * });
		 */
		String uploadType = getPara("uploadType");
		if (StrKit.isBlank(uploadType)) {
			renderJson("state", "上传类型参数缺失");
			return ;
		}

		if (notLogin()) {
			renderJson("state", "只有登录用户才可以上传文件");
			return ;
		}

		UploadFile uploadFile = null;
		try {
			// "upfile" 来自 config.json 中的 imageFieldName 配置项
			uploadFile = getFile("upfile", UploadService.tempPath, UploadService.imageMaxSize);
			FileUploadInfo info = service.fileUpload(uploadType, uploadFile);
			//隐藏绝对路径
			info.setAbsolutePath("");
			
			 
			renderJson(info.getUeditorRet());
 
		}
		catch(com.jfinal.upload.ExceededSizeException ex) {
			renderJson("state", "上传图片只允许 200K 大小");
		}
		catch(Exception e) {
			if (uploadFile != null) {
				uploadFile.getFile().delete();
			}
			
			renderJson("state", "上传图片出现未知异常，请告知管理员：" + e.getMessage());
			LogKit.error(e.getMessage(), e);
		}
	}

	
}



