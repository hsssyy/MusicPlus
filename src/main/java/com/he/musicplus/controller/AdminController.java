package com.he.musicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.musicplus.domain.Admin;
import com.he.musicplus.service.AdminService;
import com.he.musicplus.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @RequestMapping(value = "/admin/login/status" ,method = RequestMethod.POST)
    public Object adminLogin(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session){
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name).eq("password",password);
        Admin one = adminService.getOne(queryWrapper);
        if(one!=null){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"登录成功");
            session.setAttribute("name",name);
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"用户名或密码输错");
        return jsonObject;
    }
    /**
     * 管理员退出登录
     */
    @RequestMapping(value = "/admin/logout",method = RequestMethod.GET)
    public Object adminLogout(HttpSession session){
        session.removeAttribute("name");
        System.out.println("执行了请求");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.CODE,2);
        return jsonObject;
    }
    /**
     * 通过id 查询管理员
     */
    @RequestMapping(value = "/admin/getNameById",method = RequestMethod.GET)
    public Object getNameById(@RequestParam("adminId") Integer adminId ){
        return  adminService.getById(adminId);
    }
}
