package com.fu.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/6 15:29
 */
@Data
public class TagDTO {

    private String categoryName;
    private List<String> tags;


}
