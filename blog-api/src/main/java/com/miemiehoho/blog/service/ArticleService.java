package com.miemiehoho.blog.service;

import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.PageParams;
import com.miemiehoho.blog.vo.params.ArticleParam;

/**
 * @author miemiehoho
 * @date 2021/11/17 11:04
 */
public interface ArticleService {
    /**
     * 分页查询文章列表
     *
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);


    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
