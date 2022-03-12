package com.blog.admin.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.admin.dao.pojo.Article;
import org.springframework.stereotype.Component;

/**
 * @author miemiehoho
 * @date 2021/11/17 10:45
 */
@Component//把普通pojo实例化到spring容器中
public interface ArticleMapper extends BaseMapper<Article> {


}
