package com.miemiehoho.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miemiehoho.blog.dao.pojo.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2021/11/17 10:48
 */
@Component
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询标签列表
     *
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询最热标签前limit条
     *
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
