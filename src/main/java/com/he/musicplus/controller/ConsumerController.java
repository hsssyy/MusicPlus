package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.service.ConsumerService;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 用户控制类
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController  {
    @Autowired
    private ConsumerService consumerService;
    /**
     * 查询所有用户 以及分页
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
        Page<Consumer> consumerPage = new Page<>(pn, 3);
        //分页查询结果
        Page<Consumer> page = consumerService.page(consumerPage, null);

        Date time = null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//这里可以指定日期的格式
        List<Consumer> list = page.getRecords();
        for (int i = 0;i<list.size();i++) {
            Date birth = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).getBirth()) ;//字符串转换为日期格式
            String ByBirth =  sdf.format(birth);//指定日期的格式
            list.get(i).setBirth(ByBirth);//修改某一行数据的生日

            String avator = list.get(i).getAvator();//获取头像路径
            list.get(i).setAvator("http://localhost:8888"+avator);//修改头像路径，前面加上请求头
        }

        return page;
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
    public Object deleteConsumer(@RequestParam("ids") List ids){
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
     * 添加用户
     */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public Object addConsumer(@RequestBody  Consumer consumer){
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
        String filePath = ResourceUtils.getURL("classpath:").getPath()+ "static/avatorImages";
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
}