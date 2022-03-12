package com.miemiehoho.blog.service;

import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.TagVo;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2021/11/17 17:19
 */
public interface TagService {
     List<TagVo> findTagsByArticleId(Long articleId);

     Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
