package cn.edu.scu.dto;

import cn.edu.scu.entity.User;
import cn.edu.scu.enums.UserResultEnum;

public class UserResult {
    private int status;

    private String info;

    private User user;

    public UserResult(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public UserResult(UserResultEnum userResultEnum) {
        this.status = userResultEnum.getStatus();
        this.info = userResultEnum.getInfo();
    }

    public UserResult(UserResultEnum userResultEnum, User user) {
        this.status = userResultEnum.getStatus();
        this.info = userResultEnum.getInfo();
        this.user = user;
    }

    public UserResult(int status, String info, User user) {
        this.status = status;
        this.info = info;
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", user=" + user +
                '}';
    }
}
