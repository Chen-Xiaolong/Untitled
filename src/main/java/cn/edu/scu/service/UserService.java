package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;

public interface UserService {
    UserResult register(String name, String password);

    UserResult login(String name, String password);
}
