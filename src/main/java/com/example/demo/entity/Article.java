package com.example.demo.entity;

/**
 * @author chengdu
 * @date 2019/8/28.
 */
public class Article {

    private Long id;
    private String title;
    private String content;

    public Article(){

    }

    public Article(Long i, String t, String c){
        id = i;
        title = t;
        content = c;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return "{ \'id\': " + id + ",\'title\':" + title + ",\'content\':" + content + "}";
    }
}