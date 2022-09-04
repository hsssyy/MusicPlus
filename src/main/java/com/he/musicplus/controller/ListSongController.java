package com.he.musicplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.musicplus.domain.ListSong;
import com.he.musicplus.domain.Song;
import com.he.musicplus.service.ListSongService;
import com.he.musicplus.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 歌单中的歌曲 控制类
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {
    @Autowired
    private ListSongService listSongService;
    /**
     * 向某个歌单添加歌曲
     */
    @RequestMapping(value = "/insert" , method = RequestMethod.POST)
    public Object addSong( @RequestParam("songId") Integer songId, @RequestParam("songListId") Integer songListId )  {
        ListSong listSong = new ListSong();
        listSong.setSongId(songId);
        listSong.setSongListId(songListId);
        return listSongService.save(listSong);
    }
    /**
     * 根据歌单id查询歌曲Id
     */
    @RequestMapping(value = "/songIdBySongListId",method = RequestMethod.GET)
    public Object songsBySongListId(@RequestParam("songListId") Integer songListId){
        return listSongService.listMaps(new QueryWrapper<ListSong>().eq("song_list_id",songListId));
    }
    /**
     * 删除歌单中的歌曲
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.GET)
    public Object deleteListSong(@RequestParam("songId") Integer songId,@RequestParam("songListId") Integer songListId){
        return listSongService.remove(new QueryWrapper<ListSong>().eq("song_id",songId).eq("song_list_id",songListId));
    }
    /**
     * 批量删除歌单中的歌曲
     * @return
     */
    @RequestMapping(value = "/someDelete" , method = RequestMethod.GET)
    public Object deleteListSongs(@RequestParam("songId") List songLists,@RequestParam("songListId") Integer songListId){
        Boolean flag = true;
        for(int i = 0;i<songLists.size();i++){
           flag= listSongService.remove(new QueryWrapper<ListSong>().eq("song_id",songLists.get(i)).eq("song_list_id",songListId));
            if(flag==false){
                return false;
            }
        }
        return flag;
    }
}
