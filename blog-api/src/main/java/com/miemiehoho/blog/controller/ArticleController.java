package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.common.aop.LogAnnotation;
import com.miemiehoho.blog.common.cache.Cache;
import com.miemiehoho.blog.service.ArticleService;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.PageParams;
import com.miemiehoho.blog.vo.params.ArticleParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author miemiehoho
 * @date 2021/11/17 10:51
 */
@Api(value = "文章", tags = "文章")
@RestController//json数据进行交互
@RequestMapping("articles")//映射的对应路径
public class ArticleController {


    @Autowired//自动注入
    private ArticleService articleService;

    /**
     * 首页 文章列表
     *
     * @param pageParams
     * @return
     */
    @LogAnnotation(module = "文章", operator = "获取文章列表")// 加上此注解代表要对此接口记录日志
    @ApiOperation("首页文章列表")
    @PostMapping
    @Cache(expire = 5 * 60 * 1000, name = "list_article")// 切点
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }


    @ApiOperation("最热文章")
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000, name = "hot_article")// 切点
    public Result hotArticle() {
        int limit = 6;
        return articleService.hotArticle(limit);
    }

    @ApiOperation("最新文章")
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000, name = "new_article")// 切点
    public Result newArticle() {
        int limit = 6;
        return articleService.newArticle(limit);
    }

    @ApiOperation("文章归档")
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @ApiOperation("文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true)
    })
    @PostMapping("view/{id}")
    @Cache(expire = 5 * 60 * 1000, name = "view_article")// 切点
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    @ApiOperation("发布文章")
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

    @ApiOperation("查看文章")
    @PostMapping("{id}")
    public Result articleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

}
