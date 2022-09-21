package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Vip;
import com.he.musicplus.domain.VipConsumer;
import com.he.musicplus.service.ConsumerService;
import com.he.musicplus.service.VipService;
import com.he.musicplus.utils.ChangeTime;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 用户控制类
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController  {
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private VipService vipService;
    /**
     * 查询普通用户 以及分页
     * @return
     */
    @RequestMapping(value = "/allConsumer",method = RequestMethod.GET)
//    public Object allConsumer(){
//        Date time = new Date();
//        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//这里可以指定日期的格式
//        List<Map<String, Object>> maps = consumerService.listMaps();
//        for (int i = 0;i<maps.size();i++) {
//            Object birth = maps.get(i).get("birth");//获取某一行数据的生日
//            String ByBirth = sdf.format(birth);
//            maps.get(i).put("birth",ByBirth);
//            Object avator = maps.get(i).get("avator");//获取头像路径
//            maps.get(i).put("avator","http://localhost:8888"+avator);
//        }
//        return maps;
//    }
        public Object allConsumer(@RequestParam(value = "pn",defaultValue = "1") Integer pn) throws ParseException {
        //分页查询数据
        Page<Consumer> consumerPage = new Page<>(pn, 5);
        //分页查询结果
        Page<Consumer> page = consumerService.page(consumerPage, null);
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
//        queryWrapper.

        Date time = null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//这里可以指定日期的格式
        List<Consumer> list = page.getRecords();
        for (int i = 0;i<list.size();i++) {
            String avator = list.get(i).getAvator();//获取头像路径
//            list.get(i).setAvator("http://localhost:8888"+avator);//修改头像路径，前面加上请求头
        }

        return page;
    }

    /**
     * 查询VIP用户  会员没过期
     */
    @RequestMapping(value = "/allVipConsumer",method = RequestMethod.GET)
    public Object allVipConsumer(@RequestParam(value = "pn",defaultValue = "1") Integer pn) throws ParseException{
        JSONObject jsonObject = new JSONObject();
        //分页查询出VIP表的 数据
        Page<Vip> vipPage = new Page<>(pn, 5);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));

        //分页查询结果
        Page<Vip> page = vipService.page(vipPage,new QueryWrapper<Vip>().gt("end_time",formatter.format(date)));
        List<Vip> list = page.getRecords();

        List<VipConsumer> vipConsumers = new ArrayList<>();
        for (int i = 0; i<list.size(); i++) {

            Consumer one = consumerService.getOne(new QueryWrapper<Consumer>().eq("id", list.get(i).getConsumerId()));
            VipConsumer vip = new VipConsumer();
            vip.setId(one.getId());
            vip.setUsername(one.getUsername());
            vip.setSex(one.getSex());
            vip.setBirth(one.getBirth());
            vip.setEmail(one.getEmail());
            vip.setIntroduction(one.getIntroduction());
            vip.setLocation(one.getLocation());
            vip.setAvator(one.getAvator());

            vip.setPhoneNum(one.getPhoneNum());
            vip.setCreateTimeVip(list.get(i).getCreateTime());
            vip.setEndTime(list.get(i).getEndTime());
            vip.setMoneys(list.get(i).getMoneys());
            vip.setLevel(list.get(i).getLevel());
            vipConsumers.add(vip);
        }

        jsonObject.put("records",vipConsumers);
        jsonObject.put("size",page.getSize());
        jsonObject.put("total",page.getTotal());
        return jsonObject;
    }
    /**
     * 模糊查询用户名 以及分页
     */
    @RequestMapping(value = "/selectLikeUserName",method = RequestMethod.GET)
    public Object selectLikeUserName(@RequestParam(value = "pn",defaultValue = "1") Integer pn,@RequestParam("username") String username){
        //分页查询数据
        Page<Consumer> consumerPage = new Page<>(pn, 5);
        //分页查询结果
        Page<Consumer> page = consumerService.page(consumerPage, new QueryWrapper<Consumer>().like("username",username));
        return page;
    }

    /**
     * 根据用户id 查询用户所有信息
     */
    @RequestMapping(value = "/selectByPrimaryKey",method = RequestMethod.GET)
    public Object selectByPrimaryKey(@RequestParam("id")Integer id)  {
        return consumerService.getById(id);
    }
    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") Integer id){
        return consumerService.removeById(id);
    }
    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/someDelete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") List ids){
        return consumerService.removeByIds(ids);
    }
    /**
     * 修改用户
     */
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public Object updateConsumer(@RequestBody  Consumer consumer){

        return consumerService.updateById(consumer);
    }
    /**
     * 添加用户  可以用在后台添加用户和前台注册。
     */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public Object addConsumer(@RequestBody  Consumer consumer) throws ParseException {
        Date birth = consumer.getBirth();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String birthday = sdf.format(birth);
        consumer.setBirth(ChangeTime.StringChangeTime(birthday));
        Date nowTime = new Date();
        String now = sdf.format(nowTime);
        consumer.setCreateTime(ChangeTime.StringChangeTime(now));
        return consumerService.save(consumer);
    }
    /**
     * 更新图片
     */
    @RequestMapping(value = "/updateConsumerPic",method = RequestMethod.POST)
    public Object updateConsumerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") Integer id) throws FileNotFoundException {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的名字
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath =  System.getProperty("user.dir")+System.getProperty("file.separator")+"avatorImages";
//        System.out.println("文件路径："+filePath);
        //如果文件 不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/avatorImages/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(storeAvatorPath);
//            boolean flag = consumerService.update(consumer);
            boolean flag = consumerService.updateById(consumer);
            System.out.println(flag);
            if (flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"上传成功");
                jsonObject.put("avator",storeAvatorPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }

    /**
     * 用户登录 前台
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object loginIn(@RequestBody Consumer consumer){
        JSONObject jsonObject = new JSONObject();
        Consumer one = consumerService.getOne(new QueryWrapper<Consumer>().eq("username", consumer.getUsername()).eq("password", consumer.getPassword()));
        if (consumer.getUsername()==null||"".equals(consumer.getUsername())){//?username==null||username.equals("")
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"用户名不能为空");
            return jsonObject;
        }
        if (consumer.getPassword()==null||"".equals(consumer.getPassword())){//?
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"密码不能为空");
            return jsonObject;
        }
        if(one!=null){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"登录成功");
            jsonObject.put("userMsg",one);
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"用户名或密码错误");
        return jsonObject;
    }
    /**
     * 根据id 查询用户
     */
    @RequestMapping(value = "/getUserOfId",method = RequestMethod.GET)
    public Object getUserByOfId(@RequestParam("id")Integer id){
        return consumerService.getById(id);
    }

}
