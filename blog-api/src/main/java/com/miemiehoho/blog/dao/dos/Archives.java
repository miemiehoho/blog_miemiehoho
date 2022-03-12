package com.miemiehoho.blog.dao.dos;

import lombok.Data;

/**
 * @author miemiehoho
 * @date 2021/11/18 9:57
 */
@Data
public class Archives {

    private Integer year;

    private Integer month;
    
    private Long count;
}
