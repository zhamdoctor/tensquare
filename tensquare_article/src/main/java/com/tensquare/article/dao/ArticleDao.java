package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
	@Modifying//增删改都要加这个,可能发生线程问题的都要加
    @Query(value = "update tb_article set state=1 where id=?",nativeQuery = true)
    public void updateState(String id);
	//点赞
    @Modifying//增删改都要加这个,可能发生线程问题的都要加
    @Query(value = "update tb_article set thumbup=thumbup+1 where id=?",nativeQuery = true)
    public void addThumbup(String id);
}
