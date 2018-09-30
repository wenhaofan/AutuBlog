package com.autu.common.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月13日 下午3:59:07
*/
public class PropertyKit {
 
	public static void main(String[] args) {
		updateValue("blog_config.txt","theme","1");
	}
	
	public static void updateValue(String fileName,String key,String newValue) {
		String path=FileKit.class.getResource("/"+fileName).getPath() ;

		update(path, key, newValue);
	}
	
	public static void update(String path,String key,String newValue) {
	       String temp = "";
	       File file = new File(path);
	        try (
	        	    FileInputStream fis = new FileInputStream(file);
		            InputStreamReader isr = new InputStreamReader(fis);
	        		BufferedReader br = new BufferedReader(isr); 
	        		 FileOutputStream fos = new FileOutputStream(file);
		            PrintWriter pw = new PrintWriter(fos);
	        	){
	        	StringBuffer buf = new StringBuffer();
	            // 保存该行前面的内容
	            while ( (temp = br.readLine()) != null) {
	            	
	            	boolean isMath=StrKit.filterNull(temp).split("=")[0].equals(key);
	            	
	                if(isMath){
	                    buf = buf.append(key+"="+newValue);
	                }else{
	                    buf = buf.append(temp);
	                }
	                buf = buf.append(System.getProperty("line.separator"));
	            }
	            pw.write(buf.toString().toCharArray());
	            pw.flush();     
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
