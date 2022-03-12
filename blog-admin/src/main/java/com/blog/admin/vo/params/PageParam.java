package com.blog.admin.vo.params;

import lombok.Data;

/**
 * @author miemiehoho
 * @date 2022/1/12 11:10
 */
@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

    private String queryString;
}
