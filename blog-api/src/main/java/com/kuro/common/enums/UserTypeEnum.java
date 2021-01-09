package com.kuro.common.enums;

public enum UserTypeEnum {

    // 管理员
    SYSTEM_ADMIN(0),

    // 普通用户
    SYSTEM_USER(1),

    // 高级用户
    SYSTEM_FRIEND(2);

    private int typeCode;

    UserTypeEnum(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }
}
