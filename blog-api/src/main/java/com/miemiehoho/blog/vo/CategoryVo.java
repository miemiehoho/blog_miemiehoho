package com.miemiehoho.blog.vo;

import lombok.Data;

/**
 * 文章类别
 *
 * @author miemiehoho
 * @date 2021/12/19 22:23
 */
@Data
public class CategoryVo {

    private String id;

    private String avatar;

    private String categoryName;

    private String description;
}
