package cn.edu.scu.enums;

public enum UserResultEnum {
    LOGIN_SUCCESS(1,"登陆成功"),
    REGISTER_SUCCESS(2,"注册成功"),
    OPERATION_SUCCESS(3,"操作成功"),

    LOGIN_FAIL(-1, "登陆失败"),
    USERNAME_EXIST(-2,"用户名已存在"),
    INSUFFICIENT_PARAMETERS(-3, "参数不足"),
    UNLOGINED(-4, "未登录"),
    SKILL_DUPLICATED(-5, "重复添加"),

    UNKNOWN_ERROR(-999,"未知错误"),
    ;


    private int status;
    private String info;

    UserResultEnum(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }
}
