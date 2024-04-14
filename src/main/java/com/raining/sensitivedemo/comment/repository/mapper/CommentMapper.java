package com.raining.sensitivedemo.comment.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.raining.sensitivedemo.comment.repository.entity.CommentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {
}
