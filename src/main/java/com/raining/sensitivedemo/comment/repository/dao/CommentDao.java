package com.raining.sensitivedemo.comment.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raining.sensitivedemo.comment.repository.entity.CommentDO;
import com.raining.sensitivedemo.comment.repository.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends ServiceImpl<CommentMapper, CommentDO> {

}
