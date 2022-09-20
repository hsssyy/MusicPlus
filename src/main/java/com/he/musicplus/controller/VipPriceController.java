package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.he.musicplus.domain.VipPrice;
import com.he.musicplus.service.VipPriceService;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Vip Price 价格管理
 */
@RestController
@RequestMapping("/vipPrice")
public class VipPriceController {
    @Autowired
    private VipPriceService vipPriceService;

    /**
     * 查询vip_price 放到表格
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Object vipPriceInfo(){
        return vipPriceService.list();
    }
    /**
     * 删除一个
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam("id")Integer id){
        JSONObject jsonObject = new JSONObject();
        if(vipPriceService.removeById(id)){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"删除成功");
            return jsonObject;
        }else{
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"删除失败");
            return jsonObject;
        }
    }
    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object update(@RequestBody VipPrice vipPrice){
       JSONObject jsonObject = new JSONObject();
        if(vipPriceService.updateById(vipPrice)){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"修改成功");
            return jsonObject;
        }else{
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"修改失败");
            return jsonObject;
        }
    }
}
