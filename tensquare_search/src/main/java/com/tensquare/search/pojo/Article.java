package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
@Document(indexName = "tensquare_article",type = "article")//tensuqare_article是elasticsearch的索引,必须事先创建好
public class Article implements Serializable {
    //只是把数据库中列为id<title,content,state存入elasticsearch用来索引
    @Id
    private String id;
    //是否索引,就是看该域能被搜索
    //是否分词,就表示搜索时候是整体匹配还是单词匹配
    //是否存储,就是是否在页面上显示
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")//Field对应数据库的列,存的时候按analyser分词器存,查找的时候按searchAnalyser查找,两者一致
    private String title;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;
    private String state;//审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
