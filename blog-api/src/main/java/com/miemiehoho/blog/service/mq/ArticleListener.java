package com.miemiehoho.blog.service.mq;

import com.alibaba.fastjson.JSON;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.service.ArticleService;
import com.miemiehoho.blog.vo.ArticleMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

/**
 * @author miemiehoho
 * @date 2022/1/26 10:40
 */
@Slf4j
@Component
// consumerGroup：消费者组
// topic：
@RocketMQMessageListener(topic = "blog-update-article", consumerGroup = "blog-update-article-group")
public class ArticleListener implements RocketMQListener<ArticleMessage> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ArticleService articleService;


    @Override
    public void onMessage(ArticleMessage articleMessage) {
        log.info("收到的消息:{}", articleMessage);
        // 更新文章缓存
        String redisKey = "view_article::ArticleController::findArticleById::"
                + DigestUtils.md5Hex(String.valueOf(articleMessage.getArticleId()));
        Result article = articleService.findArticleById(articleMessage.getArticleId());
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(article), Duration.ofMillis(5 * 60 * 1000));
        log.info("更新了缓存:{}", redisKey);
        // 删除文章列表缓存
        Set<String> keys = redisTemplate.keys("list_article*");
        keys.forEach(s ->
                {
                    redisTemplate.delete(s);
                    log.info("删除了文章列表缓存:{},s");
                }
        );
    }
}
