package com.raining.sensitivedemo.comment.service;

import com.raining.sensitivedemo.comment.repository.entity.CommentDO;

public interface CommentService {

    CommentDO queryComment(Long commentId);
}
