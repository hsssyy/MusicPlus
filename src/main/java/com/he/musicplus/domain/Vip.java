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
 * @TableName vip
 */
@TableName(value ="vip")
@Data
public class Vip implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer consumerId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date endTime;

    /**
     * 
     */
    private Integer moneys;

    /**
     * 
     */
    private Integer months;

    /**
     * 
     */
    private Integer monthly;

    /**
     * 
     */
    private Integer level;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}