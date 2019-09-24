package com.fu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "您的问题不存在，要不换一个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何为或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004, "服务器冒烟了！！！！,稍后再试试吧！"),
    TYPE_PARAM_WRONG(2005, "评论类型参数错误或不存在"),
    COMMENT_NOT_FOUND(2006, "您的评论不存在，要不换一个试试？");
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}