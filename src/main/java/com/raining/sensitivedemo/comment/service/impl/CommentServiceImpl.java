package com.raining.sensitivedemo.comment.service.impl;

import com.raining.sensitivedemo.comment.repository.dao.CommentDao;
import com.raining.sensitivedemo.comment.repository.entity.CommentDO;
import com.raining.sensitivedemo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public CommentDO queryComment(Long commentId) {
        return commentDao.getById(commentId);
    }
}
