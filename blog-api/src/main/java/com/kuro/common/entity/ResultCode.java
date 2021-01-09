package com.kuro.common.entity;

/**
 * @Description: 返回码定义
 * 规定:
 * #200表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 */
@SuppressWarnings("all")
public enum ResultCode implements CustomizeResultCode {
    /* 成功 */
    SUCCESS(200, "请求成功！", true),
    /* 默认失败 */
    COMMON_FAIL(250, "请求失败！", false),

    /* 参数错误：1000～1100 */
    PARAM_NOT_VALID(1001, "参数无效！", false),
    PARAM_IS_BLANK(1002, "参数为空！", false),
    PARAM_TYPE_ERROR(1003, "参数类型错误！", false),
    PARAM_NOT_COMPLETE(1004, "参数缺失！", false),
    PARAM_CAN_USE(1005, "参数可用！", true),

    /* 用户相关：1200~1300 */
    USER_NOT_LOGIN(1201, "尚未登录！", false),
    USER_TOKEN_EXPIRED(1202, "令牌已过期！", false),
    USER_CREDENTIALS_ERROR(1203, "账号或密码错误！", false),
    USER_ACCOUNT_LOCKED(1204, "账号被锁定！", false),
    USER_ACCOUNT_NOT_EXIST(1205, "用户不存在！", false),
    USER_ACCOUNT_ALREADY_EXIST(1206, "账号已存在！", false),
    USER_ACCOUNT_USE_BY_OTHERS(1207, "账号下线！", false),
    USER_UPDATE_STATUS_NOT_EXIST(1208, "要更新状态的用户不存在！", false),
    TOKEN_ERROR(1209, "token错误，请重新登入！", false),
    LOGIN_SUCCESS(1210, "登录成功！", true),
    PASSWORD_CHANGE_ERROR(1211, "密码修改失败！", false),
    PASSWORD_CHANGE_SUCCESS(1212, "密码修改成功！", true),
    // 注册相关
    PHONE_CODE_SEND(1251, "验证码已发送手机，请注意查收！", true),
    CODE_ERROR(1252, "验证码错误！", false),
    PHONE_ACCOUNT_ALREADY_EXIST(1253, "该手机号已被注册！", true),
    NICKNAME_ACCOUNT_ALREADY_EXIST(1254, "该昵称已被占用！", true),
    REGISTER_SUCCESS(1255, "注册成功！", true),

    /* 留言评论相关：1400~1500 */
    MESSAGE_SUCCESS(1401, "留言成功！", true),
    MESSAGE_ERROR(1402, "留言失败！", false),
    MESSAGE_IS_BLANK(1403, "留言内容不能为空！", false),
    COMMENT_IS_BLANK(1404, "评论内容不能为空！", false),
    COMMENT_SUCCESS(1405, "评论成功！", true),
    COMMENT_ERROR(1406, "评论失败！", false),

    /* 文章相关：1600~1700 */
    ARTICLE_ALREADY_NOT_EXIST(1601, "该文章不存在！", false),
    ARTICLE_ALREADY_EXIST(1602, "文章标题重复！", false),

    /* 业务错误：1800~1900 */
    NO_PERMISSION(3001, "没有权限！", false),

    /* Tag标签相关：2000~2100 */
    TAG_EXIST(2001, "该标签已经存在！", false),
    TAG_DELETE_ERROR_BECAUSE_USE(2002, "该标签还在使用中，删除失败！", false),

    /* Diary日记记录相关：2200~2300 */
    DIARY_NOT_EXIST(2201, "该记录不存在！", false),

    /* 分类相关：2400~2500 */
    CATEGORY_EXIST(2401, "该分类已经存在！", false),
    CATEGORY_DELETE_ERROR_BECAUSE_USE(2402, "该分类还在使用中，删除失败！", false),

    ADD_SUCCESS(3801, "添加成功！", true),
    ADD_ERROR(3802, "添加失败！", false),

    UPDATE_SUCCESS(3803, "修改成功！", true),
    UPDATE_ERROR(3804, "修改失败！", false),

    DELETE_SECCESS(3805, "删除成功！", true),
    DELETE_ERROR(3806, "删除失败！", false),

    /*运行时异常*/
    ARITHMETIC_EXCEPTION(9001, "算数异常！", false);

    private Integer code;

    private String message;

    private Boolean flag;

    ResultCode(Integer code, String message, Boolean flag) {
        this.code = code;
        this.message = message;
        this.flag = flag;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Boolean getFlag() {
        return flag;
    }

}