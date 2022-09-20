package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Collect;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Vip;
import com.he.musicplus.service.CollectService;
import com.he.musicplus.utils.ChangeTime;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 收藏控制类
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 根据用户id 查询收藏表
     * @param userId
     * @return
     */
    @RequestMapping(value = "/collectOfUserId",method = RequestMethod.GET)
    public Object collectOfUserId(@RequestParam("userId") Integer userId){
        return collectService.listMaps(new QueryWrapper<Collect>().eq("user_id",userId));
    }
    /**
     * 添加收藏
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addCollect(HttpServletRequest request) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId"); // 用户id
        String type = request.getParameter("type");  //收藏类型（0歌曲1歌单）
        String songId = request.getParameter("songId");//歌曲id
        String songListId = request.getParameter("songListId"); //歌单id
        //获取当前时间
        Date date = new Date();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String createTime = sdf.format(date);


        if(songId==null||songId.equals("")){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"收藏歌曲为空");
            return jsonObject;
        }
        if(collectService.list(new QueryWrapper<Collect>().eq("user_id",userId).eq("song_id",songId)).size()>0){//Integer.parseInt(userId),Integer.parseInt(songId)
            jsonObject.put(Consts.CODE,2);
            jsonObject.put(Consts.MSG,"已收藏");
            return jsonObject;
        }

        //保存到收藏的对象中
        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        collect.setSongId(Integer.parseInt(songId));
        collect.setCreateTime(ChangeTime.StringChangeTime(createTime));
        boolean flag = collectService.save(collect);
        if (flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"收藏成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"收藏失败");
        return jsonObject;
    }

    /**
     * 根据用户id 查询收藏表 以及分页
     * @return
     */
    @RequestMapping(value = "/getCollect",method = RequestMethod.GET)
    public Object getCollect(@RequestParam(value = "pn",defaultValue = "1") Integer pn,@RequestParam("userId") Integer userId) throws ParseException {
        //分页查询数据
        Page<Collect> collectPage = new Page<>(pn, 5);
        //分页查询结果
        Page<Collect> page = collectService.page(collectPage,new QueryWrapper<Collect>().eq("user_id",userId));
        return page;
    }
    /**
     *取消收藏
     */
    @RequestMapping(value = "/deleteCollect",method = RequestMethod.GET)
    public Object deleteCollect(@RequestParam("id") Integer id){
        return collectService.removeById(id);
    }
    /**
     *取消多个收藏
     */
    @RequestMapping(value = "/someDeleteCol",method = RequestMethod.GET)
    public Object someDeleteCol(@RequestParam("id") List ids){
        return collectService.removeByIds(ids);
    }
    /**
     *根据用户id 和歌曲id   查询某个歌曲是否收藏
     */
    @RequestMapping(value = "/getCollectOfUserId",method = RequestMethod.GET)
    public Object getCollectOfUserId(@RequestParam("userId") Integer userId){
        return collectService.list(new QueryWrapper<Collect>().eq("user_id",userId));
    }
}
