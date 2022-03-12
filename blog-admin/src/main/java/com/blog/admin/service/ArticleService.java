package com.blog.admin.service;

import com.blog.admin.vo.Result;
import com.blog.admin.vo.params.ArticleParam;

/**
 * @author miemiehoho
 * @date 2021/11/17 11:04
 */
public interface ArticleService {

    Result publish(ArticleParam articleParam);
}
