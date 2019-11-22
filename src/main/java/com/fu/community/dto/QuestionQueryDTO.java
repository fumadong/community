package com.fu.community.dto;

import lombok.Data;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/15 17:46
 */

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
}
