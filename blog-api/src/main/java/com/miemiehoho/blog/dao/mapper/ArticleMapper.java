package com.miemiehoho.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miemiehoho.blog.dao.dos.Archives;
import com.miemiehoho.blog.dao.pojo.Article;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author miemiehoho
 * @date 2021/11/17 10:45
 */
@Component//把普通pojo实例化到spring容器中
public interface ArticleMapper extends BaseMapper<Article> {


    List<Archives> listArchives();


    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
}
