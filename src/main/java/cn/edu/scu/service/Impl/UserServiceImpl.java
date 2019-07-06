package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dao.UserDao;
import cn.edu.scu.dao.UserSkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.Skill;
import cn.edu.scu.entity.User;
import cn.edu.scu.entity.UserSkill;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.service.UserService;
import cn.edu.scu.util.CreateMd5;
import cn.edu.scu.util.CreateSalt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private UserSkillDao userSkillDao;

    @Autowired
    private CreateSalt createSalt;

    @Autowired
    private CreateMd5 createMd5;

    @Override
    public UserResult register(String username, String password) {
        if (username.length() < 5 || password.equals("")) {
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = userDao.selectCountByUserName(username);
        if (result >= 1) {
            return new UserResult(UserResultEnum.USERNAME_EXIST);
        }
        String salt = createSalt.getSalt();
        String passwordMd5 = createMd5.getMd5ByTwoParameter(password, salt);
        result = userDao.insertOne(username, passwordMd5, salt);
        if (result != 1) {
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.REGISTER_SUCCESS);
        }
    }

    @Override
    public UserResult login(String username, String password) {
        if (username.equals("") || password.equals("")) {
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = userDao.selectCountByUserName(username);
        if (result != 1) {
            return new UserResult(UserResultEnum.LOGIN_FAIL);
        } else {
            User user = userDao.selectByUserName(username);
            if (!user.getUserPasswordHash().equals(
                    createMd5.getMd5ByTwoParameter(password, user.getUserPasswordSalt()))) {
                return new UserResult(UserResultEnum.LOGIN_FAIL);
            } else {
//                user.setUserPasswordHash("");
//                user.setUserPasswordSalt("");
//                String userPasswordHash = user.getUserPasswordHash();
//                return new UserResult(1,
//                        createMd5.getMd5ByTwoParameter(username, userPasswordHash), user);
                return new UserResult(UserResultEnum.LOGIN_SUCCESS, user);
            }
        }
    }

    @Override
    public UserResult addSkill(String name, String hash, String[] skillNames) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        User user = userDao.selectByUserName(name);
        if(skillNames != null)
            for (String skillName : skillNames) {
                String[] sp = skillName.split(":");
                Skill skill = skillDao.queryBySkillName(skillTrim(sp[0]));
                int proficiency = Integer.valueOf(sp[1].trim());
                if(skill == null){
                    return new UserResult(UserResultEnum.UNKNOWN_ERROR);
                }
                userSkillDao.insertOne(user.getUserId(), skill.getSkillId(), proficiency);
            }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS);
    }

    @Override
    public UserResult deleteSkill(String name, String hash, String skillName) {
        if(!isLogin(name, hash)) {
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        User user = userDao.selectByUserName(name);
        Skill skill = skillDao.queryBySkillName(skillName);
        if(skill == null || user == null){
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            int result = userSkillDao.deleteOne(user.getUserId(), skill.getSkillId());
            if(result == 0){
                return new UserResult(UserResultEnum.UNKNOWN_ERROR);
            } else {
                return new UserResult(UserResultEnum.OPERATION_SUCCESS);
            }
        }
    }

    @Override
    public UserResult querySkill(String name, String hash) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        List<String> skills = new ArrayList<>();
        User user = userDao.selectByUserName(name);
        List<UserSkill> userSkills = userSkillDao.queryByUserId(user.getUserId());
        for (UserSkill userSkill : userSkills) {
            skills.add(skillDao.queryBySkillId(userSkill.getSkillId()).getSkillName());
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS, Arrays.toString(skills.toArray()));
    }

    private boolean isLogin(String name, String hash) {
        if (name.equals("") || hash.equals(""))
            return false;
        int result = userDao.selectCountByUserName(name);
        if (result != 1) {
            return false;
        } else {
            User user = userDao.selectByUserName(name);
//            System.out.println(createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()));
//            System.out.println(hash);
//            System.out.println(createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()).equals(hash));
            return createMd5.getMd5ByTwoParameter(user.getUserName(), user.getUserPasswordHash()).equals(hash);
        }
    }

    private String skillTrim(String skill){
        if(skill.startsWith("["))
            skill = skill.substring(1);
        if(skill.endsWith("]"))
            skill = skill.substring(0, skill.length()-1);
        return skill.trim();
    }
}
