package com.fu.community.service;

import com.fu.community.dto.NotificationDTO;
import com.fu.community.dto.PaginationDTO;
import com.fu.community.dto.QuestionDTO;
import com.fu.community.enums.NotificationStatusEnum;
import com.fu.community.enums.NotificationTypeEnum;
import com.fu.community.exception.CustomizeErrorCode;
import com.fu.community.exception.CustomizeException;
import com.fu.community.mapper.NotificationMapper;
import com.fu.community.mapper.UserMapper;
import com.fu.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/9 20:22
 */

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        //计算页数的偏移量
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPages;
        //Integer totalCount = questionMapper.countByUserId(userId);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);

        //计算页码总数
        if(totalCount%size==0){
            totalPages=totalCount/size;
        }else{
            totalPages = totalCount/size+1;
        }
        if(page<1){
            page=1;
        }
        if(page>totalPages){
            page=totalPages;
        }

        //totalCount = questionMapper.count();
        paginationDTO.setPagination(totalPages,page);
        Integer offset = size*(page-1);
        //每一页的列表
        //List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if(notifications.size()==0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
