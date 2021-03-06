package cn.edu.scu.dto;

import cn.edu.scu.entity.User;
import cn.edu.scu.enums.UserResultEnum;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResult {
    private int status;

    private String info;

    private User user;

    private String list;

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

    public UserResult(UserResultEnum userResultEnum, String list) {
        this.status = userResultEnum.getStatus();
        this.info = userResultEnum.getInfo();
        this.list = list;
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

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", user=" + user +
                ", list=" + list +
                '}';
    }
}
