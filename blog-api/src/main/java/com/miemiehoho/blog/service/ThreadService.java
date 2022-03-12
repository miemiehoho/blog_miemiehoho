package com.miemiehoho.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miemiehoho.blog.dao.mapper.ArticleMapper;
import com.miemiehoho.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author miemiehoho
 * @date 2021/12/20 8:50
 */
@Component
public class ThreadService {


    // 希望此操作在线程池执行，不会影响原有的主线程
    @Async("taskExcutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Article updateArticle = new Article();
        updateArticle.setViewCounts(article.getViewCounts() + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        // 设置一个在多线程环境下，保证线程安全
        updateWrapper.eq(Article::getViewCounts, article.getViewCounts());
        articleMapper.update(updateArticle, updateWrapper);
        // update article set view_count = 100 where view_count = 99 and id = 123
    }
}
