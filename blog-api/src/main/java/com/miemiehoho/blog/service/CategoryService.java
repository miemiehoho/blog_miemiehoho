package com.miemiehoho.blog.service;

import com.miemiehoho.blog.vo.CategoryVo;
import com.miemiehoho.blog.vo.Result;

/**
 * @author miemiehoho
 * @date 2021/12/20 8:05
 */
public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryeDetailById(Long id);
}
