package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.service.CommentsService;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.CommentParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author miemiehoho
 * @date 2021/12/20 13:35
 */
@Api(value = "评论", tags = "评论")
@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId) {
        return commentsService.commentsByArticleId(articleId);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam) {
        return  commentsService.comment(commentParam);
    }


}
