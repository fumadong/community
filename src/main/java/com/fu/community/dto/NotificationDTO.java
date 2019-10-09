package com.fu.community.dto;

import com.fu.community.model.User;
import lombok.Data;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/9 20:20
 */

@Data
public class NotificationDTO {

    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
