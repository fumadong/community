package com.fu.community.service;

import com.fu.community.enums.CommentTypeEnum;
import com.fu.community.exception.CustomizeErrorCode;
import com.fu.community.exception.CustomizeException;
import com.fu.community.mapper.CommentMapper;
import com.fu.community.mapper.QuestionExtMapper;
import com.fu.community.mapper.QuestionMapper;
import com.fu.community.model.Comment;
import com.fu.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
        public void insert(Comment comment){
            //判断问题是否存在
            if(comment.getParentId()==null||comment.getParentId()==0){
                throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
            }
            //判断评论类型是否存在
            if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){
                throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
            }
            //判断评论类型的值
            if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
                //回复评论
                Comment dbComment=commentMapper.selectByPrimaryKey(comment.getParentId());
                if(dbComment==null){
                    throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
                }
                commentMapper.insert(comment);
            }else {
                //回复问题
                Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
                if(question==null){
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                commentMapper.insert(comment);
                question.setCommentCount(1);
                questionExtMapper.intCommentCount(question);
            }
        }
}
