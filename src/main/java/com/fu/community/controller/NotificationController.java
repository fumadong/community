package com.fu.community.controller;

import com.fu.community.dto.NotificationDTO;
import com.fu.community.dto.PaginationDTO;
import com.fu.community.enums.NotificationTypeEnum;
import com.fu.community.model.Notification;
import com.fu.community.model.User;
import com.fu.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/10 1:05
 */
@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);

        if(NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()
             ||NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/" +notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }
    }
}
