package com.blog.admin.controller;


import com.blog.admin.service.ArticleService;
import com.blog.admin.vo.Result;
import com.blog.admin.vo.params.ArticleParam;
import io.swagger.annotations.Api;
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


    @ApiOperation("发布文章")
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }



}
