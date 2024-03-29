package com.fu.community.controller;

import com.fu.community.cache.HotTagCache;
import com.fu.community.dto.PaginationDTO;
import com.fu.community.mapper.UserMapper;
import com.fu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "10")Integer size,
                        @RequestParam(value = "search",defaultValue = "")String search,
                        @RequestParam(name = "tag", defaultValue = "") String tag) {
        //加载首页 通过cookies中的token 判断用户是否登录过
        PaginationDTO pagination = questionService.list(search,tag,page,size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        return "index";
    }
}
