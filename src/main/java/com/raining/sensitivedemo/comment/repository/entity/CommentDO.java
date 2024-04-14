package com.raining.sensitivedemo.comment.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.raining.sensitivedemo.senstive.ano.SensitiveField;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class CommentDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    @SensitiveField(bind = "content")
    private String content;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 顶级评论ID
     */
    private Long topCommentId;

    /**
     * 0未删除 1 已删除
     */
    private Integer deleted;
}
