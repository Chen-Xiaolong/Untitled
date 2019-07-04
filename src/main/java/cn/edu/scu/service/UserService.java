package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;

import java.util.List;


public interface UserService {
    UserResult register(String name, String password);

    UserResult login(String name, String password);

    UserResult addSkill(String name, String hash, String[] skillNames);

    UserResult deleteSkill(String name, String hash, String skillName);

    UserResult querySkill(String name, String hash);
}
