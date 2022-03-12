package com.miemiehoho.blog.service;

import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.CommentParam;

/**
 * @author miemiehoho
 * @date 2021/12/20 15:07
 */
public interface CommentsService {


    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);

}
