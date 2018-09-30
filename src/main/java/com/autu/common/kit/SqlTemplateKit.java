package com.autu.common.kit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class SqlTemplateKit {

	/**
	 * 初始化 sql 模板到 ActiveRecord
	 * @param arp ActiveRecordPlugin    
	 * @param basePath  sql 文件根目录
	 * @param suffix 文件后缀
	 */
	public static void init(ActiveRecordPlugin arp, String basePath, String suffix) {
		File file = new File(basePath);
		List<File> files=read(file,".sql");
		
		for (File sqlFile : files) {
			arp.addSqlTemplate(sqlFile.getName());
			//System.out.println(sqlFile);
		}
	
	}

	/**
	 * 获取某个目录下以suffix为结尾的所有文件
	 * @param file 路径文件根目录的文件对象
	 * @param suffix 结尾字符串
	 * @return
	 */
	public static List<File> read(File file,String suffix){
		List<File> fileNames=new ArrayList<>();
		
        if(file.isDirectory()) {
        	File[] files=file.listFiles();
        	for(int i=0,size=files.length;i<size;i++) {
        		fileNames.addAll(read(files[i],suffix));
        	}	
        }else {
        	String fileName=file.getName();
        	if(fileName.endsWith(suffix)) {
        		fileNames.add(file);
        	}
        	return fileNames;
        }
        
        return fileNames;
    }
	
	public static void main(String[] args) {
		String sqlBasePath = PathKit.getRootClassPath();
		init(null,sqlBasePath,".sql");
	}
}