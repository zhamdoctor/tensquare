package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
//autowired和resource区别：@resource:按照名称找到注入，，如果写了名称找不到就不会按类型，不写的话名称找不到按类型找。autowired按类型注入，如果类型找不到按类的名称注入，加个@autowired和@qualifier结合直接按名称注入

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){//common里的result
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true,StatusCode.OK,"查找成功",spitService.findById(spitId));
    }
    @RequestMapping(method = RequestMethod.POST)//保存用post
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true,StatusCode.OK,"保存成功");
    }
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)//保存用post
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);//service中已经用idworker产生了spitid
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)//保存用post
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> pageData=spitService.findByParentid(parentid,page,size);//SpitService没有这个方法是因为类.方法,方法不是静态还没加载
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageData.getTotalElements(),pageData.getContent()));

    }
    @RequestMapping(value = "/thumnup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        //判断当前用户是否已经点赞,但是现在没做认证,先把userid写死
        String userid="1111";
        if(redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            return new Result(true,StatusCode.REPERROR,"重复点赞");
        }
        spitService.thumbup(spitId);
        redisTemplate.opsForValue().set("thumb_"+userid,1);//重复点赞之后还要把重复点赞的还原到1个点赞,redis中的点赞数column值格式为thumb_userid
        return new Result(true, StatusCode.OK,"点赞成功");
    }
}
