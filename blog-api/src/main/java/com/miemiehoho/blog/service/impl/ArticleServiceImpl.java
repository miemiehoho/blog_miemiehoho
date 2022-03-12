package com.miemiehoho.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miemiehoho.blog.dao.dos.Archives;
import com.miemiehoho.blog.dao.mapper.ArticleBodyMapper;
import com.miemiehoho.blog.dao.mapper.ArticleMapper;
import com.miemiehoho.blog.dao.mapper.ArticleTagMapper;
import com.miemiehoho.blog.dao.pojo.Article;
import com.miemiehoho.blog.dao.pojo.ArticleBody;
import com.miemiehoho.blog.dao.pojo.ArticleTag;
import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.service.*;
import com.miemiehoho.blog.utils.UserThreadLocal;
import com.miemiehoho.blog.vo.*;
import com.miemiehoho.blog.vo.params.PageParams;
import com.miemiehoho.blog.vo.params.ArticleParam;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.分页查询数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPage_size());//mybatisplus的page
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();//查询条件
////        queryWrapper.orderByDesc(Article::getWeight);// 按照置顶排序
////        queryWrapper.orderByDesc(Article::getCreateDate);//按照时间倒序排列  order by create_date desc
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null) {
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTagList) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                // and id in(1,2,3...)
//                queryWrapper.in(Article::getId, articleIdList);
//            }
//        }
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);//分页查询
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPage_size());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);//limit后要加空格，不然会把数据拼到一起
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.last("limit " + limit);
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询文章信息
         * 2.根据 bodyId和categoryid 做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo artivleVo = copy(article, true, true, true, true);
        // 查看完文章，应该更新阅读数
        // 查看完文章后，本应该直接返回数据，但这个时候做了一个更新操作，更新时候加写锁，阻塞其他的读操作，性能就会比较低
        // 更新增加的此次接口的耗时，如果一旦更新出问题，不能影响查看文章的操作
        // 使用线程池，可以把更新操作扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(artivleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();// 获取 user对象的前提：此接口需要加入到登录拦截器
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
            article.setAuthorId(sysUser.getId());
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

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));

        // 更新缓存
        if (isEdit) {
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(articleParam.getId());
            // 发送一条消息给 roketmq：当前文章更新了，更新一下缓存吧
            // 消息队列名：blog-update-article
            rocketMQTemplate.convertAndSend("blog-update-article", articleMessage);
        }
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, true, true, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, true, true, false, false));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 并不是所有接口都需要标签、作者
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            UserVo userVo = sysUserService.findUserVoById(authorId);
            articleVo.setAuthor(userVo);
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }


    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


}
