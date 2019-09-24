package com.fu.community.mapper;

import com.fu.community.model.Question;
import org.apache.ibatis.annotations.Update;

public interface QuestionExtMapper {

    @Update("update question set view_count = view_count + #{viewCount} where id = #{id}")
    void incView(Question question);

    @Update("update question set comment_count = comment_count + #{commentCount} where id = #{id}")
    void intCommentCount(Question question);
}