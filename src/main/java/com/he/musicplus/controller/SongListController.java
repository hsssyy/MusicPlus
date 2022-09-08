package com.he.musicplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Singer;
import com.he.musicplus.domain.SongList;
import com.he.musicplus.service.SingerService;
import com.he.musicplus.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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



}
