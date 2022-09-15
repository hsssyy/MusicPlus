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
 * @TableName vip_price
 */
@TableName(value ="vip_price")
@Data
public class VipPrice implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private Integer adminId;

    /**
     * 
     */
    private Integer price;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String viptype;

    /**
     * 
     */
    private String viptime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}