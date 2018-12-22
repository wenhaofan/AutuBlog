package com.autu._admin.disk;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.autu.common._config.BlogContext;
import com.autu.common.kit.StrKit;
import com.autu.common.model.entity.Disk;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

public class QiniuFileManager {

	@Inject
	private Disk dao;

	/**
	 *    同步七牛云和项目数据的文件记录
	 * @return
	 */
	public AsyncResultDTO asyncQiniuDisk() {
		CompareDiskResult compareResult=compareQiniuDisk();
		AsyncResultDTO asyncResult=new AsyncResultDTO();
		
		List<Disk> notExistList=compareResult.getNotExistList();
		
		asyncResult.setExistNum(compareResult.getExistList().size());
		asyncResult.setNotExistNum(notExistList.size());
		
		Db.batchSave(notExistList, notExistList.size());
		asyncResult.setOk();
		return asyncResult;
	}
	
	public  CompareDiskResult compareQiniuDisk() {
		List<Disk> dbList = dao.find("select * from disk where type != ? and hash is not null ",
				DiskType.FOLDER.toString());
		List<Disk> qiniuList = listQiNiuDiskInfo();

		List<Disk> existList = new LinkedList<>();
		List<Disk> notExistList = new LinkedList<>();
 
		String qiniuHash = null;
		Disk qiniuDisk = null;
		Disk dbDisk = null;

		
		if(dbList.isEmpty()) {
			notExistList=qiniuList;
		}else {
			for (int qiniuSub = 0, qiniuSize = qiniuList.size(); qiniuSub < qiniuSize; qiniuSub++) {
				qiniuDisk = qiniuList.get(qiniuSub);
				qiniuHash=qiniuDisk.getHash();
				for (int dbSub = 0, dbSize = dbList.size(); dbSub < dbSize; dbSub++) {
					dbDisk = dbList.get(dbSub);
					 
					//如果hash值相等则表示已关联
					if (StrKit.equals(dbDisk.getHash(), qiniuHash)) {
						existList.add(qiniuDisk);
						continue;
					} else if (dbSub + 1 == dbSize) {
						notExistList.add(qiniuDisk);
					}
				}

			}
		}
		
		
		
		CompareDiskResult result=new CompareDiskResult();
		result.setExistList(existList);;
		result.setNotExistList(notExistList);
		
		
		
		return result;
	}

	/**
	 * 获取七牛云中的所有文件
	 * 
	 * @return
	 */
	public List<Disk> listQiNiuDiskInfo() {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		String accessKey = BlogContext.config.getQiniuAk();
		String secretKey = BlogContext.config.getQiniuSk();
		String bucket = BlogContext.config.getQiniuBucket();
		String domain = BlogContext.config.getQiniuUrl();

		Auth auth = Auth.create(accessKey, secretKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 文件名前缀
		String prefix = "";
		// 每次迭代的长度限制
		int limit = 1000;
		// 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
		String delimiter = "";
		// 列举空间文件列表
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit,
				delimiter);
		List<Disk> diskList = new ArrayList<>();

		Disk disk = null;
		String type = null;
		while (fileListIterator.hasNext()) {
			// 处理获取的file list结果
			FileInfo[] items = fileListIterator.next();
			for (FileInfo item : items) {
				disk = new Disk();

				type = getFileType(item.key);

				disk.setName(item.key);
				disk.setSize(item.fsize);
				disk.setType(type);
				disk.setHash(item.hash);
				disk.setUrl(domain + "/" + disk.getName());
				disk.setParentId(0);
				diskList.add(disk);
			}
		}
		return diskList;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param fileName
	 * @return
	 */
	public String getFileType(String fileName) {
		String[] strArr = fileName.split("\\.");
		if (strArr.length == 0) {
			return null;
		}
		return strArr[strArr.length - 1].toLowerCase();
	}
}
 
 
