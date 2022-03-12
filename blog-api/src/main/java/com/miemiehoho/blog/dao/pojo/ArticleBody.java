package com.miemiehoho.blog.dao.pojo;

import lombok.Data;

/**
 * @author miemiehoho
 * @date 2021/12/19 22:33
 */
@Data
public class ArticleBody {

    private Long id;

    private String content;

    private String contentHtml;

    private Long articleId;
}
