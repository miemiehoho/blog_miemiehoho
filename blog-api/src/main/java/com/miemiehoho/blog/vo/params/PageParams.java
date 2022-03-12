package com.miemiehoho.blog.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 接受的参数
 *
 * @author miemiehoho
 * @date 2021/11/17 10:55
 */
@Data
@ApiModel("分页参数")
public class PageParams {

    @ApiModelProperty("页数")
    private int page = 1;

    @ApiModelProperty("page_size")
    private int page_size = 10;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    public String getMonth() {
        if (this.month != null && this.month.length() == 1) {
            return "0" + this.month;
        }
        return this.month;
    }
}
