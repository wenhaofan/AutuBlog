package com.autu.search.lucene;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月10日 下午4:02:27
*/
public class Main {
	public static void main(String[] args) {
		LuceneHelper luceneHelper=LuceneHelper.single();
		  
			 //System.out.println(ret);
		  luceneHelper.deleteIndex("id", "1");
     
	}
}
