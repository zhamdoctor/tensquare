package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Spit> findAll(){
        return spitDao.findAll();
    }
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }
    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//这一系列初始值设定为了防止空指针操作
        spit.setVisits(0);
        spit.setShare(0);
        spit.setThumbup(0);
        spit.setComment(0);//如果当前添加的吐槽有父节点,那么父节点吐槽回复数加1
        if(spit.getParentid()!=null&&!"".equals(spit.getParentid())){//有回复则父节点回复加1
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));//更新父节点的回复数
            Update update=new Update();
            update.inc("comment",1);//父节点query指定,加1由update指定
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spit.setState("1");

        spitDao.save(spit);
    }
    public void update(Spit spit){
        spitDao.save(spit);
    }
    public void deleteById(String id){
        spitDao.deleteById(id);
    }
    public Page<Spit> findByParentid(String parentid,int page,int size){
        Pageable pageable= PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    public void thumbup(String spitId) {
    /*    //这种方式效率低
        Spit spit=spitDao.findById(spitId).get();
        spit.setThumbup((spit.getThumbup()==null?0:spit.getThumbup())+1);
        spitDao.save(spit);*/
    //  原生mongo命令实现点赞数自增db.spit.update("_id":"1",{$inc:{thumbup:NumberInt(1)}})//spit是mongo中的表
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is("1"));
        Update update=new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");//上面创建一个mongotemplate实例,拼装号后直接一次数据库操作,上面的两次
    }
}
