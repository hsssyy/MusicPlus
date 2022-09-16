package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.musicplus.domain.Comment;
import com.he.musicplus.domain.Vip;
import com.he.musicplus.service.VipService;
import com.he.musicplus.utils.ChangeTime;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * VIP 控制类
 */
@RestController
@RequestMapping("/vip")
public class VipController {
    @Autowired
    private VipService vipService;
    /**
     * 开通VIP 或者续费。
     * 添加
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addViP(HttpServletRequest request) throws ParseException {

        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId"); // 用户id
        String selectMonth = request.getParameter("selectMonth");  //开通的月数
        String payAmount = request.getParameter("payAmount");//支付金额
        String openTime = request.getParameter("openTime"); //开通时间
        String duedate = request.getParameter("duedate").trim();//到期时间

        boolean flag = false;

        Vip vip = new Vip();

        vip.setConsumerId(Integer.parseInt(userId));

        Vip yetVip = vipService.getOne(new QueryWrapper<Vip>().eq("consumer_id",userId));
        //获取总金额  得判断是否新开通的用户  是就添加，不是就更新时间和金额
        if(yetVip!=null){

            Integer moneys = yetVip.getMoneys();
            yetVip.setMoneys(Integer.parseInt(payAmount)+moneys);//更新总金额

            yetVip.setCreateTime(ChangeTime.StringChangeTime(openTime) );//开通时间
            yetVip.setEndTime(ChangeTime.StringChangeTime(duedate));//结束时间
            yetVip.setMonthly(Integer.parseInt(selectMonth));
            flag = vipService.updateById(yetVip);
        }else{
            vip.setMoneys(Integer.parseInt(payAmount));//总金额
            vip.setCreateTime(ChangeTime.StringChangeTime(openTime));//开通时间
            vip.setEndTime(ChangeTime.StringChangeTime(duedate));//结束时间
            vip.setMonthly(Integer.parseInt(selectMonth));
            flag = vipService.save(vip);
        }
        if (flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"开通成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"开通失败");
        return jsonObject;
    }

    /**
     * 根据用户id 查询VIP 是否开通
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectTime",method = RequestMethod.GET)
    public Object selectTime(@RequestParam("userId") Integer userId){
        JSONObject jsonObject = new JSONObject();
        Vip vip = vipService.getOne(new QueryWrapper<Vip>().eq("consumer_id",userId));
        if(vip != null){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put("vipMsg",vip);
        }else{
            jsonObject.put(Consts.CODE,0);
        }
        return jsonObject;

    }
}
