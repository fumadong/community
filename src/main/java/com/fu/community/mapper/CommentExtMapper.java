package com.fu.community.mapper;

import com.fu.community.model.Comment;
import org.apache.ibatis.annotations.Update;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/3 23:09
 */
public interface CommentExtMapper {

    @Update("update comment set comment_count = comment_count + #{commentCount} where id = #{id} ")
    int incCommentCount(Comment record);
}
