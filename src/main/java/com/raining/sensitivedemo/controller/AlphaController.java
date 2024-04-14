package com.raining.sensitivedemo.controller;

import com.raining.sensitivedemo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/comment/{commentId}")
    public String getComment(@PathVariable Long commentId) {

        return commentService.queryComment(commentId).getContent();
    }
}
