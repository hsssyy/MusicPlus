package com.he.musicplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName singer
 */
@TableName(value ="singer")
@Data
public class Singer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Byte sex;

    /**
     * 
     */
    private String pic;

    /**
     * 
     */
    private String birth;

    /**
     * 
     */
    private String location;

    /**
     * 
     */
    private String introduction;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}