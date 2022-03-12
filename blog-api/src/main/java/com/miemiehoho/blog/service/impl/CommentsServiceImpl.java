package com.miemiehoho.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miemiehoho.blog.service.SysUserService;
import com.miemiehoho.blog.utils.UserThreadLocal;
import com.miemiehoho.blog.dao.mapper.CommentMapper;
import com.miemiehoho.blog.dao.pojo.Comment;
import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.service.CommentsService;
import com.miemiehoho.blog.vo.CommentVo;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.UserVo;
import com.miemiehoho.blog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author miemiehoho
 * @date 2021/12/20 15:08
 */
@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getLevel, 1);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        // 获取用户信息
        SysUser sysUser = UserThreadLocal.get();

        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(commentParam.getParent() == null ? 0 : commentParam.getParent());
        comment.setToUid(commentParam.getToUserId() == null ? 0 : commentParam.getToUserId());
        commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        // 时间
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 作者信息
        UserVo userVo = sysUserService.findUserVoById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        // 子评论
        if (comment.getLevel() == 1) {
            List<CommentVo> children = findCommentsByParentId(comment.getId());
            commentVo.setChildren(children);
        }
        // toUser
        if (comment.getLevel() > 1) {
            UserVo toUser = sysUserService.findUserVoById(comment.getToUid());
            commentVo.setToUser(toUser);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long parentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, parentId);
        queryWrapper.eq(Comment::getLevel, 2);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }
}
