package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.blog.admin.dao.mapper.ArticleBodyMapper;
import com.blog.admin.dao.mapper.ArticleMapper;
import com.blog.admin.dao.mapper.ArticleTagMapper;
import com.blog.admin.dao.pojo.Admin;
import com.blog.admin.dao.pojo.Article;
import com.blog.admin.dao.pojo.ArticleBody;
import com.blog.admin.dao.pojo.ArticleTag;
import com.blog.admin.service.ArticleService;
import com.blog.admin.utils.AdminThreadLocal;
import com.blog.admin.vo.Result;
import com.blog.admin.vo.TagVo;
import com.blog.admin.vo.params.ArticleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2021/11/17 11:23
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;


    @Override
    public Result publish(ArticleParam articleParam) {
        Admin admin = AdminThreadLocal.get();// 获取 user对象的前提：此接口需要加入到登录拦截器
        /**
         *1. 发布文章，目的：构建article对象
         * 1. new Article
         * 2. 作者id：当前的登录用户
         * 3. 标签：要将标签加入到与文章关联的关联列表（ms_article_tag表）中
         * 4. body:文章内容存储
         */
        Article article = new Article();
        boolean isEdit = false;
        // 修改文章
        if (articleParam.getId() != null) {
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.valueOf(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true;
        } else {
            // 新增文章
            article.setAuthorId(admin.getId());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setSummary(articleParam.getSummary());
            article.setTitle(articleParam.getTitle());
            article.setViewCounts(0);
            article.setWeight(Article.Article_Common);
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            // 插入文章后会生成文章id
            this.articleMapper.insert(article);
        }

        // tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            Long articleId = article.getId();
            // 删除原来的tag
            if (isEdit) {
                LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ArticleTag::getArticleId, articleId);
                articleTagMapper.delete(queryWrapper);
            }
            // 插入更新后的tag
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        // body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        if (isEdit) {
            LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleBody::getArticleId, article.getId());
            articleBodyMapper.update(articleBody, queryWrapper);
        } else {
            articleBodyMapper.insert(articleBody);
            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }

//        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(String.valueOf(article.getId()));
//        if (isEdit) {
//            ArticleMessage articleMessage = new ArticleMessage();
//            articleMessage.setArticleId(articleParam.getId());
//            // 发送一条消息给 roketmq：当前文章更新了，更新一下缓存吧
//            rocketMQTemplate.convertAndSend("blog-update-article", articleMessage);
//        }
//        return Result.success(articleVo);
        return Result.success(null);
    }


}
