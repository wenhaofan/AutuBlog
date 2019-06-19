package com.autu._admin.page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.autu.common._config.BlogConfig;
import com.autu.common.exception.MsgException;
import com.autu.common.model.Article;
import com.autu.common.model.Page;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;

public class AdminPageService {

	private static Page pageDao=new Page().dao();
	private static Article articleDao=new Article().dao();
	

	
	public com.jfinal.plugin.activerecord.Page<Page> page(Integer pageNum,Integer pageSize,Page query){
		SqlPara sqlPara=Db.getSqlPara("page.page", Kv.by("query", query));
		return pageDao.paginate(pageNum, pageSize, sqlPara);
	}
	
	public boolean add(Page page) {
		
		String fileName = genRandomFileName(page);
 
		String relativePath=BlogConfig.PAGE_DIY_PATH+fileName;;
		
		page.setPath(relativePath);
		
		savePageContentToFile(page);
		
		return page.save();
	}

	private String genRandomFileName(Page page) {
		String fileName=page.getName()+"["+System.currentTimeMillis()+"].tpl";
		return fileName;
	}
	
	public boolean addOrUpdate(Page page) {
		if(page.getId()==null) {
			return add(page);
		}else {
			return update(page);
		}
	}
	
	public boolean update(Page page) {
		
		if(StrKit.isBlank(page.getPath())) {
			String fileName = genRandomFileName(page);
			 
			String relativePath=BlogConfig.PAGE_DIY_PATH+fileName;;
			
			page.setPath(relativePath);
		}
		
		savePageContentToFile(page);

		return page.update();
	}
	

	private void savePageContentToFile(Page page) {
		String targetPath=getAbsoluteSavePath(page);
		
		File targetFile=new File(targetPath);
	 
		try {
			targetFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFile));) {
			
			bufferedWriter.write(page.getContent());
			bufferedWriter.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
	}

	private String getAbsoluteSavePath(Page page) {
		return PathKit.getWebRootPath()+page.getPath();
	}
	
	private String getPageContent(Page page) {
		
		String targetFile=getAbsoluteSavePath(page);
		StringBuilder sb=new StringBuilder();
		try (BufferedReader br=new BufferedReader(new FileReader(targetFile))){
			
			String str;
			while ((str=br.readLine())!=null) {
				 sb.append(str+'\n');
				 
			}
		 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public boolean delete(Integer id) {
		Page Page=pageDao.findById(id);
		if(Page==null) {
			throw new MsgException("页面不存在");
		}
		return Page.setIsDeleted(true).update();
	}
	
	public boolean bindArticle(Integer articleId,Integer routeId) {
		
		Page Page=pageDao.findById(routeId);
		if(Page==null) {
			throw new MsgException("该页面不存在");
		}
		
		Article article=articleDao.findById(articleId);
		
		if(article==null) {
			throw new MsgException("该文章不存在");
		}
		
		Page.setArticleId(articleId);
		
		return Page.update();
	}
	
	public Page get(Integer id) {
		Page page= pageDao.findById(id);
		if(page==null) {
			return page;
		}
		
		String content=getPageContent(page);
		content=content.replaceAll("<", "&lt;");
		content=content.replaceAll(">", "&gt;");
		page.setContent(content);
		return page;
	}
	
	public String getContent(Integer id) {
		Page page= pageDao.findById(id);
		return getPageContent(page);
	}
	
}
