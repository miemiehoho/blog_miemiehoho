package com.blog.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2022/1/12 11:35
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private Long total;
}
