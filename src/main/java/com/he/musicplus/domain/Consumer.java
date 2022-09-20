package com.he.musicplus.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName consumer
 */
@TableName(value ="consumer")
@Data
public class Consumer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Byte sex;

    /**
     * 
     */
    private String phoneNum;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date birth;

    /**
     * 
     */
    private String introduction;

    /**
     * 
     */
    private String location;

    /**
     * 
     */
    private String avator;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date createTime;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}