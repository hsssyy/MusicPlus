package com.he.musicplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName rank
 */
@TableName(value ="rank")
@Data
public class Rank implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer songListId;

    /**
     * 
     */
    private Integer consumerId;

    /**
     * 
     */
    private Integer score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}