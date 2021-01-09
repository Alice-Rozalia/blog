package com.kuro.common.entity;

public interface CustomizeResultCode {

    /**
     * 获取错误状态码
     * @return 错误状态码
     */
    Integer getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getMessage();

    /**
     * 操作失败与否
     * @return
     */
    Boolean getFlag();
}