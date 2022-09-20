package com.he.musicplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.musicplus.domain.ListSong;
import com.he.musicplus.domain.Rank;
import com.he.musicplus.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 评价 评分控制类
 */
@RestController

public class RankController {
    @Autowired
    private RankService rankService;

    /**
     * 计算歌单平均分
     * @param songListId
     * @return
     */
    @RequestMapping(value = "/rank",method = RequestMethod.GET)
    public Object getRankScore(@RequestParam("songListId") Integer songListId){

        List<Map<String, Object>> rankScore = rankService.listMaps(new QueryWrapper<Rank>().eq("song_list_id", songListId));
        int nums = rankScore.size();//评论的人数
        if(nums == 0){
            return 5;
        }
        int sumScore  = 0;
        for(int i = 0;i<rankScore.size();i++){ //计算总分
            sumScore += (Integer) rankScore.get(i).get("score");
        }
        return sumScore/nums;//返回平均分
    }
    @RequestMapping(value = "/rank/add",method = RequestMethod.POST)
    public Object addRank(@RequestBody Rank rank){
        return rankService.save(rank);
    }
    /**
     *根据用户id 和 歌单id 获取自己的评分
     */
    @RequestMapping(value = "/rank/scoreByUserIdAndSongListId",method = RequestMethod.GET)
    public Object getScore(@RequestParam("userId")Integer userId ,@RequestParam("songListId")Integer songListId ){
        return rankService.getOne(new QueryWrapper<Rank>().eq("consumer_id",userId).eq("song_list_id",songListId));
    }

}
