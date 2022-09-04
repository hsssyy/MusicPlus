package com.he.musicplus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.musicplus.domain.Consumer;
import com.he.musicplus.domain.Singer;
import com.he.musicplus.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public Object allConsumer(@RequestParam(value = "pn", defaultValue = "1") Integer pn) throws ParseException {
        //分页查询数据
        Page<Singer> singerPage = new Page<>(pn, 3);
        //分页查询结果
        Page<Singer> page = singerService.page(singerPage, null);

        Date time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//这里可以指定日期的格式
        List<Singer> list = page.getRecords();
        for (int i = 0; i < list.size(); i++) {
            Date birth = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).getBirth());//字符串转换为日期格式
            String ByBirth = sdf.format(birth);//指定日期的格式
            list.get(i).setBirth(ByBirth);//修改某一行数据的生日
        }
        return page;
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
        String singerBirth = singer.getBirth().substring(0,9);
        singer.setBirth(singerBirth);
        return singerService.save(singer);
    }
    /**
     * 修改歌手
     */
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public Object updateConsumer(@RequestBody  Singer singer){
        String singerBirth = singer.getBirth().substring(0,10);
        singer.setBirth(singerBirth);
        return singerService.updateById(singer);
    }

}
