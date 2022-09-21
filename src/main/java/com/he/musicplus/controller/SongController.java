package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Singer;
import com.he.musicplus.domain.Song;
import com.he.musicplus.service.SongService;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 歌曲控制类
 */
@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;
    /**
     * 添加歌曲
     */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public Object addSong(@RequestBody Song song)  {

        Date date = new Date(System.currentTimeMillis());//获取当前时间。
        if(song.getCreateTime()==null){
            song.setCreateTime(date);//
        }
        song.setUpdateTime(date);

        return songService.save(song);
    }
    /**
     * 更新歌曲(可以包含上传歌曲文件)
     */
    @RequestMapping(value = "/updateSongUrl",method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile url, @RequestParam("id") int id){
        JSONObject jsonObject = new JSONObject();
        if (url.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的名字
        String fileName = System.currentTimeMillis()+"";
        //问件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"song";
        //如果文件 不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/song/"+fileName;
        try {
            url.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeAvatorPath);
            boolean flag = songService.updateById(song);
            //       System.out.println(flag);
            if (flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"上传成功");
                jsonObject.put("url",storeAvatorPath);
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
     * 根据歌手id查询歌曲  用在后台分页
     */
    @RequestMapping(value = "/singer/selectSongs",method = RequestMethod.GET)
    public Object songOfSingerId(@RequestParam("singerId") Integer singerId,@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        //分页查询数据
        Page<Song> songPage = new Page<>(pn, 3);
        //分页查询结果
        Page<Song> page = songService.page(songPage, new QueryWrapper<Song>().eq("singer_id",singerId));
        return page;
    }

    /**
     * 用在前台 歌手中的歌单
     * @param singerId
     * @return
     */
    @RequestMapping(value = "/singer/songsOfSingerId",method = RequestMethod.GET)
    public Object songsOfSingerId(@RequestParam("singerId") Integer singerId){

        return songService.listMaps(new QueryWrapper<Song>().eq("singer_id",singerId));
    }
    /**
     * 更新歌曲图片
     */
    @RequestMapping(value = "/updateSongPic",method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") Integer id){
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的名字
        String fileName = System.currentTimeMillis()+"";
        //问件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songPic";
        //如果文件 不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/songPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeAvatorPath);
//            boolean flag = songService.update(song);
            boolean flag = songService.updateById(song);
            //       System.out.println(flag);
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
     * 修改歌曲信息
     */
    @RequestMapping(value = "/updateSong" , method = RequestMethod.POST)
    public Object updateConsumer(@RequestBody  Song song){
        return songService.updateById(song);
    }
    /**
     * 删除歌曲
     */
    @RequestMapping(value = "/deleteSong" , method = RequestMethod.GET)
    public Object deleteSong(@RequestParam("id") Integer id){
        return songService.removeById(id);
    }
    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/someDeleteSong" , method = RequestMethod.GET)
    public Object deleteSongs(@RequestParam("id") List ids){
        return songService.removeByIds(ids);
    }
    /**
     * 根据歌曲名字获取歌曲 精准查询
     */
    @RequestMapping(value = "/songByName",method = RequestMethod.GET)
    public Object  selectSongByName(@RequestParam("name") String name){
        return songService.getOne(new QueryWrapper<Song>().eq("name",name));
    }
    /**
     * 根据歌曲id 查询该歌曲  前台也使用了  用在歌单的列表查询返回
     */
    @RequestMapping(value = "/songBySongId" , method = RequestMethod.GET)
    public Object selectSongBySongId(@RequestParam("songId") Integer songId){
        return songService.getOne(new QueryWrapper<Song>().eq("id",songId));
    }
    /**
     * 模糊查询 根据歌手名字
     */
    @RequestMapping(value = "/likeSongName",method = RequestMethod.GET)
    public Object likeSongName(@RequestParam("songName") String name){
        return songService.listMaps(new QueryWrapper<Song>().like("name",name));
    }
    /**
     * 设置歌曲为VIP
     */
    @RequestMapping(value = "/setVip",method = RequestMethod.GET)
    public Object setVip(@RequestParam("id")Integer id){
        Song song = new Song();
        song.setSetVip((byte) 1);
        return songService.update(song,new QueryWrapper<Song>().eq("id",id));
    }
    /**
     * 取消歌曲为VIP
     */
    @RequestMapping(value = "/removeVip",method = RequestMethod.GET)
    public Object removeVip(@RequestParam("id")Integer id){
        Song song = new Song();
        song.setSetVip((byte) 0);
        return songService.update(song,new QueryWrapper<Song>().eq("id",id));
    }

}
