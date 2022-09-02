package com.he.musicplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.musicplus.domain.Admin;
import com.he.musicplus.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MusicplusApplicationTests {
    @Autowired
    private AdminService adminService;
    @Test
    void contextLoads() {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","admin").eq("password","2");
        Admin one = adminService.getOne(queryWrapper);
        System.out.println(one);
    }

}
