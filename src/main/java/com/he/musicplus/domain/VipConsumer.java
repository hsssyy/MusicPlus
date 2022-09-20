package com.he.musicplus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class VipConsumer {

    private Integer id;
    private String username;
    private String password;
    private Byte sex;
    private String phoneNum;
    private String email;
    private Date birth;
    private String introduction;
    private String location;
    private String avator;
    private Date createTime;
    private Date updateTime;


    private Integer consumerId;
    private Date createTimeVip;//开通vip  时间
    private Date endTime;
    private Integer moneys;
    private Integer months;
    private Integer monthly;
    private Integer level;
}
/*
private Integer id;
    private String username;
    private String password;
    private Byte sex;
    private String phoneNum;
    private String email;
    private Date birth;
    private String introduction;
    private String location;
    private String avator;
    private Date createTime;
    private Date updateTime;
 */
