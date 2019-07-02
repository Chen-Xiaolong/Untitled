package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;

public interface AdminService {
    UserResult register(String name, String password);

    UserResult login(String name, String password);
}
