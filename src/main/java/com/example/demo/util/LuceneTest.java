package com.example.demo.util;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.Article;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author chengdu
 * @date 2019/8/28.
 */
public class LuceneTest {



    private static String TEXT_CONTENTS = "contents";
    private static String TEXT_FILENAME = "fileName";
    private static String TEXT_FILEPATH = "filePath";

    public static void writeFileDoc(String filePath, String luceneIndex) throws Exception{
        Document document = new Document();
        File file = new File(filePath);

        document.add(new TextField(TEXT_CONTENTS, new FileReader(file)));
        document.add(new TextField(TEXT_FILENAME, file.getName(), Field.Store.YES));
        document.add(new TextField(TEXT_FILEPATH, file.getAbsolutePath(),Field.Store.YES));

        IndexWriter indexWriter = getIndexWriter(luceneIndex);
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    public static List<String> searchFile(String luceneIndex, String searchText, int lines, List<String> filePaths, List<String> fileNames) throws Exception{
        Directory dir = FSDirectory.open(Paths.get(luceneIndex));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
//		Analyzer analyzer = new StandardAnalyzer();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        QueryParser queryParser = new QueryParser(TEXT_CONTENTS, analyzer);
        Query query = queryParser.parse(searchText);
        TopDocs hits = is.search(query, lines);
        for(ScoreDoc scoreDoc : hits.scoreDocs){
            Document document = is.doc(scoreDoc.doc);
            filePaths.add(document.get(TEXT_FILEPATH));
            fileNames.add(document.get(TEXT_FILENAME));
        }
        reader.close();
        return filePaths;
    }

    public static IndexWriter getIndexWriter(String luceneIndex) throws Exception{
        Directory directory = FSDirectory.open(Paths.get(luceneIndex));
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        return indexWriter;
    }

    public static IndexSearcher getIndexSearcher(String luceneIndex) throws Exception{
        Directory directory = FSDirectory.open(Paths.get(luceneIndex));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }

    public static void addLuceneIndex(IndexWriter indexWrite, Object object) throws Exception{
        Class<? extends Object> clazz = object.getClass();
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        Document document = new Document();
        for(java.lang.reflect.Field field : fields){
            //access  modifiers "private"
            field.setAccessible(true);
//			System.out.println(field.getName());
//			System.out.println(field.get(object));
//			System.out.println(field.getType());
//			StringField stringField = new StringField(field.getName(), String.valueOf(field.get(object)), Field.Store.YES);
//			document.add(stringField);
            String type = field.getType().toString();
            //日期类型字段需要格式化串处理
            if(!type.endsWith("Date")){
                TextField txtField = new TextField(field.getName(), String.valueOf(field.get(object)), Field.Store.YES);
                document.add(txtField);
            }
        }
        indexWrite.addDocument(document);
    }

    public static List<Article> searchLuceneIndex(String lucenePath, String search) throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher(lucenePath);
        //中文分析器
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        // 建立查询解析器
        QueryParser contentparser = new QueryParser("content",analyzer);
        Query cQuery = contentparser.parse(search);
        //查询
        TopDocs topDocs = indexSearcher.search(cQuery, 100);
        //查询分数
        QueryScorer scorer=new QueryScorer(cQuery);
        //显示得分高的片段
        Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
        //输出html样式
        SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
        //高亮
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        //遍历文档
        List<Article> articles = new ArrayList<Article>();
        for(ScoreDoc scoreDoc : topDocs.scoreDocs){
            Article article = new Article();
            Document document = indexSearcher.doc(scoreDoc.doc);
            article.setId(document.get("id") != null ? Long.valueOf(document.get("id")) : 0L);
            article.setTitle(document.get("title"));
            //html <tag> 标记出查询到的部分
            String content = document.get("content");
            if(content != null){
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String highlicontent = highlighter.getBestFragment(tokenStream, content);
                article.setContent(highlicontent);
            }
            articles.add(article);
        }
        //关闭读取流
        indexSearcher.getIndexReader().close();
        return articles;
    }

    public static void testArticle() throws Exception {
        String luceneIndexPo = "D:/lucene";
        //Article	写入磁盘索引库
//		Article article = new Article(1L, "第一篇文章", "内容1");
//		Article article2 = new Article(2L, "第二篇文章", "内容2");
//		Article article3 = new Article(2L, "第三篇文章", "内容3");
//		IndexWriter indexWriter = getIndexWriter(luceneIndexPo);
//		addLuceneIndex(indexWriter, article);
//		addLuceneIndex(indexWriter, article2);
//		addLuceneIndex(indexWriter, article3);
//		indexWriter.close();
        List<Article> articles = searchLuceneIndex(luceneIndexPo,"内容");
        for(Article art : articles){
            System.out.println(art);
        }
    }


    public static void main(String[] args) throws Exception{
        //idnexFile
        String originFile = "C://Users//chengdu//Desktop//filetype/1.txt";
        String luceneIndex = "D:/lucene2";
		writeFileDoc(originFile, luceneIndex);

        //Search
        List<String> files = new ArrayList<String>();
        List<String> names = new ArrayList<String>();
		searchFile(luceneIndex, "name", 10, files, names);
        for(String filePath : files){
            System.out.println(filePath);
        }
        for(String name : names){
            System.out.println(name);
        }
    }

}
