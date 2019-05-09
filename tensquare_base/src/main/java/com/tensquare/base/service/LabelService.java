package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }
    public  Label findById(String id){
        return labelDao.findById(id).get();
    }
    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }
    public void update(Label label){
        labelDao.save(label);
    }
    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll();
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
//封装分页对象
        Pageable pageable= PageRequest.of(page-1,size);
        return labelDao.findAll(new Specification<Label>() {
            /*
             * root根对象，条件封装到哪个对象中，“where 类名=Label.getid”
             * query封装查询关键字，比如group by，boder by
             * cb封装条件对象，直接返回null表示不需要任何条件
             * */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //new一个list集合存放条件
                List<Predicate> list=new ArrayList<>();
                if (label.getLabelname()!=null&&!"".equals(label.getLabelname())){//用于判断json中数据是否为null
                    Predicate predicate=cb.like(root.get("labelname").as(String.class),"%"+label.getLabelname()+"%");//从root中得到类名,封装类型,where labelname like "%小明%"
                    list.add(predicate);
                }
                if (label.getState()!=null&&!"".equals(label.getState())){
                    Predicate predicate=cb.equal(root.get("state").as(String.class),label.getState());//where state="1"
                    list.add(predicate);
                }
                //new一个数组作为最终返回值的条件
                Predicate[] parr=new Predicate[list.size()];
                //把list直接转成数组
                parr=list.toArray(parr);
                return cb.and(parr);//where labelname like "%小明%" and state="1"
            }
        },pageable);
    }
}
