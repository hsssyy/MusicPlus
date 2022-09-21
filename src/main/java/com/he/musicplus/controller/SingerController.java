package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Singer;
import com.he.musicplus.service.SingerService;
import com.he.musicplus.utils.ChangeTime;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 歌手控制类
 */
@RestController
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    /**
     * 查询所有歌手 以及分页
     *
     * @return
     */
    @RequestMapping(value = "/allSinger", method = RequestMethod.GET)
    public Object allSinger(@RequestParam(value = "pn", defaultValue = "1") Integer pn) throws ParseException {
        //分页查询数据
        Page<Singer> singerPage = new Page<>(pn, 5);
        //分页查询结果
        Page<Singer> page = singerService.page(singerPage, null);

        Date time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//这里可以指定日期的格式
        List<Singer> list = page.getRecords();
        for (int i = 0; i < list.size(); i++) {
//            Date birth = list.get(i).getBirth();//字符串转换为日期格式
//            String ByBirth = sdf.format(birth);//指定日期的格式
//            list.get(i).setBirth(ChangeTime.StringChangeTime(ByBirth));//修改某一行数据的生日
        }
        return page;
    }

       /**
     * 查询歌手返回前10条  用在前台展示
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/allSingerSelect", method = RequestMethod.GET)
    public Object allSingerSelect() throws ParseException {
        List<Singer> list = singerService.list(new QueryWrapper<Singer>().last("limit 10"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这里可以指定日期的格式
        for (int i = 0; i < list.size(); i++) {
            Date birth =  list.get(i).getBirth();//字符串转换为日期格式
            String ByBirth = sdf.format(birth);//指定日期的格式
            list.get(i).setBirth(ChangeTime.StringChangeTime(ByBirth));//修改某一行数据的生日
        }
        return   singerService.listMaps(new QueryWrapper<Singer>().last("limit 10"));
    }

    /**
     * 根据性别查询歌手    用在前台展示
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/singerOfSex", method = RequestMethod.GET)
    public Object singerOfSex(@RequestParam("sex") Integer sex){
        return  singerService.listMaps(new QueryWrapper<Singer>().eq("sex",sex));
    }
    /**
     * 根据id查询歌手 信息   用在前台展示
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/singerOfId", method = RequestMethod.GET)
    public Object singerOfId(@RequestParam("id") Integer id){
        return  singerService.getById(id);
    }

    /**
     * 删除歌手
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") Integer id){
        return singerService.removeById(id);
    }
    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/someDelete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") List ids){
        return singerService.removeByIds(ids);
    }
    /**
     * 添加歌手
     */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public Object addConsumer(@RequestBody Singer singer)  {
        return singerService.save(singer);
    }
    /**
     * 修改歌手
     */
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public Object updateSinger(@RequestBody  Singer singer) throws ParseException {
        Date birth = singer.getBirth();
        System.out.println();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String birthday = sdf.format(birth);
        singer.setBirth(ChangeTime.StringChangeTime(birthday));

        return singerService.updateById(singer);
    }

    /**
     * 更新图片
     */
    @RequestMapping(value = "/updateSingerPic",method = RequestMethod.POST)
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
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.updateById(singer);
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
