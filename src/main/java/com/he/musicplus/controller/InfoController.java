package com.he.musicplus.controller;


import com.he.musicplus.domain.Singer;
import com.he.musicplus.service.ConsumerService;
import com.he.musicplus.service.SingerService;
import com.he.musicplus.service.SongListService;
import com.he.musicplus.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统首页控制
 */
@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private SongService songService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongListService songListService;
    /**
     * 用户统计 返回所有的列表
     * @return
     */
    @RequestMapping(value = "/countConsumer",method = RequestMethod.GET)
    public Object countConsumer(){
        return consumerService.list();
    }
    /**
     * 歌曲统计 返回所有的列表
     * @return
     */
    @RequestMapping(value = "/countSong",method = RequestMethod.GET)
    public Object countSong(){
        return songService.list();
    }
    /**
     * 歌手统计 返回所有的列表
     * @return
     */
    @RequestMapping(value = "/countSinger",method = RequestMethod.GET)
    public Object countSinger(){
        return singerService.list();
    }
    /**
     * 歌单统计 返回所有的列表
     * @return
     */
    @RequestMapping(value = "/countSongList",method = RequestMethod.GET)
    public Object countSongList(){
        return songListService.list();
    }
}
