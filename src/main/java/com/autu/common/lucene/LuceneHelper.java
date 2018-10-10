package com.autu.common.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.autu.common.model.entity.Article;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author 作者:范文皓
 * @createDate 创建时间：2018年9月10日 下午6:18:27
 */
public class LuceneHelper {

	public static String indexDir = PathKit.getWebRootPath() + "/lucene";
	//索引目录
	private static Directory directory;
	//分词器
	private static IKAnalyzer iKAnalyzer;
	//多个线程使用同一个indexWriter，避免锁冲突
	private static IndexWriter indexWriter;
	
	//使用读写锁保证索引操作的线程安全
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private WriteLock writeLock = rwl.writeLock();
	private ReadLock readLock = rwl.readLock();

	static class SingleHolder{
		static LuceneHelper single=new LuceneHelper();
		 
	}
	
	public static LuceneHelper single() {
		 return SingleHolder.single;
	}
 
	private LuceneHelper() {
		try {
			directory = FSDirectory.open(Paths.get(indexDir));
			iKAnalyzer = new IKAnalyzer(true);
			IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());
			indexWriter= new IndexWriter(directory, config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 获取索引存放目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Directory getDirectory() {
		return directory;
	}

	/**
	 * 获取分词器
	 * 
	 * @return
	 */
	public static Analyzer getAnalyzer() {
		return iKAnalyzer;
	}

	/**
	 * 获取操作索引的流
	 * 
	 * @return
	 */
	public  IndexWriter getIndexWriter() {
		return indexWriter;
	}

	/**
	 * 创建索引
	 * 
	 * @param id
	 * @param content
	 * @throws IOException
	 */
	public long createIndexs(List<Document> documents) {
		writeLock.lock();
		IndexWriter writer = getIndexWriter();
		try {
			 writer.addDocuments(documents);
			return writer.commit();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * 创建索引
	 * 
	 * @param id
	 * @param content
	 * @throws IOException
	 */
	public long createIndex(Document document) {

		// 将document放到documents集合中

		writeLock.lock();

 
		try  {
			IndexWriter writer = getIndexWriter();
			writer.addDocument(document);
			return writer.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 
	 * @param queryStr 查询语句
	 * @param size     查询数
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */

	public Page<Article> readerIndex(String queryStr, Integer pageNum, Integer pageSize) {
		
		//加读锁
		readLock.lock();
		
		// 创建QueryParser对象，并指定要查询的索引域及分词对象
		QueryParser queryParser = new MultiFieldQueryParser(new String[] { "title", "content" }, getAnalyzer());

		// 执行查询语句，获取返回的TopDocs对象,size为返回多少条记录
		TopDocs search = null;

		// 通过IndexReader流对象创建IndexSearcher
		IndexSearcher searcher = null;
		List<Article> articles = null;
		
		try (
			// 获取lucene索引目录,
			IndexReader reader=getIndexReader();
		) {
			// 创建Query对象，指定查询条件
			Query query = queryParser.parse(queryStr);
			searcher = new IndexSearcher(reader);
			search = pageSearch(query, searcher, pageNum, pageSize);

			articles = convertToArticles(query, reader, search, searcher);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			//释放读锁
			readLock.unlock();
		}

		Long totalPage = search.totalHits % pageSize == 0 ? search.totalHits % pageSize : search.totalHits / pageSize;

		return new Page<>(articles, pageNum, pageSize, totalPage.intValue(), (int) search.totalHits);
	}

	private IndexReader getIndexReader() throws IOException {
		Directory directory = getDirectory();
		// 通过索引目录创建IndexReader流对象
		return DirectoryReader.open(directory);
	}

	public TopDocs pageSearch(Query query, IndexSearcher searcher, Integer pageNum, Integer pageSize)
			throws IOException {

		ScoreDoc sd;

		if (pageNum == 1) {
			sd = null;
		} else {
			int num = pageSize * (pageNum - 1);// 获取上一页的最后一条记录的index
			TopDocs td = searcher.search(query, num);
			sd = td.scoreDocs[num - 1];
		}

		// 核心方法
		return searcher.searchAfter(sd, query, pageSize);//  
	}

	private List<Article> convertToArticles(Query query, IndexReader reader, TopDocs search, IndexSearcher searcher) {
		// 获取查询到的数据
		ScoreDoc[] scoreDocs = search.scoreDocs;

		List<Article> articles = new ArrayList<>();

		QueryScorer scorer = new QueryScorer(query);
		SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font style='color:red;'>", "</font>");// 设定高亮显示的格式<B>keyword</B>,此为默认的格式
		Highlighter highlighter = new Highlighter(simpleHtmlFormatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(100));// 设置每次返回的字符数

		Article article = null;
		String intro = null;
		String title = null;
		String id = null;
		try {
			for (ScoreDoc doc : scoreDocs) {
				Document document = null;

				document = searcher.doc(doc.doc);
				intro = highlighter.getBestFragment(getAnalyzer(), "content", document.get("content"));
				title = highlighter.getBestFragment(getAnalyzer(), "title", document.get("title"));
				id = document.get("id");
				article = new Article();

				if (StrKit.isBlank(intro)) {
					intro = document.get("content").substring(0, 100);
				}
				if (StrKit.isBlank(title)) {
					title = document.get("title");
				}

				article.setIntro(intro);
				article.setId(Integer.parseInt(id));
				article.setTitle(title);
				articles.add(article);
			}
			return articles;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		
	}

	public long deleteAll() {
		writeLock.lock();
		// 通过指定的索引目录地址、配置对象创建IndexWriter流对象
		IndexWriter writer = getIndexWriter();
		
		try {
			writer.deleteAll();
			return writer.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			 writeLock.unlock();
		}

	}

	/**
	 * 根据查询条件删除lucene索引及文档数据
	 * 
	 * @param key
	 * @param value
	 */
	public long deleteIndex(String key, String value) {
		
		writeLock.lock();
		// 通过指定的索引目录地址、配置对象创建IndexWriter流对象
		IndexWriter writer = getIndexWriter();

		try {
			  writer.deleteDocuments(new Term(key, value));
			  return writer.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 更新索引
	 * 
	 * @param key
	 * @param value
	 * @param document
	 */
	public long updateIndex(String key, String value, Document document) {
		writeLock.lock();
		IndexWriter writer = getIndexWriter();
		try {
			  writer.updateDocument(new Term(key, value), document);
			  return writer.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			 writeLock.unlock();
		}
	 
	}
}
