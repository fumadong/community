package com.fu.community.mapper;

import com.fu.community.dto.QuestionQueryDTO;
import com.fu.community.model.Question;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionExtMapper {

    @Update("update question set view_count = view_count + #{viewCount} where id = #{id}")
    void incView(Question question);

    @Update("update question set comment_count = comment_count + #{commentCount} where id = #{id}")
    void intCommentCount(Question question);

    @Select("select * from question where id !=#{id} and tag regexp #{tag}")
    List<Question> selectRelated (Question question);

    @Select("select count(*) from question where title regexp #{search} and tag regexp #{tag}")
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    @Select("select * from question title where title regexp #{search} and tag regexp #{tag} order by gmt_create desc limit #{page},#{size}")
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
