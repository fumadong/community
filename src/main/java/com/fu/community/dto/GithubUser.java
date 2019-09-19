package com.fu.community.dto;

import lombok.Data;

/**
 * Github用户信息
 */
@Data
public class GithubUser {

    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
