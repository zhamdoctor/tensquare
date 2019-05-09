package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    //newlist,hotlist,waitlist问答的三个列表代码,新问题,热门问题,等待回答问题
	@Query(value = "select * from tb_problem,tb_pl where id=problemid and labelid=? order by replytime desc",nativeQuery = true)
    public Page<Problem> newList(String labelid, Pageable pageable);
    @Query(value = "select * from tb_problem,tb_pl where id=problemid and labelid=? order by reply desc",nativeQuery = true)
    public Page<Problem> hotList(String labelid, Pageable pageable);
    @Query(value = "select * from tb_problem,tb_pl where id=problemid and labelid=? and reply=0 order by createtime desc",nativeQuery = true)
	public Page<Problem> waitList(String labelid, Pageable pageable);
}
