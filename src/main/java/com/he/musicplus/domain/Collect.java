package com.he.musicplus.domain;

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
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Byte type;

    /**
     * 
     */
    private Integer songId;

    /**
     * 
     */
    private Integer songListId;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}