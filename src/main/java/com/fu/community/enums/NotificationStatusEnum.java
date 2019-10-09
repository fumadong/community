package com.fu.community.enums;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/9 17:47
 */
public enum  NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
