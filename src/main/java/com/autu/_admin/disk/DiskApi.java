package com.autu._admin.disk;

import java.util.List;

import com.autu.common._config.BlogContext;
import com.autu.common.controller.BaseController;
import com.autu.common.model.Disk;
import com.autu.common.model.QueryDisk;
import com.autu.common.uplod.UploadService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

public class DiskApi extends BaseController{

	@Inject
	private DiskService service;
	
	@Inject
	private QiniuFileManager qiniuFileManager;
	
	public void get() {
		Disk disk=service.get(getParaToInt());
		if(disk.getUrl().startsWith("/upload")){
			String projectPath=BlogContext.getProjectPath().endsWith("/")?BlogContext.getProjectPath().substring(0, BlogContext.getProjectPath().length()-1):BlogContext.getProjectPath();
			
			disk.setUrl(projectPath+disk.getUrl());
		}
		renderJson(Ret.ok("disk",disk));
	}
	
	public void list() {
		QueryDisk query=getBean(QueryDisk.class,"",true);
		List<Disk> diskList=service.list(query);
		renderJson(Ret.ok("list", diskList));
	}
	
 
	public void upload() {
		UploadFile	uploadFile = getFile("upfile", UploadService.tempPath);
		Integer parentId=getParaToInt("parentId",0);
		Disk disk=service.upload(uploadFile, parentId);
		renderJson(Ret.ok("disk", disk));
	}
	
 
	public void createFolder() {
		Disk disk=getModel(Disk.class,"",true);
		disk.setType(DiskType.FOLDER.toString());
		service.createFolder(disk);
		renderJson(Ret.ok("disk",disk));
	}
	
 
	public void remove() {
		service.remove(getParaToInt());
		renderJson(Ret.ok());
	}
	
 
	public void update() {
		Disk disk=getModel(Disk.class,"",true);
		renderJson(Ret.ok("disk", service.update(disk)	));
 	}
	
	public void listFolderChain() {
		Integer folderId=getParaToInt();
		renderJson(Ret.ok("diskList", service.listFolderChain(folderId)));
	}
	
	public void asyncQiniu() {
		AsyncResultDTO  result=	qiniuFileManager.asyncQiniuDisk();
		renderJson(result.IsOk()?Ret.ok("result", result):Ret.fail("result", result));
	}
}
