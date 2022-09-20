package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Comment;
import com.he.musicplus.service.CommentService;
import com.he.musicplus.utils.ChangeTime;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 评论控制类
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    /**
     * 查询所有评论
     */
    @RequestMapping(value = "/allComment",method = RequestMethod.GET)
    public Object allComment(HttpServletRequest request)  {
        return commentService.listMaps();
    }
    /**
     * 查询某个歌单下的所有评论
     */
    @RequestMapping(value = "/commentOfSongListId",method = RequestMethod.GET)
    public Object commentOfSongListId(@RequestParam("songListId") Integer songListId)  {
        return commentService.listMaps(new QueryWrapper<Comment>().eq("song_list_id",songListId));
    }
    /**
     * 查询某个歌单下的所有评论 以及分页
     */
    @RequestMapping(value = "/commentOfSongListIdPage",method = RequestMethod.GET)
    public Object commentOfSongListIdPage(@RequestParam(value = "pn",defaultValue = "1") Integer pn, @RequestParam("songListId") Integer songListId)  {
        Page<Comment> page = new Page<>(pn,10);
        Page<Comment> commentPage = commentService.page(page, new QueryWrapper<Comment>().eq("song_list_id", songListId));

        for(int i = 0; i<commentPage.getTotal();i++){

        }
        return commentPage;
    }
    /**
     * 添加评论
     */
    /**
     * 添加评论
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addComment(HttpServletRequest request) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId"); // 用户id
        String type = request.getParameter("type");  //评论类型（0歌曲1歌单）
        String songId = request.getParameter("songId");//歌曲id
        String songListId = request.getParameter("songListId"); //歌单id
        String content = request.getParameter("content").trim();//评论内容

        //获取当前时间
        Date date = new Date();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String createTime = sdf.format(date);

        //保存到评论的对象中
        Comment comment = new Comment();
//        Byte by = Byte.parseByte(type);
        comment.setUserId(Integer.parseInt(userId));
        comment.setCreateTime(ChangeTime.StringChangeTime(createTime));
        comment.setType(new Byte(type));

        comment.setUp(0);//初始值点赞数为 0
        if (new Byte(type) == 0){
            comment.setSongId(Integer.parseInt(songId));
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        boolean flag = commentService.save(comment);
        if (flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"评论成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"评论失败");
        return jsonObject;
    }

    /**
     * 给某个评论点赞
     */
    @RequestMapping(value = "like",method = RequestMethod.POST)
    public Object setLike(HttpServletRequest request){//
        String id = request.getParameter("id").trim(); //主键
        String up = request.getParameter("up").trim();//点赞
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));
        JSONObject jsonObject = new JSONObject();
        boolean flag = commentService.updateById(comment);
        if (flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"点赞成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"点赞失败");
        return jsonObject;
    }
    /**
     * 更新评论
     */
    @RequestMapping(value = "/updateCom",method = RequestMethod.POST)
    public Object updateComment(@RequestBody Comment comment){
        return commentService.updateById(comment);
    }

    /**
     * 删除一条评论
     * @param id
     * @return
     */
    @RequestMapping(value = "/delComment",method = RequestMethod.GET)
    public Object deleteComment(@RequestParam("id") Integer id){
        return  commentService.removeById(id);
    }

    /**
     * 批量删除评论
     * @return
     */
    @RequestMapping(value = "/someDelete" , method = RequestMethod.GET)
    public Object deleteConsumer(@RequestParam("id") List ids){
        return commentService.removeByIds(ids);
    }

}
