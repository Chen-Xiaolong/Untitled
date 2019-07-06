package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;



public interface AdminService {
    UserResult register(String name, String password);

    UserResult login(String name, String password);

    UserResult addEmployment(String name, String hash, String duty, String[] skills);

    UserResult addDuty(String name, String hash, String duty, String description);

    UserResult addSkill(String name, String hash, String[] skill);

    UserResult createFPTreeModel(String name, String hash, String duty);

    UserResult createRecommendModel(String name, String hash);
}
