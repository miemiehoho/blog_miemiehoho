package com.miemiehoho.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2021/12/20 15:21
 */
@Data
public class CommentVo {

//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> children;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
