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
 * @createDate 创建时间：2018年9月13日 下午2:52:50
 */
public class FileKit {
 
	
	public static  void main(String[] args) {
		String path=FileKit.class.getResource("/blog_config.txt").getPath() ;
		path=path.substring(1, path.length());
		replaceLine(path,1,"#数据库信息2");
		  
	}

	public static void replaceLine(String path,Integer num,String newStr ) {
	       String temp = "";
	       FileInputStream fis =null;
	       InputStreamReader isr =null;
	       BufferedReader br =null;
	       PrintWriter pw =null;
	        try {
	            File file = new File(path);
	            fis = new FileInputStream(file);
	            isr= new InputStreamReader(fis);
	            br=new BufferedReader(isr);
	            StringBuffer buf =new StringBuffer();
	            
	            // 保存该行前面的内容
	            int j = 1;
	            while ( (temp = br.readLine()) != null) {
	                if(j==num){
	                    buf = buf.append(newStr);
	                }else{
	                    buf = buf.append(temp);
	                }
	                buf = buf.append(System.getProperty("line.separator"));
	                j++;
	            }

	            br.close();
	            
	            FileOutputStream fos = new FileOutputStream(file);
	            pw=new PrintWriter(fos);
	            pw.write(buf.toString().toCharArray());
	            pw.flush();
	        
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	     
				if(pw!=null) {
					pw.close();
				}
				if(isr!=null) {
					try {
						isr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(br!=null) {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fis!=null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	    }
 
	
	
	 
}
