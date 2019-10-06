package com.fu.community.controller;


import com.fu.community.dto.CommentDTO;
import com.fu.community.dto.QuestionDTO;
import com.fu.community.enums.CommentTypeEnum;
import com.fu.community.service.CommentService;
import com.fu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class questionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("question/{id}")
    public String question(@PathVariable(name = "id")Long id,
                           Model model) {
        //累加阅读数
        questionService.incView(id);

        QuestionDTO questionDTO = questionService.getById(id);
        //查询相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
