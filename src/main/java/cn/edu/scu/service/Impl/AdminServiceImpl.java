package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.AdminDao;
import cn.edu.scu.dao.DutyDao;
import cn.edu.scu.dao.EmploymentDao;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.Skill;
import cn.edu.scu.entity.User;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.service.AdminService;
import cn.edu.scu.util.CreateMd5;
import cn.edu.scu.util.CreateSalt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private EmploymentDao employmentDao;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private DutyDao dutyDao;

    @Autowired
    private CreateSalt createSalt;

    @Autowired
    private CreateMd5 createMd5;

    @Override
    public UserResult register(String username, String password) {
        if(username.length() < 5||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = adminDao.selectCountByUserName(username);
        if (result >= 1) {
            return new UserResult(UserResultEnum.USERNAME_EXIST);
        }
        String salt = createSalt.getSalt();
        String passwordMd5 = createMd5.getMd5ByTwoParameter(password, salt);
        result = adminDao.insertOne(username, passwordMd5, salt);
        if (result != 1) {
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.REGISTER_SUCCESS);
        }
    }

    @Override
    public UserResult login(String username, String password) {
        if(username.equals("")||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = adminDao.selectCountByUserName(username);
        if (result != 1) {
            return new UserResult(UserResultEnum.LOGIN_FAIL);
        } else {
            User user = adminDao.selectByUserName(username);
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
    public UserResult addEmployment(String name, String hash, String duty, String[] skills) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        int index = employmentDao.getMaxIndex()+1;
        int dutyId = dutyDao.queryByDutyName(duty).getDutyId();
        if(skills != null)
            for (String skill : skills) {
                skill = skillTrim(skill);
                int skillId = skillDao.queryBySkillName(skill).getSkillId();
                employmentDao.insertOne(index, dutyId, skillId);
            }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS);
    }

    private boolean isLogin(String name, String hash) {
        if (name.equals("") || hash.equals(""))
            return false;
        int result = adminDao.selectCountByUserName(name);
        if (result != 1) {
            return false;
        } else {
            User user = adminDao.selectByUserName(name);
            return createMd5.getMd5ByTwoParameter(name, user.getUserPasswordHash()).equals(hash);
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
