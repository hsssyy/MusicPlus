package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Singer;
import com.he.musicplus.domain.Song;
import com.he.musicplus.domain.SongList;
import com.he.musicplus.service.SingerService;
import com.he.musicplus.service.SongListService;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 歌单控制类
 */
@RestController
@RequestMapping("/songList")
public class SongListController {
    @Autowired
    private SongListService songListService;

    /**
     * 查询所有歌单 以及分页
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object allConsumer(@RequestParam(value = "pn", defaultValue = "1") Integer pn) throws ParseException {
        //分页查询数据
        Page<SongList> songListPage = new Page<>(pn, 5);
        //分页查询结果
        Page<SongList> page = songListService.page(songListPage,null);

        return page;
    }

    /**
     * 查询歌单返回前10条  用在前台展示
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/allSongListSelect", method = RequestMethod.GET)
    public Object allSingerSelect() throws ParseException {
        return  songListService.listMaps(new QueryWrapper<SongList>().last("limit 10"));
    }
    /**
     *
     */
    @RequestMapping(value = "/likeTitle",method = RequestMethod.GET)
    public Object songListLikeTitle(@RequestParam("title") String title){
        return  songListService.listMaps(new QueryWrapper<SongList>().like("title",title));
    }

    /**
     * 根据风格(style)模糊查询歌单列表
     */
    @RequestMapping(value = "/likeStyle",method = RequestMethod.GET)
    public Object likeStyle(@RequestParam("style") String style)  {
        return songListService.listMaps(new QueryWrapper<SongList>().like("style",style));
    }
    /**
     * 删除歌单
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") Integer id){
        return songListService.removeById(id);
    }
    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/someDelete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") List ids){
        return songListService.removeByIds(ids);
    }
    /**
     * 添加歌单
     */
    @RequestMapping(value = "/insert" , method = RequestMethod.POST)
    public Object addConsumer(@RequestBody SongList songList)  {

        return songListService.save(songList);
    }
    /**
     * 修改歌单
     */
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public Object updateConsumer(@RequestBody  SongList songList){
        return songListService.updateById(songList);
    }
    /**
     * 更新歌单图片
     */
    @RequestMapping(value = "/updateSongListPic",method = RequestMethod.POST)
    public Object updateConsumerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") Integer id) throws FileNotFoundException {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的名字
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //问件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songListPic";
        //如果文件 不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/songListPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean flag = songListService.updateById(songList);
            if (flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"上传成功");
                jsonObject.put("pic",storeAvatorPath);
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
     * 根据歌单id 查询歌单信息
     */
    @RequestMapping(value = "/getSongListInfoById" , method = RequestMethod.GET)
    public Object getSongListInfoById(@RequestParam("id") Integer id){
        return songListService.getById(id);
    }



}
